package me.iszhenyu.ifiji.core.cache;

import me.iszhenyu.ifiji.core.cache.redis.JsonRedisSerializer;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

/**
 * @author zhen.yu
 * @since 2017/9/7
 */
@Configuration
public class RedisCacheConfiguration extends JCacheConfigurerSupport {

    private class CustomRedisTemplate extends RedisTemplate<String, Object> {

        CustomRedisTemplate() {
            RedisSerializer<String> stringSerializer = new StringRedisSerializer();
            JsonRedisSerializer jsonSerializer = new JsonRedisSerializer();
            setKeySerializer(stringSerializer);
            setValueSerializer(jsonSerializer);
            setHashKeySerializer(stringSerializer);
            setHashValueSerializer(jsonSerializer);
        }
    }

    @Bean
    public CustomRedisTemplate customRedisTemplate(
            RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        CustomRedisTemplate template = new CustomRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(CustomRedisTemplate customRedisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(customRedisTemplate);
        cacheManager.setUsePrefix(true);
        return cacheManager;
    }

}
