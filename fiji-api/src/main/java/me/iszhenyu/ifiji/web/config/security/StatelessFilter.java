package me.iszhenyu.ifiji.web.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
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
class StatelessFilter extends AccessControlFilter {

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
        return !isTokenExpired(claims.getExpiration());
    }

    private boolean isTokenExpired(Date expiredAt) {
        Date now = Calendar.getInstance().getTime();
        return expiredAt.before(now);
    }

    private String parseToken(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            return "";
        }
        String token = ((HttpServletRequest) request).getHeader(jwtProperties.getTokenName());
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals(jwtProperties.getTokenName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        onFail(response);
        return false;
    }

    private void onFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("unauthorized");
    }

}
