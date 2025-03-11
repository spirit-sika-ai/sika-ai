package cc.sika.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

/**
 * 缓存请求体, 避免请求体被@RequestBody读取后无法再次获取
 * @author 小吴来哩
 * @since 2025-03
 */
@Component
public class RequestCachingFilter extends OncePerRequestFilter {
	@Override
	@SuppressWarnings("all")
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException {
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		filterChain.doFilter(wrappedRequest, response);
	}
}
