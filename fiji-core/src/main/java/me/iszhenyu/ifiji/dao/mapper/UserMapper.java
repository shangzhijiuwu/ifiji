package me.iszhenyu.ifiji.dao.mapper;

import me.iszhenyu.ifiji.model.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

	UserDO getByMobileNumber(String mobile);

	void updateMobileNumber(UserDO user);
}
