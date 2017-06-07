package me.iszhenyu.ifiji.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xiaoz on 2017/5/9.
 */
class FormLoginFilter extends FormAuthenticationFilter {

	FormLoginFilter() {
		this.setLoginUrl("/auth/login");
		this.setSuccessUrl("/");
		this.setUsernameParam("username");
		this.setPasswordParam("password");
		this.setRememberMeParam("rememberMe");
	}

	@Override
	protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
		request.setAttribute(getFailureKeyAttribute(), ae);
	}

	@Override
	protected void redirectToLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			String contentType = request.getContentType();
			if (contentType != null && contentType.contains("application/json")) {
//				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//				response.setCharacterEncoding("UTF-8");
//				PrintWriter writer = response.getWriter();
//				writer.print("{\"error\":\"登录超时，请重新登录。\"}");
				response.setCharacterEncoding("UTF-8");
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"登录超时，请重新登录。\"}");
			} else {
				super.redirectToLogin(servletRequest, servletResponse);
			}
		} catch (Exception e) {
			super.redirectToLogin(servletRequest, servletResponse);
		}
	}
}
