package me.iszhenyu.ifiji.core.shiro;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiaoz on 2017/5/9.
 */
@Configuration
public class ShiroConfiguration {

	private Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

	/**
	 * Shiro生命周期处理器
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
	 * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 */
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 配置缓存
	 */
	@Bean
	public EhCacheManager shiroCacheManager() {
		logger.debug("注入Shiro的缓存管理器-->ehCacheManager");
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return ehCacheManager;
	}

	@Bean
	public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
		logger.debug("注入Shiro的凭证匹配器-->credentialsMatcher");
		RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(shiroCacheManager());
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(2);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	/**
	 * 配置Realm
	 */
	@Bean
	@Qualifier("retryLimitHashedCredentialsMatcher")
	public ShiroRealm shiroRealm(DataSource dataSource) {
		logger.debug("注入Shiro的Realm-->shiroRealm");
		ShiroRealm realm = new ShiroRealm();
		realm.setDataSource(dataSource);
		realm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
		return realm;
	}

	/**
	 * * * * * Session 相关
	 */

	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(1800000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler());
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		sessionManager.setSessionDAO(sessionDAO());
		return sessionManager;
	}

	@Bean
	public SessionValidationScheduler sessionValidationScheduler() {
		QuartzSessionValidationScheduler scheduler = new QuartzSessionValidationScheduler();
		scheduler.setSessionValidationInterval(1800000);
		return scheduler;
	}

	@Bean
	public SimpleCookie sessionIdCookie(){
		SimpleCookie cookie = new SimpleCookie();
		cookie.setHttpOnly(true);
		cookie.setMaxAge(259200);
		cookie.setName("sid");
		return cookie;
	}

	@Bean
	public SessionDAO sessionDAO() {
		EnterpriseCacheSessionDAO dao = new EnterpriseCacheSessionDAO();
		dao.setSessionIdGenerator(sessionIdGenerator());
		dao.setCacheManager(shiroCacheManager());
		dao.setActiveSessionsCacheName(ShiroCacheName.SESSION_CACHE);
		return dao;
	}

	@Bean
	public SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	/**
	 * * * * * cookie管理器
	 */

	@Bean
	public CookieRememberMeManager rememberMeManager() {
		logger.info("注入Shiro的记住我(CookieRememberMeManager)管理器-->rememberMeManager");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		//rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位），通过以下代码可以获取
		//KeyGenerator keygen = KeyGenerator.getInstance("AES");
		//SecretKey deskey = keygen.generateKey();
		//System.out.println(Base64.encodeToString(deskey.getEncoded()));
		byte[] cipherKey = Base64.decode("rPNqM6uKFCyaL10AK51UkQ==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}

	@Bean
	public SimpleCookie rememberMeCookie(){
		//这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		//如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
		simpleCookie.setHttpOnly(true);
		//记住我cookie生效时间,默认30天 ,单位秒：60 * 60 * 24 * 30
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * 配置安全管理器
	 */
	@Bean
	public SecurityManager securityManager(ShiroRealm shiroRealm) {
		logger.debug("注入Shiro的Web过滤器-->securityManager");
		DefaultSecurityManager sm = new DefaultWebSecurityManager();
		sm.setRealm(shiroRealm);
		sm.setCacheManager(shiroCacheManager());
		sm.setSessionManager(sessionManager());
		sm.setRememberMeManager(rememberMeManager());
		return sm;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		CustomFilterFactoryBean factoryBean = new CustomFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager);
		factoryBean.setLoginUrl("/auth/login");
		factoryBean.setSuccessUrl("/");
		factoryBean.setUnauthorizedUrl("/error/401");

		//拦截器.
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
		filterChainDefinitionMap.put("/favicon.ico", "anon");
		filterChainDefinitionMap.put("/assets/**", "anon");
		filterChainDefinitionMap.put("/error/**", "anon");
		filterChainDefinitionMap.put("/auth/login", "anon");
		filterChainDefinitionMap.put("/auth/register", "anon");
		filterChainDefinitionMap.put("/auth/logout", "logout");
		//配置记住我过滤器或认证通过可以访问的地址(当上次登录时，记住我以后，在下次访问/或/index时，可以直接访问，不需要登陆)
		filterChainDefinitionMap.put("/index", "user");
		filterChainDefinitionMap.put("/", "user");
		filterChainDefinitionMap.put("/**", "authc");
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return factoryBean;
	}

//	@Bean
//	public FilterRegistrationBean delegatingFilterProxy() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//		proxy.setTargetFilterLifecycle(true);
//		proxy.setTargetBeanName("shiroFilter");
//		filterRegistrationBean.setFilter(proxy);
//		return filterRegistrationBean;
//	}
}
