package me.iszhenyu.ifiji;

import me.iszhenyu.ifiji.dao.interceptor.AutoDateTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by xiaoz on 2017/5/21.
 */
@SpringBootApplication
public class CoreApplication {

	@Bean
	public Interceptor autoDateTimeInterceptor() {
		return new AutoDateTimeInterceptor();
	}

}
