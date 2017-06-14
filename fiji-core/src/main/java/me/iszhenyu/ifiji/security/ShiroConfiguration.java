package me.iszhenyu.ifiji.security;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.CipherService;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by xiaoz on 2017/5/9.
 */
@Configuration
public class ShiroConfiguration {

	@Autowired(required = false)
	private CipherService cipherService;

	@Autowired(required = false)
	private Serializer<PrincipalCollection> serializer;

	/**
	 * Shiro生命周期处理器
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
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
	public CacheManager cacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return ehCacheManager;
	}

	@Bean
	public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
		RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	@Bean
	public Realm fijiRealm(DataSource dataSource, CredentialsMatcher credentialsMatcher) {
		FijiRealm realm = new FijiRealm();
		realm.setDataSource(dataSource);
		realm.setCredentialsMatcher(credentialsMatcher);
		realm.setCachingEnabled(true);
		realm.setAuthenticationCachingEnabled(true);
		realm.setAuthenticationCacheName(CacheName.AUTHENTICATION_CACHE);
		realm.setAuthorizationCachingEnabled(true);
		realm.setAuthorizationCacheName(CacheName.AUTHORIZATION_CACHE);
		return realm;
	}

	@Bean
	public Cookie rememberMeCookie() {
		SimpleCookie rememberMeCookie = new SimpleCookie();
		rememberMeCookie.setName("rememberMe");
		rememberMeCookie.setMaxAge(2592000); // 30天
		rememberMeCookie.setVersion(1);
		rememberMeCookie.setHttpOnly(true);
		rememberMeCookie.setSecure(false);
		return rememberMeCookie;
	}

	@Bean
	public RememberMeManager rememberMeManager(Cookie cookie) {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(cookie);
		cookieRememberMeManager.setCipherService(cipherService);
		cookieRememberMeManager.setSerializer(serializer);
		return cookieRememberMeManager;
	}

	@Bean
	public DefaultSecurityManager securityManager(Realm realm, RememberMeManager rememberMeManager,
                                                  CacheManager cacheManager, SessionManager sessionManager) {
		DefaultSecurityManager sm = new DefaultWebSecurityManager();
		sm.setSubjectFactory(subjectFactory());
		sm.setRealm(realm);
		sm.setCacheManager(cacheManager);
		sm.setSessionManager(sessionManager);
		sm.setRememberMeManager(rememberMeManager);
		/*
         * 禁用使用Sessions 作为存储策略的实现，但它没有完全地禁用Sessions
         * 所以需要配合context.setSessionCreationEnabled(false);
         */
		((DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) sm.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
		return sm;
	}



	/**
	 *  开启shiro aop注解支持.
	 *  使用代理方式;所以需要开启代码支持;
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
