package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.model.User;
import me.iszhenyu.ifiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaoz on 2017/5/11.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public User doLogin() {
		return null;
	}

	@RequestMapping("/logout")
	public String logout() {
		return null;
	}

}