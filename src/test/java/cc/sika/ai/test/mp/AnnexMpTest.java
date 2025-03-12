package cc.sika.ai.test.mp;

import cc.sika.ai.JasyptExtension;
import cc.sika.ai.entity.po.Annex;
import cc.sika.ai.mapper.AnnexMapper;
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
class AnnexMpTest {
	@Resource
	private AnnexMapper annexMapper;
	private String id;
	@PostConstruct
	public void init() {
		id = IdUtil.simpleUUID();
	}
	
	@Test
	void testMessage() {
		Annex build = Annex.builder()
			.id(id)
			.displayName("display-name-test")
			.extensionType(".test")
			.fileUrl("...")
			.status(1)
			.bucketId("test-bucket-id")
			.bucketName("test-bucket-name")
			.staffId("staff-id")
			.messageId("message-id")
			.createTime(LocalDateTime.now())
			.createBy(id)
			.updateTime(LocalDateTime.now())
			.updateBy(id)
			.build();
		Assertions.assertEquals(1, annexMapper.insert(build));
		Assertions.assertTrue(annexMapper.selectCount(null) > 0);
		build.setDisplayName("display-name-test-updated");
		LambdaUpdateWrapper<Annex> annexUpdate = new LambdaUpdateWrapper<>();
		annexUpdate.eq(Annex::getId, id);
		Assertions.assertEquals(1, annexMapper.update(build, annexUpdate));
		Assertions.assertEquals(1, annexMapper.deleteById(id));
	}
}
