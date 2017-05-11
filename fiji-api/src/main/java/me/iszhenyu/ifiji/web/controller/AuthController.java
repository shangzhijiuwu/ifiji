package me.iszhenyu.ifiji.web.controller;

import me.iszhenyu.ifiji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaoz on 2017/5/11.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;



}
