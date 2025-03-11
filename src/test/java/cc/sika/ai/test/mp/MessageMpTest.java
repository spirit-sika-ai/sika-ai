package cc.sika.ai.test.mp;

import cc.sika.ai.JasyptExtension;
import cc.sika.ai.entity.po.Message;
import cc.sika.ai.mapper.MessageMapper;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@SpringBootTest
@ExtendWith(JasyptExtension.class)
class MessageMpTest {
	@Resource
	private MessageMapper messageMapper;
	private String id;
	
	@PostConstruct
	public void init() {
		id = IdUtil.simpleUUID();
	}
	
	@Test
	void testMessage() {
		Message build = Message.builder()
			.id(id)
			.sessionId(IdUtil.simpleUUID())
			.sendId(IdUtil.simpleUUID())
			.content("content-test")
			.type(1)
			.annex(1)
			.createTime(LocalDateTime.now())
			.build();
		Assertions.assertEquals(1, messageMapper.insert(build));
		Assertions.assertTrue(messageMapper.selectCount(null) > 0);
		build.setContent("content-test-updated");
		LambdaUpdateWrapper<Message> messageUpdate = new LambdaUpdateWrapper<>();
		messageUpdate.eq(Message::getId, id);
		Assertions.assertEquals(1, messageMapper.update(build, messageUpdate));
		Assertions.assertEquals(1, messageMapper.deleteById(id));
	}
}
