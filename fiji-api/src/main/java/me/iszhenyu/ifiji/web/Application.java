package me.iszhenyu.ifiji.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by xiaoz on 2017/5/11.
 */
@MapperScan("me.iszhenyu.ifiji.dao.mapper")
@SpringBootApplication(scanBasePackages = "me.iszhenyu.ifiji")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
