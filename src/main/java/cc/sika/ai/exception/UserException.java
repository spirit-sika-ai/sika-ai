package cc.sika.ai.exception;

import lombok.Getter;

import static cc.sika.ai.constant.ResponseConstant.SERVER_ERROR_CODE;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Getter
public class UserException extends RuntimeException {
    
    private final Integer code;
    
    public UserException() {
        super("操作用户信息失败");
        this.code = SERVER_ERROR_CODE;
    }

    @SuppressWarnings("unused")
    public UserException(String message) {
        super(message);
        this.code = SERVER_ERROR_CODE;
    }
    
    public UserException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
