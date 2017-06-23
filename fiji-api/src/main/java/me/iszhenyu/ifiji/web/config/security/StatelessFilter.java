package me.iszhenyu.ifiji.web.config.security;

import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
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
    protected JwtAuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String token = parseToken(request);
        return new JwtAuthenticationToken(token);
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

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            return true;
        }
        boolean authc = this.executeJwtAuthentication(request, response);
        if (!authc) {
            sendChallenge(response);
        }
        return authc;
    }

    private boolean executeJwtAuthentication(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            subject.login(token);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    private void sendChallenge(ServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Authentication required: sending 401 Authentication challenge response.");
        }

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("unauthorized");
    }

}
