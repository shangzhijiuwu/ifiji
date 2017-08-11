package me.iszhenyu.ifiji.dao;

import me.iszhenyu.ifiji.core.dao.BaseDao;
import me.iszhenyu.ifiji.core.dao.BaseMapper;
import me.iszhenyu.ifiji.dao.mapper.UserMapper;
import me.iszhenyu.ifiji.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Component
public class UserDao extends BaseDao<UserDO> {

	@Autowired
	private UserMapper userMapper;

	@Override
	protected BaseMapper<UserDO> getMapper() {
		return userMapper;
	}

	public UserDO getByMobile(String mobile) {
		return userMapper.getByMobileNumber(mobile);
	}

	public UserDO getByUsername(String username) {
		return userMapper.getByUsername(username);
	}

	public UserDO getByEmail(String email) {
		return userMapper.getByEmail(email);
	}

	public void updateMobileNumber(UserDO user) {
		userMapper.updateMobileNumber(user);
	}
}
