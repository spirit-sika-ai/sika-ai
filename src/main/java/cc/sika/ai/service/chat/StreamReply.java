package cc.sika.ai.service.chat;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@FunctionalInterface
public interface StreamReply {

    /**
     * 接受字符串消息, 并在生成内容时积极响应, 不能获取到完整的响应文本, 但是可以更快的给予回复
     * <p>在生成完成后会将内容放入到当前会话的prompt中影响后续的内容生成</p>
     * 要获取所有生成的内容需要采用 <b> 缓存+Flux相关API </b> 处理, 如: doOneEach
     * <pre>{@code
     * @Resource
     * private OpenAiChatModel deepseekModel;
     * private final StringJoiner responseMessageBuffer = new StringJoiner("");
     * void example(Prompt prompt) {
     *     chatModel.stream(prompt).doOnEach(this::handleSignal)
     * }
     * void handleSignal(Signal<ChatResponse> signal) {
     *     if (signal.isOnNext()) {
     *         AssistantMessage partialResponse = signal.get().getResult().getOutput();
     *         responseMessageBuffer.add(partialResponse.getText());
     *     }
     *     else if (signal.isOnComplete()) {
     *         String fullResponse = responseMessageBuffer.toString();
     *         log.info("all response is {}", fullResponse);
     *     }
     *     else if (signal.isOnError()) {
     *         // process error
     *     }
     * }
     *
     * }</pre>
     * @param message 客户端的消息内容, 聊天下的每个消息会被追加到prompt列表(包括AI响应的内容)
     * @return AI生成响应结果的部分
     */
    Flux<ChatResponse> streamReply(String message);
}
