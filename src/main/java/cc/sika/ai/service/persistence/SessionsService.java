package cc.sika.ai.service.persistence;

import cc.sika.ai.entity.po.Sessions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 针对表【SESSIONS(会话记录表)】的数据库操作Service
 *
 * @author 小吴来哩
 * @since 2025-03-08 22:44:56
 */
public interface SessionsService extends IService<Sessions> {
	
	/**
	 * 创建一个新的会话, 会话名称自动推导, 会话类型默认1, 同时记录会话人id与创建时间
	 * @return 会话id
	 */
	String createSession();

	/**
	 * 创建一个新的会话, 会话名称自动推导, 会话类型默认1, 同时记录会话人id与创建时间
	 * @return 会话id
	 */
	String createSession(String sessionId);
}
