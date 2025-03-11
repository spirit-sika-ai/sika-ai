package cc.sika.ai;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Slf4j
public class JasyptExtension implements BeforeAllCallback {
	@Override
	public void beforeAll(ExtensionContext context) {
		String password = System.getProperty("jasypt.encryptor.password");
		if (CharSequenceUtil.isBlank(password)) {
			log.warn("Jasypt encryptor password is null");
			System.setProperty("jasypt.encryptor.password", "wjc52292");
		}
	}
}
