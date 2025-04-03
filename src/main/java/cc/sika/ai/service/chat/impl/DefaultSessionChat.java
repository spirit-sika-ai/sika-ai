package cc.sika.ai.service.chat.impl;

import cc.sika.ai.service.ContextManager;
import cc.sika.ai.service.SessionManager;
import cc.sika.ai.service.chat.SessionChat;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 接收消息内容并根据是否存在会话id决定创建新会话还是沿用旧的会话
 *
 * @author 小吴来哩
 * @since 2025-04
 */
@Service
@Slf4j
public class DefaultSessionChat implements SessionChat {

    private static final SessionManager sessionManager = SessionManager.getInstance();
    // 缓存每次流式响应的结果
    private final StringJoiner responseMessageBuffer = new StringJoiner("");
    @Resource
    private ChatModel deepseekModel;
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 生成新的会话并返回会话ID
     * @return 会话id字符串
     */
    private String buildNewSession() {
        String newSessionId = String.valueOf(sessionManager.getSnowflakeId());
        sessionManager.addContextManager(newSessionId, applicationContext.getBean(ContextManager.class));
        return newSessionId;
    }
    @Override
    public ChatResponse fullReply(String message) {
        return fullReply(message, buildNewSession());
    }

    @Override
    public Flux<ChatResponse> streamReply(String message) {
        if (CharSequenceUtil.isBlank(message)) {
            return Flux.empty();
        }
        return streamReply(message, buildNewSession());
    }

    @Override
    public ChatResponse fullReply(String message, String currentSession) {
        if (CharSequenceUtil.isBlank(message)) {
            return ChatResponse.builder().build();
        }
        if (CharSequenceUtil.isBlank(currentSession)) {
            currentSession = buildNewSession();
        }
        // 获取当前会话的上下文, 限制上下文长度添加用户消息后添加到prompt中
        ContextManager contextManager = sessionManager.getContextManager(currentSession);
        // TODO: 会话获取不到处理
        List<Message> messageContent = contextManager.buildPrompts(message);
        Prompt prompt = new Prompt(messageContent);
        ChatResponse response = deepseekModel.call(prompt);
        // 将AI响应的消息追加到会话上下文同时限制上下文长度
        contextManager.addMessage(mapResp2Message(response));
        return response;
    }

    @Override
    public Flux<ChatResponse> streamReply(String message, String currentSession) {
        if (CharSequenceUtil.isBlank(message)) {
            return Flux.empty();
        }
        if (CharSequenceUtil.isBlank(currentSession)) {
            currentSession = buildNewSession();
        }
        // 获取当前会话的上下文, 限制上下文长度添加用户消息后添加到prompt中
        ContextManager contextManager = sessionManager.getContextManager(currentSession);
        // TODO: 会话获取不到处理
        List<Message> messages = contextManager.buildPrompts(message);
        Prompt prompt = new Prompt(messages);
        // 构建流式响应会话支持SSE并返回, 同时处理每次响应的内容
        return deepseekModel.stream(prompt)
                .doOnEach((signal -> this.handleSignal(signal, contextManager)));
    }


    /**
     * 处理流式响应的每个信号, 将正常信号追加到消息缓冲区, 在所有信号处理完毕后记录消息到数据库
     *
     * @param signal         -
     * @param contextManager 当前会话的聊天上下文管理器
     */
    @SuppressWarnings("ConstantConditions")
    public void handleSignal(Signal<ChatResponse> signal, ContextManager contextManager) {
        // 中间结果, 可能为: 空|一个汉字|一个字母|一个单词|一个符号
        if (signal.isOnNext()) {
            log.trace("flux stream item: {}", signal.get());
            if (needCache(signal)) {
                AssistantMessage output = signal.get().getResult().getOutput();
                responseMessageBuffer.add(output.getText());
                log.trace("cached result: [{}]", responseMessageBuffer);
            }
        }
        // 消息回复完成时将回复的消息添加到聊天记录充当下一次的prompt
        else if (signal.isOnComplete()) {
            String fullResponse = responseMessageBuffer.toString();
            contextManager.addMessage(new AssistantMessage(fullResponse));
            responseMessageBuffer.setEmptyValue("");
            log.trace("flux records add completed!");
        }
        // 错误处理
        else {
            log.warn("flux stream Error: {}", ObjectUtil
                    .defaultIfNull(signal.getThrowable(), Throwable::getMessage, "no error message"));
        }
    }

    /**
     * 校验信号是否存在需要缓存的内容, 具备文本消息, 且文本消息不为null
     * <p>不可见字符也将被当作需要缓存内容</p>
     *
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
     * 将AI响应消息转为Message对象
     *
     * @param response ai调用响应对象
     * @return 消息内容, 将作为聊天记录添加到prompt
     */
    private Message mapResp2Message(ChatResponse response) {
        return response.getResult().getOutput();
    }
}
