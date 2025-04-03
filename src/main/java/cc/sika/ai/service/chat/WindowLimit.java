package cc.sika.ai.service.chat;

import cc.sika.ai.eunm.LimitStrategyEnum;

/**
 * 窗口限制
 * @author 小吴来哩
 * @since 2025-03
 */
public interface WindowLimit {

    /**
     * TOKEN与实际内容的精度
     * <p>实际上下文内容字符串长度不一定是token的长度</p>
     * deepseek给出的token计算用量为:
     * <ul>
     *     <li>1 个英文字符 ≈ 0.3 个 token。</li>
     *     <li>1 个中文字符 ≈ 0.6 个 token。</li>
     * </ul>
     */
     Double ENGLISH_TOKEN_LOAD_FACTOR = .3;
     Double CHINESE_TOKEN_LOAD_FACTOR = .6;

    /**
     * 采用对应策略限制聊天上下文
     * @param newMessage 新消息
     * @param strategy 限制策略
     */
    void limitWindow(String newMessage, LimitStrategyEnum strategy);

    /**
     * 计算添加新消息后上下文是否溢出最大token限制
     * @param newMessage 新消息
     * @return 如果溢出返回true
     */
    boolean isOverflow(String newMessage);

    /**
     * 根据新消息与上下文估算当前token
     * @param newMessage 新消息
     * @return 估算后的token长度
     */
    Integer calculateTokens(String newMessage);

    /**
     * 计算新消息是否溢出, 溢出采用最早非系统消息限制策略限制聊天上下文
     * @param newMessage 新消息
     */
    default void limitWindow(String newMessage) {
        while (isOverflow(newMessage)) {
            limitWindow(newMessage, LimitStrategyEnum.NON_SYSTEM_TOOL);
        }
    }
}
