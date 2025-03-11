package cc.sika.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ExtendWith(JasyptExtension.class)
class SikaAiApplicationTests {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    @Value("${jasypt.encryptor.password}")
    private String password;

    /**
     * 测试生成的密钥与配置是否有问题
     */
//    @Test
//    void testApiKeyIsCorrect() {
//        // 预期值必须和生成密文时原来的明文一致
//        String expectedApiKey = "sk-XXX(替换为你的api-key)";
//        String expectedPassword = "替换为你加密时使用的密钥";
//        System.out.println("password = " + password);
//        System.out.println("apiKey = " + apiKey);
//        // 如果不一致你可以在测试结果看到下面的两个消息输出
//        assertEquals("解密后的 password 不正确", expectedPassword, password);
//        assertEquals("解密后的 api-key 不正确", expectedApiKey, apiKey);
//    }

}
