package cc.sika.ai.service.chat;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * 提供可回溯上下文的聊天功能
 * @author 小吴来哩
 * @since 2025-04
 */
public interface SessionChat extends Reply {
    /**
     * <p>提供可回溯上下文聊天, 需要传递会话id, 会有较长时间等待, 并在回答生成后一次性响应</p>
     * 当无会话id时将视为新会话, 并自动创建新的会话id
     * @param message 客户端的消息内容, 每个聊天下的消息会被追加到prompt列表
     * @param currentSession 当前会话id, 允许为空, 为空时视为新会话, 创建并绑定新的会话id
     * @return AI生成响应结果
     */
    ChatResponse fullReply(String message, String currentSession);


    /**
     * <p>提供可回溯上下文聊天, 需要传递会话id, 流程消息接口, 在每生成一点内容时立即返回</p>
     * 当无会话id时将视为新会话, 并自动创建新的会话id
     * @param message 客户端的消息内容, 每个聊天下的消息会被追加到prompt列表
     * @param currentSession 当前会话id, 允许为空, 为空时视为新会话, 创建并绑定新的会话id
     * @return AI生成响应结果的部分
     */
    Flux<ChatResponse> streamReply(String message, String currentSession);
}
