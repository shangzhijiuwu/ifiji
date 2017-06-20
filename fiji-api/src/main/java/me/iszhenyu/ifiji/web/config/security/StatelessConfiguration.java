package me.iszhenyu.ifiji.web.config.security;

import me.iszhenyu.ifiji.security.CacheName;
import me.iszhenyu.ifiji.security.FijiRealm;
import me.iszhenyu.ifiji.security.RetryLimitHashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhen.yu
 * @since 2017/6/7
 */
@Configuration
public class StatelessConfiguration {

    @Bean
    public StatelessFilter jwtFilter() {
        StatelessFilter filter = new StatelessFilter();
        filter.setLoginUrl("/auth/login");
        return filter;
    }

    @Bean
    public StatelessCSRFFilter csrfFilter() {
        return new StatelessCSRFFilter();
    }

    @Bean
    @Qualifier("simpleCredentialsMatcher")
    public JwtRealm jwtRealm(SimpleCredentialsMatcher simpleCredentialsMatcher) {
        JwtRealm realm = new JwtRealm();
        realm.setCredentialsMatcher(simpleCredentialsMatcher);
        realm.setCachingEnabled(false);
        return realm;
    }

    @Bean
    @Qualifier("retryLimitHashedCredentialsMatcher")
    public FijiRealm fijiRealm(DataSource dataSource, RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher) {
        FijiRealm realm = new FijiRealm();
        realm.setDataSource(dataSource);
        realm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher);
        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName(CacheName.AUTHENTICATION_CACHE);
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName(CacheName.AUTHORIZATION_CACHE);
        return realm;
    }

    @Bean
    public DefaultWebSubjectFactory subjectFactory() {
        return new StatelessSubjectFactory();
    }

    @Bean
    public DefaultSessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    @Bean
    public SecurityManager securityManager(FijiRealm fijiRealm, JwtRealm jwtRealm, CacheManager cacheManager, SessionManager sessionManager) {
        DefaultSecurityManager sm = new DefaultWebSecurityManager();
        sm.setSubjectFactory(subjectFactory());
        sm.setRealms(Arrays.asList(fijiRealm, jwtRealm));
        sm.setCacheManager(cacheManager);
        sm.setSessionManager(sessionManager);
		/*
         * 禁用使用Sessions 作为存储策略的实现，但它没有完全地禁用Sessions
         * 所以需要配合context.setSessionCreationEnabled(false);
         */
        ((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) sm.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
        return sm;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        StatelessFilterFactoryBean factoryBean = new StatelessFilterFactoryBean(securityManager);
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
