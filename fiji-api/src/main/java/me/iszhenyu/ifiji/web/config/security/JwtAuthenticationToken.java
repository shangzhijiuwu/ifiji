package me.iszhenyu.ifiji.web.config.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zhen.yu
 * @since 2017/6/15
 */
public class JwtAuthenticationToken implements AuthenticationToken {

    private Object userId;
    private String token;

    private JwtAuthenticationToken(Object userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
