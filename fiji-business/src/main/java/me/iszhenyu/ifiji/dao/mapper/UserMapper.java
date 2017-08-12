package me.iszhenyu.ifiji.dao.mapper;

import me.iszhenyu.ifiji.core.dao.BaseMapper;
import me.iszhenyu.ifiji.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	@Override
	@Select("select * from sys_user where id=#{id}")
	User get(Serializable id);

	@Select("select * from sys_user where mobile_number=#{mobileNumber}")
	User getByMobileNumber(String mobile);

	@Select("select * from sys_user where username=#{username}")
	User getByUsername(String username);

	@Select("select * from sys_user where email=#{email}")
	User getByEmail(String email);

	@Update("UPDATE sys_user SET mobile_number=#{mobileNumber}, gmt_modified=#{gmtModified} WHERE id=#{id}")
	void updateMobileNumber(User user);
}
