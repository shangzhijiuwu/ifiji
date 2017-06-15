package me.iszhenyu.ifiji.web.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

/**
 * @author zhen.yu
 * @since 2017/6/15
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        return new SimpleAuthorizationInfo(((UserDefault) principals.getPrimaryPrincipal()).getRoles());
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) token;
        String jwtTokenString = (String) jwtAuthenticationToken.getCredentials();
        if (StringUtils.isEmpty(jwtTokenString)) {
            throw new AuthenticationException("jwt is empty!");
        }

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtProperties.getKey()).parseClaimsJws(jwtTokenString).getBody();
        } catch (Exception e) {
            throw new AuthenticationException("jwt parse error!");
        }
        String principal = (String) SecurityUtils.getSubject().getPrincipal();
        if (isTokenExpired(claims.getExpiration()) || claims.getSubject().equals(principal)) {
            throw new AuthenticationException("jwt expired or info error!");
        }

        return new SimpleAuthenticationInfo(principal, jwtTokenString, getName());
    }

    private boolean isTokenExpired(Date expiredAt) {
        Date now = Calendar.getInstance().getTime();
        return expiredAt.before(now);
    }

}
