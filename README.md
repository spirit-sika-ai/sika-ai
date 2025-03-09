# 数据库密码
使用druid密文生成工具生成密文以及公钥, 分别替换:
- spring.datasource.druid.password
  > 密文(password)
- spring.datasource.druid.password.connect-properties.config.decrypt.key
  > 公钥(publicKey)

# ai-key
使用jasypt加密apikey, 将spring.ai.api-key的内容替换为生成后的密文, 需要保留ENC()
> api-key密文可以使用下面下测试包中的cc.sika.ai.JasyptEncryptionTest#generateEncryptedApiKey生成

> 仅替换ENC()中的内容, 如ENC(AAA)替换为ENC(BBB)

并在启动时添加参数注入密钥
> --jasypt.encryptor.password=${加密时使用的密钥}
> 
> 注意密钥非API-Key内容

或者是使用虚拟机参数:
> -Djasypt.encryptor.password=${加密时使用的密钥}

可以使用cc.sika.ai.SikaAiApplicationTests#testApiKeyIsCorrect测试你的配置与启动参数是否正确