package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.model.UserDO;
import me.iszhenyu.ifiji.service.UserService;
import me.iszhenyu.ifiji.web.form.LoginForm;
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

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public UserDO login(@Validated LoginForm form, BindingResult bindingResult) {
		this.validateForm(bindingResult);
		return null;
	}

	@RequestMapping("/logout")
	public String logout() {
		return null;
	}

}
