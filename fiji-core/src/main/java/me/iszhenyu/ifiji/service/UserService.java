package me.iszhenyu.ifiji.service;

import me.iszhenyu.ifiji.dao.UserDao;
import me.iszhenyu.ifiji.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public User getUser() {
		return userDao.get(1);
	}

	public void createUser() {
		User user = new User();
		userDao.insert(user);
	}

}
