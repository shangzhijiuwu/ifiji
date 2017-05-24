package me.iszhenyu.ifiji.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.iszhenyu.ifiji.constant.UserStatus;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class UserDO extends BaseDO {
	private String username;
	private String passwordHash;
	private String passwordSalt;
	private String mobileNumber;
	private String email;
	private String avatarUrl;
	private UserStatus status;
}
