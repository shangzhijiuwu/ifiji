package me.iszhenyu.ifiji.web.config.security;

import javax.servlet.Filter;
import java.util.Map;

/**
 * Created by xiaoz on 2017/5/9.
 */
public interface ShiroFilterCustomizer {
	Map<String, Filter> customize(Map<String, Filter> filterMap);
}
