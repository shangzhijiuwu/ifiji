package me.iszhenyu.ifiji.util;

import java.util.regex.Pattern;

/**
 * Created by xiaoz on 2017/5/21.
 */
public class StringUtils {

	public static final String EMPTY_STRING = "";
	private static final String REG_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private static final String REG_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

	public static boolean isEmpty(String str) {
		return str == null || EMPTY_STRING.equals(str.trim());
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isMobile(String mobileNumber) {
		return isNotEmpty(mobileNumber) && Pattern.compile(REG_MOBILE).matcher(mobileNumber).matches();
	}

	public static boolean isEmail(String email) {
		return isNotEmpty(email) && Pattern.compile(REG_EMAIL).matcher(email).matches();
	}
}
