package me.iszhenyu.ifiji.dao.mapper;

import me.iszhenyu.ifiji.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	User getByMobileNumber(String mobile);

	void updateMobileNumber(User user);
}
