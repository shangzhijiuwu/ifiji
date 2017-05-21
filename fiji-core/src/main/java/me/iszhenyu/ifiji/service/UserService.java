package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public User getUser() {
		User user = userDao.get(1);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		userDao.update(user);
		return user;
	}

	public User createUser() {
		User user = new User();
		user.setUsername("this is a test");
		user.setPasswordHash("123");
		user.setPasswordSalt("456");
		user.setMobileNumber("15110223333");
		user.setEmail("zhen.yu@17zuoye.com");
		user.setDeleted(false);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		userDao.insert(user);
		return user;
	}

}
