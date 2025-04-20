package cc.sika.ai.controller;

import cc.sika.ai.entity.vo.R;
import cc.sika.ai.service.chat.SessionChat;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestController
@RequestMapping("chat")
public class ChatController {

    @Resource
    private final SessionChat sessionChat;

    public ChatController(SessionChat sessionChat) {
        this.sessionChat = sessionChat;
    }

    @GetMapping("message")
    public R<ChatResponse> fullReply(@RequestParam(name = "message") String message,
                                     @RequestParam(name = "sessionId", required = false) String sessionId) {
        // http://139.159.236.189:8080/sika-ai-service/chat/message?message=给我讲个笑话
        return R.success(sessionChat.fullReply(message, sessionId));
    }

    @GetMapping(value = "stream-message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<R<ChatResponse>> streamMessage(@RequestParam(name = "message") String message,
                                               @RequestParam(name = "sessionId", required = false) String sessionId) {
        return sessionChat.streamReply(message, sessionId).map(R::success);
    }
}
