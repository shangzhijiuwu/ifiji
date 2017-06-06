package me.iszhenyu.ifiji.security;

import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * Created by xiaoz on 2017/5/9.
 */
class FijiRealm extends JdbcRealm {

	FijiRealm() {
		this.setSaltStyle(SaltStyle.COLUMN);
		this.setPermissionsLookupEnabled(true);
		this.setAuthenticationQuery("select password_hash, password_salt from sys_user where username = ?");
	}

}
