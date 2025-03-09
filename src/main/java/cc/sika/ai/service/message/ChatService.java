package cc.sika.ai.service.message;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public interface ChatService {

    /**
     * 接受字符串消息, 并在生成完成后一次性返回, 会有更长时间的等待, 但是不需要流式处理响应结果
     *
     * @param message 客户端的消息内容, 每个聊天下的消息会被追加到prompt列表
     * @return AI生成响应结果
     */
    ChatResponse messageFullReply(String message);

    /**
     * 接受字符串消息, 并在每次接受到生成的内容时响应, 不能获取到完整的响应文本, 但是可以更快的给予回复
     * <p>在生成完成后会将内容放入到当前会话的prompt中影响后续的内容生成</p>
     * @param message 客户端的消息内容, 每个聊天下的消息会被追加到prompt列表
     * @return AI生成响应结果的部分
     */
    Flux<ChatResponse> messageStreamReply(String message);
}
