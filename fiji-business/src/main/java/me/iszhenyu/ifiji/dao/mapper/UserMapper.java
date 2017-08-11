package me.iszhenyu.ifiji.dao.mapper;

import me.iszhenyu.ifiji.core.dao.BaseMapper;
import me.iszhenyu.ifiji.model.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.io.Serializable;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

	@Override
	@Select("select * from sys_user where id=#{id}")
	UserDO get(Serializable id);

	@Select("select * from sys_user where mobile_number=#{mobileNumber}")
	UserDO getByMobileNumber(String mobile);

	@Select("select * from sys_user where username=#{username}")
	UserDO getByUsername(String username);

	@Select("select * from sys_user where email=#{email}")
	UserDO getByEmail(String email);

	@Update("UPDATE sys_user SET mobile_number=#{mobileNumber}, gmt_modified=#{gmtModified} WHERE id=#{id}")
	void updateMobileNumber(UserDO user);
}
