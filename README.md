# 数据库密码
使用 druid 密文生成工具生成密文以及公钥, 分别替换:
- spring.datasource.druid.password
  > 密文(password)
- spring.datasource.druid.password.connect-properties.config.decrypt.key
  > 公钥(publicKey)

# ai-key
使用 jasypt 加密 apikey, 将 spring.ai.api-key 的内容替换为生成后的密文, 需要保留ENC()
> api-key密文可以使用下面下测试包中的cc.sika.ai.JasyptEncryptionTest#generateEncryptedApiKey生成

> 仅替换ENC()中的内容, 如ENC(AAA)替换为ENC(BBB)

并在启动时添加参数注入密钥
> --jasypt.encryptor.password=${加密时使用的密钥}
> 
> 注意密钥非API-Key内容

或者是使用虚拟机参数:
> -Djasypt.encryptor.password=${加密时使用的密钥}

可以使用cc.sika.ai.SikaAiApplicationTests#testApiKeyIsCorrect测试你的配置与启动参数是否正确

# 环节
PostgreSQL
JDK21

# 结构设计
## 鉴权部分

## 会话部分
功能接口:
- 全量回复接口(FullReply)
- 流式回复接口(StreamReply)
- 糅合全量与流程回复接口, 同时提供全量回复与流式回复(Reply)

因为回复接口要发送消息, 就可能导致上下文溢出, 需要有上下文窗口限制, 所以需要一个上下文窗口管理器(WindowLimit)

聊天以会话为维度, 存储上下文(历史记录), 需要有一个SessionChat接口, 会话id管理功能由SessionManager实现, SessionManager保存一个Map
记录会话id与会话上下文(ContextManager), SessionManager是为了后续服务器主动控制会话预抽取的层级.

上下文在ContextManager类中, 并且实现了 WindowLimit 接口, 能够控制上下文窗口大小与token大小计算

- SessionManager只关注会话
- ContextManager只关注上下文

# 测试接口
目前没有启用Sa-Token的请求拦截功能, 可以直接使用API接口测试工具测试.

如果要测试用户相关功能需要调用 `用户信息加密接口` 先对账号密码加密, 使用加密后的用户名密码调用 `登录接口`.

如果采用的是 `ApiFox` 登录完成后调用用户信息相关的接口即可, `ApiFox` 会自动将 `Authorization` 添加到请求头中.

