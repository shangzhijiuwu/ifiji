package me.iszhenyu.ifiji.web.config.security;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
class StatelessAuthenticationToken implements AuthenticationToken {

    private static final long serialVersionUID = -7007179065911805829L;

    private String username;
    private String clientDigest;
    private Map<String, String> params;

    public StatelessAuthenticationToken() {
    }

    public StatelessAuthenticationToken(String username, String clientDigest) {
        this.username = username;
        this.clientDigest = clientDigest;
    }

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return this.clientDigest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientDigest() {
        return clientDigest;
    }

    public void setClientDigest(String clientDigest) {
        this.clientDigest = clientDigest;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
