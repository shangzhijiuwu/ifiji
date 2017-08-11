package me.iszhenyu.ifiji;

import me.iszhenyu.ifiji.core.dao.interceptor.AutoDateTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by xiaoz on 2017/5/21.
 */
@EnableCaching  // 开启声明式缓存支持
@SpringBootApplication
public class BusinessApplication {

	@Bean
	public Interceptor autoDateTimeInterceptor() {
		return new AutoDateTimeInterceptor();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
