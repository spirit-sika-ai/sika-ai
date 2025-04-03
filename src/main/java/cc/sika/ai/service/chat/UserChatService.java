package cc.sika.ai.service.chat;

/**
 * 提供用户相关聊天功能
 * @author 小吴来哩
 * @since 2025-03
 */
public interface UserChatService extends StreamReply, FullReply {

    boolean saveUserMessage(String message, String currentSession);

    boolean saveUserMessage(String message);
}
