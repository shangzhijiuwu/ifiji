package me.iszhenyu.ifiji.web.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
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
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
class StatelessFilter_dep extends AccessControlFilter {

    private Logger logger = LoggerFactory.getLogger(StatelessFilter_dep.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (isLoginRequest(request, response)) {
            return true;
        }

        String jwtToken = parseToken(request);
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        Claims claims = Jwts.parser().setSigningKey(jwtProperties.getKey()).parseClaimsJws(jwtToken).getBody();
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        return !isTokenExpired(claims.getExpiration()) && claims.getSubject().equals(principal);
    }

    private boolean isTokenExpired(Date expiredAt) {
        Date now = Calendar.getInstance().getTime();
        return expiredAt.before(now);
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
        sendChallenge(response);
        return false;
    }

    private void sendChallenge(ServletResponse response) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication required: sending 401 Authentication challenge response.");
        }

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("unauthorized");
    }

}
