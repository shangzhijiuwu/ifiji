package me.iszhenyu.ifiji.web.config;

import me.iszhenyu.ifiji.web.validator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by xiaoz on 2017/5/12.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ArgumentNotNullResolver());
        argumentResolvers.add(new ArgumentNotEmptyResolver());
        argumentResolvers.add(new ArgumentInRangeResolver());
        argumentResolvers.add(new ArgumentMinResolver());
        argumentResolvers.add(new ArgumentMaxResolver());
    }
}
