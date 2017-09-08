package me.iszhenyu.ifiji.core.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;

import java.lang.reflect.Method;

/**
 * @author zhen.yu
 * @since 2017/9/8
 */
class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return String.format("%s:%s:%s",
                target.getClass().getName(),
                method.getName(),
                generateKey(params).toString()
        );
    }

    private static Object generateKey(Object... params) {
        if (params.length == 0) {
            return SimpleKey.EMPTY;
        }
        if (params.length == 1) {
            Object param = params[0];
            if (param != null && !param.getClass().isArray()) {
                return param;
            }
        }
        return new SimpleKey(params);
    }
}
