package cc.sika.ai.test.mp;

import cc.sika.ai.JasyptExtension;
import cc.sika.ai.service.persistence.SessionsService;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 小吴来哩
 * @since 2025-04
 */
@SpringBootTest
@ExtendWith(JasyptExtension.class)
class SessionsMpTest {
    @Resource
    private SessionsService sessionsService;

    @BeforeEach
    void login() {
        StpUtil.login("a32adb56d5cc434c82da3f8c544b06a1");
    }

    @Test
    void testCreateSession() {
        String sessionId = sessionsService.createSession();
        Assertions.assertTrue(sessionId.length() > 1);
    }
}
