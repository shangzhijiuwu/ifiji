package me.iszhenyu.ifiji.web.vo;

import me.iszhenyu.ifiji.model.UserDO;

/**
 * @author zhen.yu
 * @since 2017/6/15
 */
public class LoginVO {
    private String token;
    private UserDO user;

    public LoginVO(String token, UserDO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDO getUser() {
        return user;
    }

    public void setUser(UserDO user) {
        this.user = user;
    }
}
