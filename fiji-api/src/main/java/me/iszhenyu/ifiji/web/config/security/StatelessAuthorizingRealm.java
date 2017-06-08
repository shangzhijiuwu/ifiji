package me.iszhenyu.ifiji.web.config.security;

import me.iszhenyu.ifiji.util.CipherUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author zhen.yu
 * @since 2017/6/8
 */
public class StatelessAuthorizingRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //根据用户名查找角色，请根据需求实现
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        //这里模拟admin账号才有role的权限.
        if ("admin".equals(username)) {
            authorizationInfo.addRole("admin");
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        StatelessAuthenticationToken statelessToken = (StatelessAuthenticationToken) token;
        String username = (String) statelessToken.getPrincipal();
        //根据用户名获取密钥（和客户端的一样）
        String key = getKey(username);

        //在服务器端生成客户端参数消息摘要
        String serverDigest = CipherUtils.hmacSHA256Digest(key, statelessToken.getParams());
        //然后进行客户端消息摘要和服务器端消息摘要的匹配
        return new SimpleAuthenticationInfo(
                username,
                serverDigest,
                getName()
        );
    }

    private String getKey(String username) {
        return "andy123456";
    }
}
