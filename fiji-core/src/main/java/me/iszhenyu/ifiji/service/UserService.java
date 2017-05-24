package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.constant.UserStatus;
import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by xiaoz on 2017/5/11.
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public UserDO getUser() {
		UserDO user = userDao.getByMobile("15122223333");
//		user.setMobileNumber("15122223333");
//		userDao.updateMobileNumber(user);
		return user;
	}

	public UserDO createUser() {
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
