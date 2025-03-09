package cc.sika.ai.controller;

import cc.sika.ai.entity.vo.R;
import cc.sika.ai.service.message.ChatService;
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
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("message")
    public R<ChatResponse> fullReply(@RequestParam(name = "message") String message) {
        return R.success(chatService.messageFullReply(message));
    }

    @GetMapping(value = "stream-message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<R<ChatResponse>> streamMessage(String message) {
        return chatService.messageStreamReply(message).map(R::success);
    }
}
