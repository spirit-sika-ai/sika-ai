package cc.sika.ai.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static cc.sika.ai.constant.ResponseConstant.*;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private Integer code;
    private String message;
    private T result;

    public static <T> R<T> success(T result) {
        return success(SUCCESS_MESSAGE, result);
    }

    public static <T> R<T> fail(String message) {
        return fail(message, null);
    }

    public static <T> R<T> success(String message, T result) {
        return success(SUCCESS_CODE, message, result);
    }

    public static <T> R<T> success(Integer code, String message, T result) {
        return new R<>(code, message, result);
    }

    public static <T> R<T> fail(String message, T result) {
        return fail(SERVER_ERROR_CODE, message, result);
    }

    public static <T> R<T> fail(Integer code, String message, T result) {
        return new R<>(code, message, result);
    }
}
