package cc.sika.ai.controller;

import cc.sika.ai.entity.vo.R;
import cc.sika.ai.exception.UserException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @SuppressWarnings("rawtypes")
    public R sqlExceptionHandler(RuntimeException ex, HttpServletRequest request) {
        defaultHandleError(ex, request);
        log.error("错误栈: ", ex);
        return R.fail("服务器发生错误");
    }

    /**
     * 捕获业务异常
     * @param ex 运行时异常
     * @return 错误信息
     */
    @ExceptionHandler
    @SuppressWarnings("rawtypes")
    public R exceptionHandler(RuntimeException ex, HttpServletRequest request) {
        defaultHandleError(ex, request);
        log.error("错误栈: ", ex);
        if (ex instanceof UserException userException) {
            return R.fail(userException.getCode(), ex.getMessage(), null);
        }
        if (ex instanceof NotLoginException notLoginException) {
            String tipMsg = CharSequenceUtil.format("{}, 请重新登录", notLoginException.getMessage());
            return R.fail(HttpStatus.HTTP_BAD_REQUEST, tipMsg, null);
        }
        return R.fail(ex.getMessage());
    }

    private static void defaultHandleError(RuntimeException ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        log.error("请求发生错误, 异常信息：{}", ex.getMessage());
        log.error("URL: {}", uri);
        log.error("查询参数: {}", CharSequenceUtil.isNotBlank(query) ? query : "无查询参数");
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            String contentAsString = requestWrapper.getContentAsString();
            log.error("请求体: {}", CharSequenceUtil.isNotBlank(contentAsString) ? contentAsString : "无请求体信息");
        }
    }
}
