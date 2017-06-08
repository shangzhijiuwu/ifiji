package me.iszhenyu.ifiji.security;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.CipherService;
import org.apache.shiro.io.Serializer;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaoz on 2017/5/9.
 */
//@Configuration
public class ShiroConfiguration {

	@Autowired(required = false)
	private CipherService cipherService;

	@Autowired(required = false)
	private Serializer<PrincipalCollection> serializer;

	@Autowired(required = false)
	private Collection<SessionListener> listeners;

	@Autowired(required = false)
	private JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader;

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
		sm.setRealm(realm);
		sm.setCacheManager(cacheManager);
		sm.setSessionManager(sessionManager);
		sm.setRememberMeManager(rememberMeManager);
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

	/////////////////////////////////////////
	//              session 相关           //
	////////////////////////////////////////

	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	@Bean
	public SessionDAO sessionDAO(CacheManager cacheManager, SessionIdGenerator sessionIdGenerator) {
		EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
		sessionDAO.setCacheManager(cacheManager);
		sessionDAO.setSessionIdGenerator(sessionIdGenerator);
		sessionDAO.setActiveSessionsCacheName(CacheName.ACTIVE_SESSION_CACHE);
		return sessionDAO;
	}

	@Bean
	public WebSessionManager sessionManager(CacheManager cacheManager, SessionDAO sessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager);
		sessionManager.setGlobalSessionTimeout(36000); // 默认30分钟
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionListeners(listeners);
		return sessionManager;
	}

	@Bean
	public SessionValidationScheduler sessionValidationScheduler(DefaultWebSessionManager sessionManager) {
		QuartzSessionValidationScheduler quartzSessionValidationScheduler = new QuartzSessionValidationScheduler(sessionManager);
		sessionManager.setSessionValidationSchedulerEnabled(true); // 开启定时验证Session
		sessionManager.setSessionValidationInterval(36000); // 每隔一小时验证Session
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationScheduler(quartzSessionValidationScheduler);
		return quartzSessionValidationScheduler;
	}

	/////////////////////////////////////////
	//              Filter 相关           //
	////////////////////////////////////////

	@Bean(name = "shiroFilter")
	public FilterRegistrationBean filterRegistrationBean(SecurityManager securityManager) throws Exception {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		//该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setFilter((Filter) getShiroFilterFactoryBean(securityManager).getObject());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean
	public JdbcPermissionDefinitionsLoader jdbcFilterChainsLoader(DataSource dataSource) {
		JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader = new JdbcPermissionDefinitionsLoader(dataSource);
		jdbcPermissionDefinitionsLoader.setSql("SELECT url, permission FROM sys_resource WHERE permission != '' AND url != ''");
		return jdbcPermissionDefinitionsLoader;
	}

	private ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) throws Exception {
		FijiFilterFactoryBean shiroFilter = new FijiFilterFactoryBean(securityManager);

		// 自定义过滤器
		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("authc", formLoginFilter());
		shiroFilter.setFilters(filterMap); // filters 属性用于定义自己的过滤器

		// 拦截器
		Map<String, String> filterChains = new LinkedHashMap<>();
		filterChains.put("/favicon.ico", "anon");
		filterChains.put("/assets/**", "anon");
		filterChains.put("/error/**", "anon");
		filterChains.put("/auth/login", "anon");
		filterChains.put("/auth/logout", "logout");
		//配置记住我或认证通过可以访问的地址
		filterChains.put("/index", "user");
		filterChains.put("/", "user");
		filterChains.put("/**", "authc");
		if (jdbcPermissionDefinitionsLoader != null) {
			Map<String, String> permissionUrlMap = jdbcPermissionDefinitionsLoader.getObject();
			filterChains.putAll(permissionUrlMap);
		}
		shiroFilter.setFilterChainDefinitionMap(filterChains); // filterChainDefinitions 用于声明 url 和 filter 的关系

		return shiroFilter;
	}

	private FormLoginFilter formLoginFilter() {
		return new FormLoginFilter();
	}
}
