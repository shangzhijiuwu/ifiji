package me.iszhenyu.ifiji.web.config.security;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.BeanInitializationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaoz on 2017/5/9.
 */
public class FijiFilterFactoryBean extends ShiroFilterFactoryBean {
	private Set<String> ignoreExt;

	FijiFilterFactoryBean() {
		super();
		ignoreExt = new HashSet<>();
		ignoreExt.add(".jpg");
		ignoreExt.add(".png");
		ignoreExt.add(".gif");
		ignoreExt.add(".js");
		ignoreExt.add(".css");
	}

	@Override
	protected AbstractShiroFilter createInstance() throws Exception {
		SecurityManager securityManager = getSecurityManager();
		if (securityManager == null) {
			throw new BeanInitializationException("SecurityManager property must be set");
		}

		if (!(securityManager instanceof WebSecurityManager)) {
			throw new BeanInitializationException("The security manager done not implement the WebSecurityManager interface");
		}

		FilterChainManager manager = createFilterChainManager();
		PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(manager);
		return new FijiFilter((WebSecurityManager) securityManager, chainResolver);
	}

	private final class FijiFilter extends AbstractShiroFilter {

		FijiFilter(WebSecurityManager webSecurityManager, FilterChainResolver filterChainResolver) {
			super();
			if (webSecurityManager == null) {
				throw new IllegalArgumentException("WebSecurityManager property can not be null");
			}
			setSecurityManager(webSecurityManager);
			if (filterChainResolver != null) {
				setFilterChainResolver(filterChainResolver);
			}
		}

		@Override
		protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String str = request.getRequestURI().toLowerCase();
			boolean flag = true;
			int idx;
			if ((idx = str.indexOf(".")) > 0) {
				str = str.substring(idx);
				if (ignoreExt.contains(str.toLowerCase())) {
					flag = false;
				}
			}

			if (flag) {
				super.doFilterInternal(servletRequest, servletResponse, chain);
			} else {
				chain.doFilter(servletRequest, servletResponse);
			}
		}
	}
}
