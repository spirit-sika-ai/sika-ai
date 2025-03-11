package cc.sika.ai.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Component
public final class RSAUtil {
    private static final RSA rsa = new RSA();
    @Getter
    private static final String PUBLIC_KEY = Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded());
    @Getter
    private static final String PRIVATE_KEY = Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded());

    public static String decrypt(String encryptedText) {
        return rsa.decryptStr(encryptedText, KeyType.PrivateKey);
    }
    
    public static String encrypt(String plainText) {
        return rsa.encryptHex(plainText, KeyType.PublicKey);
    }

    private RSAUtil() {
        // do nothing
    }
}
