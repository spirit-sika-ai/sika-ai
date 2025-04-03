package cc.sika.ai.service;

import cc.sika.ai.eunm.LimitStrategyEnum;
import cc.sika.ai.executor.LimitExecutor;
import cc.sika.ai.service.chat.WindowLimit;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 上下文管理器, 将区别每个会话进行上下文控制
 * <p>核心任务:</p>
 * <ol>
 *     <li>确保上下文被正确持久化, 如果消息需要持久化</li>
 *     <li>限制上下文长度在合理范围</li>
 * </ol>
 * @author 小吴来哩
 * @since 2025-04
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContextManager implements WindowLimit {

    /**
     * 将所有的上下文向上抽取以便统一维护
     * <p>ChatModel#stream()会采用自定义任务线程task-*执行任务, 而服务器本身会使用netty负责http请求响应</p>
     * 消息缓存列表存在竞态 需要保持prompt缓存为线程安全结构
     */
    protected final List<Message> chatContext = new CopyOnWriteArrayList<>();

    @Value("${spring.ai.openai.chat.options.max-tokens}")
    private Integer maxToken;

    /**
     * 确保上下文不溢出后将新消息添加到上下文, 再返回一整个上下文对象
     * @param message 新消息
     * @return 当前会话的所有上下文内容
     */
    public List<Message> buildPrompts(String message) {
        limitWindow(message);
        chatContext.add(new UserMessage(message));
        return chatContext;
    }

    /**
     * 获取当前会话上下文内容, 不做窗口溢出处理
     * @return 所有聊天内容
     */
    public List<Message> buildPrompts() {
        return chatContext;
    }

    public void addMessage(Message message) {
        limitWindow(message.getText());
        chatContext.add(message);
    }

    @Override
    public void limitWindow(String newMessage, LimitStrategyEnum strategy) {
        // TODO: 如果策略删除后的消息仍然超过最大值时?
        while (isOverflow(newMessage)) {
            switch (strategy) {
                case OLDEST -> LimitExecutor.limitOldest(chatContext, newMessage);
                case NON_SYSTEM -> LimitExecutor.limitNonSystem(chatContext, newMessage);
                case NON_TOOL -> LimitExecutor.limitNonTool(chatContext, newMessage);
                default -> LimitExecutor.limitNonSystemTool(chatContext, newMessage);
            }
        }
    }

    @Override
    public boolean isOverflow(String newMessage) {
        int limit = ObjectUtil.defaultIfNull(maxToken, Integer::intValue, 1024 * 64);
        return limit < calculateTokens(newMessage);
    }

    @Override
    public Integer calculateTokens(String newMessage) {
        int currentTotalTokens = chatContext.stream()
                .mapToInt(item -> calculateMessageTokens(item.getText()))
                .sum();
        Integer newMessageTokens = calculateMessageTokens(newMessage);
        return currentTotalTokens + newMessageTokens;
    }

    /**
     * 计算消息的token用量
     * <p>分词后根据中英文乘负载因子得出token长度</p>
     * @param newMessage 消息内容
     * @return -
     */
    public Integer calculateMessageTokens(String newMessage) {
        TokenizerEngine engine = TokenizerUtil.createEngine();
        Result parse = engine.parse(newMessage);
        AtomicDouble tokens = new AtomicDouble(0D);
        parse.forEach(word -> {
            String wordStr = word.getText();
            if (isChinese(wordStr)) {
                tokens.addAndGet(wordStr.length() * CHINESE_TOKEN_LOAD_FACTOR);
            } else {
                tokens.addAndGet(wordStr.length() * ENGLISH_TOKEN_LOAD_FACTOR);
            }
        });
        return (int) tokens.get();
    }

    private boolean isChinese(String word) {
        if (CharSequenceUtil.isBlank(word)) {
            return false;
        }
        char firstCharacter = word.charAt(0);
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(firstCharacter);
        // 只包含汉字，排除标点
        return unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D ||
                unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS;
    }
}
