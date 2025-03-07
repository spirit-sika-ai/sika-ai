package cc.sika.ai.controller;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestController
@RequestMapping("chat")
public class ChatController {

    @Resource
    private ChatModel deepseekModel;

    private final List<Message> chatHistoryList = new ArrayList<>();

    @PostConstruct
    public void init() {
        chatHistoryList.add(new SystemMessage("You are a helpful assistant."));
    }

    @GetMapping("message")
    public ChatResponse test(String message) {
        chatHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistoryList);
        ChatResponse chatResponse = deepseekModel.call(prompt);
        if (chatResponse.getResult() != null && chatResponse.getResult().getOutput() != null) {
            chatHistoryList.add(chatResponse.getResult().getOutput());
        }
        return chatResponse;
    }

    @GetMapping(value = "stream-message", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> streamMessage(String message) {
        chatHistoryList.add(new UserMessage(message));
        Prompt prompt = new Prompt(chatHistoryList);
        return deepseekModel.stream(prompt);
    }
}
