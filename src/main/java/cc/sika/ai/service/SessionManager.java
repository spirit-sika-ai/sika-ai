package cc.sika.ai.service;

import cn.hutool.core.lang.Snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护一个会话与聊天上下文的结构
 * todo: 需要有能够处理冷链接关闭功能
 * @author 小吴来哩
 * @since 2025-04
 */
public final class SessionManager {
    private static final Map<String, ContextManager> SESSION_MAP = new ConcurrentHashMap<>();
    private static final SessionManager INSTANCE = new SessionManager();
    public static final Snowflake ID_GENERATOR = new Snowflake(1, 2);
//    @Resource
//    private SessionsService sessionsService;

    private SessionManager() {
        // singleton
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public ContextManager getContextManager(String sessionId) {
        return SESSION_MAP.get(sessionId);
    }

    public void addContextManager(String sessionId, ContextManager contextManager) {
        // TODO: 持久化会话
        SESSION_MAP.put(sessionId, contextManager);
    }

    public void removeContextManager(String sessionId) {
        // TODO: 是否移除持久化会话数据?
        SESSION_MAP.remove(sessionId);
    }

    public long getSnowflakeId() {
        return ID_GENERATOR.nextId();
    }
}
