package me.iszhenyu.ifiji.util;

/**
 * Created by xiaoz on 2017/5/21.
 */
public class StringUtils {

	public static final String EMPTY_STRING = "";

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}
