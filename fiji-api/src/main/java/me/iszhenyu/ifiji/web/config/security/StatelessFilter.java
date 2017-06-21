package me.iszhenyu.ifiji.web.config.security;

import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhen.yu
 * @since 2017/6/15
 */
class StatelessFilter extends AuthenticatingFilter {

    private static final Logger log = LoggerFactory.getLogger(StatelessFilter.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        if (log.isTraceEnabled()) {
            log.trace("Adding login url to applied paths.");
        }
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String token = parseToken(request);
        return new JwtAuthenticationToken(token);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response))  {
            // 允许执行login
            return true;
        }
        sendChallenge(response);
        return false;
    }

    private void sendChallenge(ServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Authentication required: sending 401 Authentication challenge response.");
        }

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("unauthorized");
    }

    private String parseToken(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            return "";
        }
        String token = ((HttpServletRequest) request).getHeader(jwtProperties.getTokenHeaderName());
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(jwtProperties.getTokenCookieName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

}
