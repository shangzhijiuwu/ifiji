package me.iszhenyu.ifiji.web.config.security;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaoz on 2017/5/9.
 */
@Configuration
public class ShiroConfiguration {

	@Autowired(required = false)
	private CipherService cipherService;

	@Autowired(required = false)
	private Serializer<PrincipalCollection> serializer;

	@Autowired(required = false)
	private Collection<SessionListener> listeners;

	@Autowired(required = false)
	private ShiroFilterCustomizer shiroFilterCustomizer;

	@Autowired(required = false)
	private JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader;

	/**
	 * Shiro生命周期处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@ConditionalOnMissingBean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	@ConditionalOnMissingBean
	@DependsOn("cacheManager")
	public CredentialsMatcher credentialsMatcher(CacheManager cacheManager) {
		RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		credentialsMatcher.setRetryTimes(3);
		return credentialsMatcher;
	}

	@Bean
	@ConditionalOnMissingBean(name = "mainRealm")
	@DependsOn(value = {"dataSource", "lifecycleBeanPostProcessor", "credentialsMatcher"})
	public Realm mainRealm(DataSource dataSource, CredentialsMatcher credentialsMatcher) {
		FijiRealm realm = new FijiRealm();
		realm.setDataSource(dataSource);
		realm.setCredentialsMatcher(credentialsMatcher);
		return realm;
	}

	@Bean
	@ConditionalOnClass(name = {"org.apache.shiro.cache.ehcache.EhCacheManager"})
	@ConditionalOnMissingBean(name = "cacheManager")
	public CacheManager cacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return ehCacheManager;
	}

	@Bean
	@ConditionalOnMissingBean(Cookie.class)
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
	@ConditionalOnMissingBean(RememberMeManager.class)
	public RememberMeManager rememberMeManager(Cookie cookie) {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(cookie);
		cookieRememberMeManager.setCipherService(cipherService);
//		if (shiroCookieProperties.getCipherKey() != null) {
//			cookieRememberMeManager.setCipherKey(shiroCookieProperties.getCipherKey().getBytes());
//		} else {
//			if (shiroCookieProperties.getEncryptionCipherKey() != null) {
//				cookieRememberMeManager.setEncryptionCipherKey(shiroCookieProperties.getEncryptionCipherKey().getBytes());
//			}
//			if (shiroCookieProperties.getDecryptionCipherKey() != null) {
//				cookieRememberMeManager.setDecryptionCipherKey(shiroCookieProperties.getDecryptionCipherKey().getBytes());
//			}
//		}
		cookieRememberMeManager.setSerializer(serializer);
		return cookieRememberMeManager;
	}

	@Bean
	@DependsOn(value = {"cacheManager", "rememberMeManager", "mainRealm"})
	public DefaultSecurityManager securityManager(Realm realm, RememberMeManager rememberMeManager,
                                                  CacheManager cacheManager, SessionManager sessionManager) {
		DefaultSecurityManager sm = new DefaultWebSecurityManager();
		sm.setRealm(realm);
		sm.setCacheManager(cacheManager);
		sm.setSessionManager(sessionManager);
		sm.setRememberMeManager(rememberMeManager);
		return sm;
	}

	/////////////////////////////////////////
	//              session 相关           //
	////////////////////////////////////////

	@Bean
	@ConditionalOnMissingBean
	public SessionDAO sessionDAO(CacheManager cacheManager) {
		EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
		sessionDAO.setActiveSessionsCacheName("shiro-acciveSessionCache");
		SessionIdGenerator sessionIdGenerator = BeanUtils.instantiate(JavaUuidSessionIdGenerator.class);
		sessionDAO.setSessionIdGenerator(sessionIdGenerator);
		sessionDAO.setCacheManager(cacheManager);
		return sessionDAO;
	}

	@Bean
	@DependsOn(value = {"cacheManager", "sessionDAO"})
	public WebSessionManager sessionManager(CacheManager cacheManager, SessionDAO sessionDAO) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setCacheManager(cacheManager);
//		sessionManager.setGlobalSessionTimeout(36000); // 默认30分钟
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionListeners(listeners);
		return sessionManager;
	}

	@Bean(name = "sessionValidationScheduler")
	@DependsOn(value = {"sessionManager"})
	@ConditionalOnMissingBean(SessionValidationScheduler.class)
	public SessionValidationScheduler quartzSessionValidationScheduler(DefaultWebSessionManager sessionManager) {
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
	@DependsOn("securityManager")
	@ConditionalOnMissingBean
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
	@ConditionalOnMissingBean
	public JdbcPermissionDefinitionsLoader jdbcFilterChainsLoader(DataSource dataSource) {
		JdbcPermissionDefinitionsLoader jdbcPermissionDefinitionsLoader = new JdbcPermissionDefinitionsLoader(dataSource);
		jdbcPermissionDefinitionsLoader.setSql("SELECT url, permission FROM sys_resource WHERE permission != '' AND url != ''");
		return jdbcPermissionDefinitionsLoader;
	}

	private ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) throws Exception {
		FijiFilterFactoryBean shiroFilter = new FijiFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		shiroFilter.setLoginUrl("/auth/login");
		shiroFilter.setSuccessUrl("/");
		shiroFilter.setUnauthorizedUrl("/error/401");

		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("authc", formLoginFilter());
		if (shiroFilterCustomizer != null) {
			filterMap = shiroFilterCustomizer.customize(filterMap);
		}
		shiroFilter.setFilters(filterMap); // filters 属性用于定义自己的过滤器

		Map<String, String> filterChains = new LinkedHashMap<>();
		filterChains.put("/favicon.ico", "anon");
		filterChains.put("/assets/**", "anon");
		filterChains.put("/error/**", "anon");
		filterChains.put("/auth/login", "anon");
		filterChains.put("/auth/logout", "logout");
		filterChains.put("/**", "authc");
		if (jdbcPermissionDefinitionsLoader != null) {
			Map<String, String> permissionUrlMap = jdbcPermissionDefinitionsLoader.getObject();
			filterChains.putAll(permissionUrlMap);
		}
		shiroFilter.setFilterChainDefinitionMap(filterChains); // filterChainDefinitions 用于声明 url 和 filter 的关系

		return shiroFilter;
	}

	private FormLoginFilter formLoginFilter() {
		FormLoginFilter filter = new FormLoginFilter();
		filter.setLoginUrl("/auth/login");
		filter.setSuccessUrl("/");
		filter.setUsernameParam("username");
		filter.setPasswordParam("password");
		filter.setRememberMeParam("rememberMe");
		return filter;
	}

	private Map<String, Filter> instantiateFilterClasses(Map<String, Class<? extends Filter>> filters) {
		Map<String, Filter> filterMap = null;
		if (filters != null && !filters.isEmpty()) {
			filterMap = new LinkedHashMap<>();
			for (String name : filters.keySet()) {
				Class<? extends Filter> clazz = filters.get(name);
				Filter f = BeanUtils.instantiate(clazz);
				filterMap.put(name, f);
			}
		}
		return filterMap;
	}
}
