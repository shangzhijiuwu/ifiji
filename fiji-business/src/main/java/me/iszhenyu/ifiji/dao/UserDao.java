package me.iszhenyu.ifiji.dao;

import me.iszhenyu.ifiji.core.dao.BaseDao;
import me.iszhenyu.ifiji.core.dao.BaseMapper;
import me.iszhenyu.ifiji.dao.mapper.UserMapper;
import me.iszhenyu.ifiji.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Component
@CacheConfig(cacheNames = "user")
public class UserDao extends BaseDao<User> {

	@Autowired
	private UserMapper userMapper;

	@Override
	protected BaseMapper<User> getMapper() {
		return userMapper;
	}

	@Cacheable
	public User getByMobile(String mobile) {
		return userMapper.getByMobileNumber(mobile);
	}

	@Cacheable
	public User getByUsername(String username) {
		return userMapper.getByUsername(username);
	}

	@Cacheable
	public User getByEmail(String email) {
		return userMapper.getByEmail(email);
	}

	public void updateMobileNumber(User user) {
		userMapper.updateMobileNumber(user);
	}
}
