package me.iszhenyu.ifiji.dao.mapper;

import me.iszhenyu.ifiji.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	@Select("select * from sys_user where mobile_number = #{mobile}")
	User getByMobileNumber(@Param("mobile") String mobile);

	@Select("select * from sys_user where username = #{username}")
	User getByUsername(@Param("username") String username);
}
