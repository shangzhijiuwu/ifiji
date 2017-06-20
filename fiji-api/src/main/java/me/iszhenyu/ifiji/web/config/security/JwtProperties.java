package me.iszhenyu.ifiji.web.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by xiaoz on 2017/2/17.
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
	private String key;
	private String tokenHeaderName;
	private String tokenCookieName;
	private int tokenExpireDay;
}
