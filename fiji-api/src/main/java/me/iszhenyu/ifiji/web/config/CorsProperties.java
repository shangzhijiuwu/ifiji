package me.iszhenyu.ifiji.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhen.yu
 * @since 2017/7/7
 */
@Getter
@Setter
@Component
@ConfigurationProperties("cors")
public class CorsProperties {
    private String allowOrigin;
    private String allowMethods;
    private String allowCredentials;
    private String allowHeaders;
    private String exposeHeaders;
}
