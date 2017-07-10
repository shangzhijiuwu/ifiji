package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.ValidationException;
import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.service.SecurityService;
import me.iszhenyu.ifiji.service.UserService;
import me.iszhenyu.ifiji.web.config.security.JwtProperties;
import me.iszhenyu.ifiji.web.form.RegisterForm;
import me.iszhenyu.ifiji.web.validator.annotation.NotEmpty;
import me.iszhenyu.ifiji.web.vo.LoginVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaoz on 2017/5/11.
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

	@Autowired
	JwtProperties jwtProperties;
	@Autowired
	private UserService userService;
	@Autowired
	private SecurityService securityService;

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(@Validated RegisterForm form, BindingResult bindingResult) {
		this.validateForm(bindingResult);
		if (!form.getPassword().equals(form.getRePassword())) {
			throw new ValidationException("两次密码不一致");
		}
		userService.registerUser(form.getUsername(), form.getPassword());
		return "注册成功";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginVO login(@NotEmpty(message = "用户名不能为空") String username,
						 @NotEmpty(message = "密码不能为空") String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			// 根据token类型, 这里实际是FijiRealm执行的登录
			subject.login(token);
		} catch (AuthenticationException e) {
			throw new ValidationException("用户名或密码错误");
		}
		UserDO user =  userService.getUser(username);
		String jwtTokenStr = securityService.generateJwtToken(user, jwtProperties.getKey(), jwtProperties.getTokenExpireDay());
		return new LoginVO(jwtTokenStr, user);
	}

	@RequestMapping("/logout")
	public String logout() {
		return "登出成功";
	}

}
