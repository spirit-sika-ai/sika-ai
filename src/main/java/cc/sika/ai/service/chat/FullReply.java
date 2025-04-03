package cc.sika.ai.service.chat;

import org.springframework.ai.chat.model.ChatResponse;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@FunctionalInterface
public interface FullReply {
    /**
     * 接受字符串消息, 并在生成完成后一次性返回, 会有更长时间的等待, 但是不需要流式处理响应结果
     *
     * @param message 客户端的消息内容, 聊天下的每个消息会被追加到prompt列表(包括AI响应的内容)
     * @return AI生成响应结果
     */
    ChatResponse fullReply(String message);
}
