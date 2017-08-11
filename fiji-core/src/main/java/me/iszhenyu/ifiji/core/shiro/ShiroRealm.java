package me.iszhenyu.ifiji.core.shiro;

import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * Created by xiaoz on 2017/5/9.
 */
public class ShiroRealm extends JdbcRealm {

	public ShiroRealm() {
		this.setSaltStyle(SaltStyle.COLUMN);
		this.setPermissionsLookupEnabled(true);
		this.setAuthenticationQuery("select password_hash, password_salt from sys_user where username = ?");
		this.setAuthenticationCachingEnabled(true);
		this.setAuthenticationCacheName(ShiroCacheName.AUTHENTICATION_CACHE);
		this.setAuthorizationCachingEnabled(true);
		this.setAuthorizationCacheName(ShiroCacheName.AUTHORIZATION_CACHE);
	}

}
