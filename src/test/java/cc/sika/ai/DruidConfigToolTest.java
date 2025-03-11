package cc.sika.ai;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.jupiter.api.Test;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public class DruidConfigToolTest {
	@Test
	void testGeneratePassword() throws Exception {
		ConfigTools.main(new String[]{"123456"});
	}
}
