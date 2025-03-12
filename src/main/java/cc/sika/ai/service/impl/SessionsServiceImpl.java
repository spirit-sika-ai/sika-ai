package cc.sika.ai.service.impl;

import cc.sika.ai.constant.SessionsConstant;
import cc.sika.ai.entity.po.Sessions;
import cc.sika.ai.exception.UserException;
import cc.sika.ai.mapper.SessionsMapper;
import cc.sika.ai.service.SessionsService;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cc.sika.ai.constant.SessionsConstant.NEW_SESSION_NAME;
import static cn.dev33.satoken.exception.NotLoginException.DEFAULT_MESSAGE;

/**
 * @author 小吴来哩
 * @since 2025-03-08 22:44:56
 */
@Service
public class SessionsServiceImpl extends ServiceImpl<SessionsMapper, Sessions> implements SessionsService {
	@Override
	public String createSession() {
		if (!StpUtil.isLogin()) {
			throw new UserException(HttpStatus.HTTP_UNAUTHORIZED, DEFAULT_MESSAGE);
		}
		String currentId = StpUtil.getLoginIdAsString();
		Sessions sessions = buildSession(currentId);
		save(sessions);
		return sessions.getId();
	}
	
	private Sessions buildSession(String userId) {
		return Sessions.builder()
			.id(IdUtil.fastUUID())
			.sessionName(NEW_SESSION_NAME)
			.sessionType(SessionsConstant.SessionType.SINGLE)
			.staffId(userId)
			.status(SessionsConstant.SessionStatus.RIGHT)
			.createTime(LocalDateTime.now())
			.updateTime(LocalDateTime.now())
			.updateBy(userId).build();
	}
	
	/**
	 * 通过ai会话id获取新名称
	 * @param sessionId
	 * @param newSessionName
	 */
	public void updateSessionName(String sessionId, String newSessionName) {}
}




