package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.constant.UserStatus;
import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.UserDO;
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

	public UserDO getUser(String username) {
		if (StringUtils.isMobile(username)) {
			return userDao.getByMobile(username);
		} else if (StringUtils.isEmail(username)) {
			return userDao.getByEmail(username);
		}
		return userDao.getByUsername(username);
	}

	public UserDO createUser(String username, String password) {
		UserDO user = new UserDO();
		user.setUsername("this is a test3");
		user.setPasswordHash("123");
		user.setPasswordSalt("456");
		user.setMobileNumber("15110223334");
		user.setEmail("zhen.yu.3@17zuoye.com");
		user.setStatus(UserStatus.ACTIVE);
		user.setDeleted(false);
		userDao.save(user);
		return user;
	}

}
