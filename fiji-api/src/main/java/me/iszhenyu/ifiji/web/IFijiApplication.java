package me.iszhenyu.ifiji.web;

import me.iszhenyu.ifiji.BusinessApplication;
import me.iszhenyu.ifiji.core.CoreApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by xiaoz on 2017/5/11.
 */
@SpringBootApplication
public class IFijiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(
						CoreApplication.class,
						BusinessApplication.class,
						IFijiApplication.class
				)
				.run(args);
	}

}
