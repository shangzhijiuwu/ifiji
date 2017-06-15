package me.iszhenyu.ifiji.web.config.security;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhen.yu
 * @since 2017/6/7
 */
@Configuration
public class StatelessConfiguration {

    @Bean
    public JwtFilter jwtFilter() {
        JwtFilter filter = new JwtFilter();
        filter.setLoginUrl("/auth/login");
        return filter;
    }

    @Bean
    public StatelessCSRFFilter csrfFilter() {
        return new StatelessCSRFFilter();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        StatelessFilterFactoryBean factoryBean = new StatelessFilterFactoryBean(securityManager);
        factoryBean.setSecurityManager(securityManager);
        factoryBean.getFilters().put("jwtAuthc", jwtFilter());
        factoryBean.getFilters().put("csrf", csrfFilter());

        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/auth/login", "anon");
        filterChainDefinitionMap.put("/auth/logout", "logout");
        filterChainDefinitionMap.put("/**", "jwtAuthc");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

}
