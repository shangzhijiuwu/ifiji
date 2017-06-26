package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.constant.UserStatus;
import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.util.RandomUtils;
import me.iszhenyu.ifiji.util.StringUtils;
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
	private SecurityService securityService;

	public UserDO getUser(String username) {
		if (StringUtils.isMobile(username)) {
			return userDao.getByMobile(username);
		} else if (StringUtils.isEmail(username)) {
			return userDao.getByEmail(username);
		}
		return userDao.getByUsername(username);
	}

	public UserDO registerUser(String principal, String credential) {
		String salt = RandomUtils.randomNumeric(4);
		String hashedPassword = securityService.encodePasswordWithSalt(credential, salt);
		if (StringUtils.isMobile(principal)) {
			String username = principal + "@";
			return createUser(username, principal, hashedPassword, salt);
		}
		return createUser(principal, null, hashedPassword, salt);
	}

	private UserDO createUser(String username, String mobile, String password, String salt) {
		UserDO user = new UserDO();
		user.setUsername(username);
		user.setMobileNumber(mobile);
		user.setPasswordHash(password);
		user.setPasswordSalt(salt);
		user.setStatus(UserStatus.ACTIVE);
		user.setDeleted(false);
		userDao.insert(user);
		return user;
	}

}
