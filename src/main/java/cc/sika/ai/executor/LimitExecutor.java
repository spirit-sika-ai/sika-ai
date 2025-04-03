package cc.sika.ai.executor;

import cn.hutool.core.collection.CollUtil;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public class LimitExecutor {
    public static int limitOldest(List<Message> chatContext, String newMessage) {
        if (CollUtil.isEmpty(chatContext)) {
            return 0;
        }
        chatContext.removeFirst();
        return 1;
    }
    public static void limitNonSystem(List<Message> chatContext, String newMessage) {}
    public static void limitNonTool(List<Message> chatContext, String newMessage) {}
    public static void limitNonSystemTool(List<Message> chatContext, String newMessage) {

    }
}
