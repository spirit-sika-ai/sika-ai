package cc.sika.ai.service;

import cc.sika.ai.exception.SessionError;
import cc.sika.ai.service.persistence.SessionsService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 维护一个会话与聊天上下文的结构
 *
 * @author 小吴来哩
 * @since 2025-04
 */
@Component
@Slf4j
public final class SessionManager {
    private static final Map<String, ContextManager> SESSION_MAP = new ConcurrentHashMap<>();
    public static final Snowflake ID_GENERATOR = new Snowflake(1, 2);
    // 清理冷会话
    private final ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor(new CustomizableThreadFactory("session-clearer-"));
    // 会话超时时间
    private static final long SESSION_TIMEOUT_MINUTES = 15;
    private final SessionsService sessionsService;

    public SessionManager(SessionsService sessionsService) {
        this.sessionsService = sessionsService;
        // 每十分钟检查一次超时会话
        scheduled.scheduleAtFixedRate(this::removeExpiredSessions, 10, 10, TimeUnit.MINUTES);
    }
    public synchronized ContextManager getContextManager(String sessionId) {
        ContextManager contextManager = SESSION_MAP.get(sessionId);
        if (ObjectUtil.isNull(contextManager)) {
            throw new SessionError();
        }
        contextManager.updateLastActivityTime();
        return contextManager;
    }

    public synchronized void addContextManager(String sessionId, ContextManager contextManager) {
        if (StpUtil.isLogin()) {
            sessionsService.createSession(sessionId);
        }
        SESSION_MAP.put(sessionId, contextManager);
    }

    public void removeContextManager(String sessionId) {
        SESSION_MAP.remove(sessionId);
    }

    public long getSnowflakeId() {
        return ID_GENERATOR.nextId();
    }

    private void removeExpiredSessions() {
        log.info("scan frozen context");
        LocalDateTime now = LocalDateTime.now();
        SESSION_MAP.entrySet().removeIf(entry -> {
            ContextManager contextManager = entry.getValue();
            return now.minusMinutes(SESSION_TIMEOUT_MINUTES).isAfter(contextManager.getLastActivityTime());
        });
    }

    public void shutdown() {
        scheduled.shutdown();
    }
}
