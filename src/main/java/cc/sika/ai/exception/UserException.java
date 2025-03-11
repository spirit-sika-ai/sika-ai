package cc.sika.ai.exception;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
public class UserException extends RuntimeException {
    public UserException() {
        super("操作用户信息失败");
    }

    public UserException(String message) {
        super(message);
    }
}
