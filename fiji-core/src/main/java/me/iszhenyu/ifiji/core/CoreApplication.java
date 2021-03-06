package me.iszhenyu.ifiji.core;

import me.iszhenyu.ifiji.core.dao.interceptor.AutoDateTimeInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author zhen.yu
 * @since 2017/5/21
 */
@SpringBootApplication
public class CoreApplication {

	@Bean
	public Interceptor autoDateTimeInterceptor() {
		return new AutoDateTimeInterceptor();
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
