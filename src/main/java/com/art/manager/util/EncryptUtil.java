package com.art.manager.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class EncryptUtil {

	private EncryptUtil(){}
	private static BCryptPasswordEncoder encoder;

	static{
		encoder = new BCryptPasswordEncoder();
	}

	/**
	 * 对密码加密处理
	 * @param str
	 * @return
	 */
	public static String encode(String str){

		return encoder.encode(str);
	}

	/**
	 * 判断原始密码和加密后的密码是否一致
	 * @param str
	 * @param encode
	 * @return
	 */
	public static boolean matches(String str,String encode){

		return encoder.matches(str,encode);
	}
}
