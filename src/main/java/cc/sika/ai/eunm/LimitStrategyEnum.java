package cc.sika.ai.eunm;

/**
 * 上下文溢出后的限制策略
 * <ul>
 *     <li>删除最早消息</li>
 *     <li>删除最早非系统消息</li>
 *     <li>删除最早非工具消息</li>
 *     <li>删除最早非系统与工具消息</li>
 * </ul>
 * @author 小吴来哩
 * @since 2025-03
 */
public enum LimitStrategyEnum {
    /**
     * 删除最早消息
     */
    OLDEST,
    /**
     * 删除最早非系统消息
     */
    NON_SYSTEM,
    /**
     * 删除最早非工具消息
     */
    NON_TOOL,
    /**
     * 删除最早非系统与工具消息
     */
    NON_SYSTEM_TOOL,
    /**
     * 混合策略
     */
    COMPOSITE;
}
