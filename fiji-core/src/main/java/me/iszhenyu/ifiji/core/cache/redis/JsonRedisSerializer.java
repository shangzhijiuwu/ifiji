package me.iszhenyu.ifiji.core.cache.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.StringUtils;

/**
 * @author zhen.yu
 * @since 2017/9/8
 */
public class JsonRedisSerializer implements RedisSerializer<Object> {
    private static final byte[] EMPTY_ARRAY = new byte[0];
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonRedisSerializer() {
        this(null);
    }

    public JsonRedisSerializer(String classPropertyTypeName) {
        if (StringUtils.hasText(classPropertyTypeName)) {
            objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return EMPTY_ARRAY;
        }
        try {
            return this.objectMapper.writeValueAsBytes(o);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return deserialize(bytes, Object.class);
    }

    private <T> T deserialize(byte[] bytes, Class<T> type) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return this.objectMapper.readValue(bytes, type);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}