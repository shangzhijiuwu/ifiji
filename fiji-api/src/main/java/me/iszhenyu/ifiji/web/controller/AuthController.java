package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.exception.ValidationException;
import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.service.UserService;
import me.iszhenyu.ifiji.web.form.LoginForm;
import me.iszhenyu.ifiji.web.form.RegisterForm;
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
	private UserService userService;

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(@Validated RegisterForm form, BindingResult bindingResult) {
		this.validateForm(bindingResult);
		return "注册成功";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserDO login(@Validated LoginForm form, BindingResult bindingResult) {
		this.validateForm(bindingResult);
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(
				form.getUsername(), form.getPassword()
		);
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			throw new ValidationException("用户名或密码错误");
		}
		return userService.getUser();
	}

	@RequestMapping("/logout")
	public String logout() {
		return "登出成功";
	}

}
