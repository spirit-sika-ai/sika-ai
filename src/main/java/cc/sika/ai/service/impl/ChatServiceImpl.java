package cc.sika.ai.service.impl;

import cc.sika.ai.service.MessageService;
import cc.sika.ai.service.message.ChatService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.CopyOnWriteArrayList;

import static cc.sika.ai.constant.DbConstant.NO_ANNEX;
import static cc.sika.ai.constant.GenerationConstant.EMPTY_KEY;
import static cc.sika.ai.constant.MessageType.*;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatModel deepseekModel;
    @Resource
    private MessageService messageService;

    /**
     * ChatModel#stream()会采用自定义任务线程task-*执行任务, 而服务器本身会使用netty负责http请求响应
     * <p>消息缓存列表存在竞态 需要保持prompt缓存为线程安全结构</p>
     */
    private final List<Message> chatHistoryList = new CopyOnWriteArrayList<>();
    // 缓存每次流式响应的结果
    private final StringJoiner responseMessageBuffer = new StringJoiner("");


    @PostConstruct
    public void init() {
        chatHistoryList.add(new SystemMessage("You are a helpful assistant."));
    }

    @Override
    public ChatResponse messageFullReply(String message) {
        // 记录用户消息到数据库, TODO: 定位会话id?
        if (StpUtil.isLogin()) {
            saveUserMessage(message);
        }
        chatHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistoryList);
        ChatResponse chatResponse = deepseekModel.call(prompt);
        addToPrompt(chatResponse);
        saveAssistantMessage(chatResponse);
        return chatResponse;
    }

    @Override
    public Flux<ChatResponse> messageStreamReply(String message) {
        // 将用户发送的数据记录到当前会话
        chatHistoryList.add(new UserMessage(message));
        saveUserMessage(message);
        Prompt prompt = new Prompt(chatHistoryList);
        return deepseekModel.stream(prompt).doOnEach(this::handleSignal);
    }
    
    private void saveUserMessage(String message) {
        cc.sika.ai.entity.po.Message messagePo = cc.sika.ai.entity.po.Message.builder()
            .id(IdUtil.simpleUUID())
            // TODO: 当前为新会话 ? 如何获取会话ID : 当前会话ID
            .sessionId(IdUtil.simpleUUID())
            .sendId(StpUtil.getLoginId().toString())
            .sendName(message)
            .content(message)
            .annex(NO_ANNEX)
            .type(USER_MESSAGE)
            .createTime(LocalDateTime.now())
            .build();
        messageService.save(messagePo);
    }

    private cc.sika.ai.entity.po.Message toMessagePo(String content) {
        return cc.sika.ai.entity.po.Message.builder()
                .id(IdUtil.simpleUUID())
                // TODO: 当前为新会话 ? 如何获取会话ID : 当前会话ID
                .sessionId(IdUtil.simpleUUID())
                .sendId(ASSISTANT_ID)
                .sendName(ASSISTANT_ID)
                .content(content)
                .type(ASSISTANT_MESSAGE)
                .annex(NO_ANNEX)
                .createTime(LocalDateTime.now())
                .build();

    }

    private void saveAssistantMessage(String message) {
        messageService.save(toMessagePo(message));
    }
    
    private void saveAssistantMessage(ChatResponse chatResponse) {
        if (hasOutput(chatResponse)) {
            chatResponse.getResults()
                    .stream().filter(this::hasOutput)
                    .map(Generation::getOutput)
                    .map(AssistantMessage::getText)
                    .filter(CharSequenceUtil::isNotBlank)
                    .map(this::toMessagePo)
                    .forEach(messageService::save);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void handleSignal(Signal<ChatResponse> signal) {
        // 缓存每次响应的结果
        // 需要使用有界缓冲区替换chatHistoryList
        if (signal.isOnNext()) {
            log.trace("flux stream item: {}", signal.get());
            if (needCache(signal)) {
                AssistantMessage output = signal.get().getResult().getOutput();
                responseMessageBuffer.add(output.getText());
                log.trace("cached result: [{}]", responseMessageBuffer);
            }
        }
        // 回复完成时将回复的消息添加到聊天记录充当下一次的prompt
        else if (signal.isOnComplete()) {
            String fullResponse = responseMessageBuffer.toString();
            chatHistoryList.add(new AssistantMessage(fullResponse));
            log.trace("start log all prompt:");
            chatHistoryList.forEach(item -> log.trace(item.toString()));
            log.trace("log prompt done!");
            // 将聊天内容记录到数据库
            saveAssistantMessage(fullResponse);
            // 清空缓冲区
            responseMessageBuffer.setEmptyValue("");
            log.trace("flux records add completed!");
        } else if (signal.isOnError()) {
            log.warn("flux stream Error: {}", ObjectUtil
                    .defaultIfNull(signal.getThrowable(), Throwable::getMessage, "no error message"));
        }
    }

    /**
     * 校验信号是否存在需要缓存的内容, 具备文本消息, 且文本消息不为null
     * <p>不可见字符也将被当作需要缓存内容</p>
     * @param signal 响应信号
     * @return 为true时需要缓存
     */
    @SuppressWarnings({"ConstantConditions", "null"})
    private boolean needCache(Signal<ChatResponse> signal) {
        if (hasOutput(signal)) {
            return !CharSequenceUtil.isEmpty(signal.get().getResult().getOutput().getText());
        }
        return false;
    }

    private boolean hasOutput(Signal<ChatResponse> signal) {
        return Optional.ofNullable(signal)
                .map(Signal::get)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .isPresent();
    }

    /**
     * 判断响应助理响应内容有内容并具有 AssistantMessage 对象
     * @param chatResponse 助理响应结果
     * @return 有内容时为true
     */
    private boolean hasOutput(ChatResponse chatResponse) {
        if (notEmptyField(chatResponse)) {
            return false;
        }
        return Optional.of(chatResponse)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .isPresent();
    }

    private boolean hasOutput(Generation response) {
        if (notEmptyField(response)) {
            return false;
        }
        return Optional.of(response)
                .map(Generation::getOutput)
                .isPresent();
    }

    @SuppressWarnings("rawtypes")
    private boolean notEmptyField(ModelResponse res) {
        if (!res.getMetadata().containsKey(EMPTY_KEY)) {
            return true;
        }
        return Boolean.parseBoolean(res.getMetadata().get(EMPTY_KEY));
    }
    private boolean notEmptyField(Generation res) {
        if (!res.getMetadata().containsKey(EMPTY_KEY)) {
            return true;
        }
        return Boolean.parseBoolean(res.getMetadata().get(EMPTY_KEY));
    }

    /**
     * 将chatModel的响应的消息内容缓存到prompt
     *
     * @param chatResponse chatModel的响应结果
     */
    private void addToPrompt(ChatResponse chatResponse) {
        if (hasOutput(chatResponse)) {
            chatHistoryList.add(this.mapToMessage(chatResponse));
        }
    }

    /**
     * 将chatModel的响应的消息转为AssistantMessage
     *
     * @param chatResponse chatModel的响应结果
     * @return AssistantMessage 助手类型消息
     */
    private Message mapToMessage(ChatResponse chatResponse) {
        return chatResponse.getResult().getOutput();
    }
}
