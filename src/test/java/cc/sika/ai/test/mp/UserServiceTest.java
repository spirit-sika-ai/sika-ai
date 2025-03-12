package cc.sika.ai.test.mp;

import cc.sika.ai.JasyptExtension;
import cc.sika.ai.entity.po.User;
import cc.sika.ai.mapper.UserMapper;
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
class UserServiceTest {
	@Resource
	private UserMapper userMapper;
	private String id;
	
	@PostConstruct
	public void init() {
		id = IdUtil.simpleUUID();
	}
	
	@Test
	void testUser() {
		User build = User.builder()
			.id(id)
			.nickname("nickname-test")
			.username("username-test")
			.password("password-test")
			.createTime(LocalDateTime.now())
			.createBy(id)
			.updateTime(LocalDateTime.now())
			.updateBy(id)
			.build();
		Assertions.assertEquals(1, userMapper.insert(build));
		Assertions.assertTrue(userMapper.selectCount(null) > 0);
		LambdaUpdateWrapper<User> userUpdate = new LambdaUpdateWrapper<>();
		userUpdate.eq(User::getId, build.getId());
		build.setNickname("nickname-test-updated");
		build.setUsername("username-test-updated");
		build.setPassword("password-test-updated");
		Assertions.assertEquals(1, userMapper.update(build, userUpdate));
		Assertions.assertEquals(1, userMapper.deleteById(id));
	}
}
