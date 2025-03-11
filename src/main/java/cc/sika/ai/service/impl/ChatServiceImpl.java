package cc.sika.ai.service.impl;

import cc.sika.ai.service.message.ChatService;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatModel deepseekModel;

    /**
     * ChatModel#stream()会采用自定义任务线程task-*执行任务, 而服务器本身会使用netty负责http请求响应
     * <p>消息缓存列表存在竞态 需要保持prompt缓存为线程安全结构</p>
     */
    private final List<Message> chatHistoryList = new CopyOnWriteArrayList<>();


    @PostConstruct
    public void init() {
        chatHistoryList.add(new SystemMessage("You are a helpful assistant."));
    }

    @Override
    public ChatResponse messageFullReply(String message) {
        // TODO: 记录用户消息到数据库
        chatHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistoryList);
        ChatResponse chatResponse = deepseekModel.call(prompt);
        // TODO 将聊天内容记录到数据库
        addToPrompt(chatResponse);
        return chatResponse;
    }

    @Override
    public Flux<ChatResponse> messageStreamReply(String message) {
        // 将用户发送的数据记录到当前会话
        chatHistoryList.add(new UserMessage(message));
        // TODO: 记录用户消息到数据库
        Prompt prompt = new Prompt(chatHistoryList);
        return deepseekModel.stream(prompt).doOnEach(this::handleSignal);
    }

    @SuppressWarnings("ConstantConditions")
    private void handleSignal(Signal<ChatResponse> signal) {
        final Queue<Message> responseMessageCacheBuffer = new ConcurrentLinkedQueue<>();
        // 缓存每次响应的结果
        if (signal.isOnNext()) {
            log.debug("flux stream item: {}", signal.get());
            if (needCache(signal)) {
                responseMessageCacheBuffer.add(signal.get().getResult().getOutput());
            }
        }
        // 回复完成时将回复的消息添加到聊天记录充当下一次的prompt
        else if (signal.isOnComplete()) {
            chatHistoryList.addAll(responseMessageCacheBuffer);
            // TODO 将聊天内容记录到数据库
            log.info("flux records add completed!");
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

    private boolean hasOutput(ChatResponse chatResponse) {
        return Optional.ofNullable(chatResponse)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .isPresent();
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
