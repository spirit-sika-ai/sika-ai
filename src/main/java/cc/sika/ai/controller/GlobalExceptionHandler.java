package cc.sika.ai.controller;

import cc.sika.ai.entity.vo.R;
import cc.sika.ai.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     * @param ex 运行时异常
     * @return 错误信息
     */
    @ExceptionHandler({RuntimeException.class, UserException.class})
    public R<?> exceptionHandler(RuntimeException ex){
        log.error("异常信息：{}", ex.getMessage());
        return R.fail(ex.getMessage());
    }
}
