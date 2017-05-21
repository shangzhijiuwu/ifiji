package me.iszhenyu.ifiji.web;

import me.iszhenyu.ifiji.CoreApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by xiaoz on 2017/5/11.
 */
//@MapperScan("me.iszhenyu.ifiji.dao.mapper")
@SpringBootApplication()
public class IFijiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(CoreApplication.class, IFijiApplication.class)
				.run(args);
	}

}
