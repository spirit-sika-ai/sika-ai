package cc.sika.ai;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public class JasyptEncryptionTest {
    @Test
    public void generateEncryptedApiKey() {
        // 创建加密器实例
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // 加密算法
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        // 设置加密密钥, 不是api-key, 可以理解为加密算法的盐
        encryptor.setPassword("替换为你的密钥");
        // 需要加密的明文, 你的 api-key
        String apiKey = "sk-XXX";

        // 生成加密后的密文
        String encryptedApiKey = encryptor.encrypt(apiKey);
        System.out.println("加密后的 api-key:" + encryptedApiKey);
    }

}
