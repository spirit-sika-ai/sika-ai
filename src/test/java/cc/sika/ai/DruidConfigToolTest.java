package cc.sika.ai;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
class DruidConfigToolTest {
	@Test
	@SuppressWarnings("deprecation")
	void testGeneratePassword() {
		Assertions.assertAll(() -> ConfigTools.main(new String[]{"123456"}));
	}
}
