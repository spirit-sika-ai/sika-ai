package cc.sika.ai.controller;

import cc.sika.ai.entity.vo.R;
import cc.sika.ai.exception.UserException;
import cn.hutool.core.text.CharSequenceUtil;
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
    /**
     * 捕获业务异常
     * @param ex 运行时异常
     * @return 错误信息
     */
    @ExceptionHandler
    @SuppressWarnings("rawtypes")
    public R exceptionHandler(RuntimeException ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        log.error("请求发生错误, 异常信息：{}", ex.getMessage());
        log.error("URL: {}", uri);
        if (CharSequenceUtil.isNotBlank(query)) {
            log.error("查询参数: {}", query);
        }
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            String contentAsString = requestWrapper.getContentAsString();
	        if (CharSequenceUtil.isNotBlank(contentAsString)) {
                log.error("请求体: {}", contentAsString);
	        }
        }
        log.error("错误栈: ", ex);
        if (ex instanceof UserException userException) {
            return R.fail(userException.getCode(), ex.getMessage(), null);
        }
        return R.fail(ex.getMessage());
    }
}
