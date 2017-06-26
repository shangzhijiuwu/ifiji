package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.constant.UserStatus;
import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.security.RetryLimitHashedCredentialsMatcher;
import me.iszhenyu.ifiji.util.RandomUtils;
import me.iszhenyu.ifiji.util.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by xiaoz on 2017/5/11.
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RetryLimitHashedCredentialsMatcher credentialsMatcher;

	public UserDO getUser(String username) {
		if (StringUtils.isMobile(username)) {
			return userDao.getByMobile(username);
		} else if (StringUtils.isEmail(username)) {
			return userDao.getByEmail(username);
		}
		return userDao.getByUsername(username);
	}

	public UserDO createUser(String username, String password) {
		String salt = RandomUtils.randomNumeric(4);
		SimpleHash hash = new SimpleHash(credentialsMatcher.getHashAlgorithmName(), password, salt, credentialsMatcher.getHashIterations());

		UserDO user = new UserDO();
		user.setUsername(username);
		user.setPasswordHash(hash.toString());
		user.setPasswordSalt(salt);
		user.setStatus(UserStatus.ACTIVE);
		user.setDeleted(false);
		userDao.save(user);
		return user;
	}

}
