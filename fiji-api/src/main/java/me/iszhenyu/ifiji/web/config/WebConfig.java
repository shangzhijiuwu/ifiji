package me.iszhenyu.ifiji.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by xiaoz on 2017/5/12.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }
}
