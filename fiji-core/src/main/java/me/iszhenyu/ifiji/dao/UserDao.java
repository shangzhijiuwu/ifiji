package me.iszhenyu.ifiji.dao;

import me.iszhenyu.ifiji.dao.mapper.BaseMapper;
import me.iszhenyu.ifiji.dao.mapper.UserMapper;
import me.iszhenyu.ifiji.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Component
public class UserDao extends BaseDao<User> {

	@Autowired
	private UserMapper userMapper;

	@Override
	protected BaseMapper<User> getMapper() {
		return userMapper;
	}
}
