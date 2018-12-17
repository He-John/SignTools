package com.ky;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.security.MessageDigest;


public class KYSignUtil {
	/**
	 * 快银分配
	 */
	public static String ua = "SXFQ20181217";
	/**
	 * 快银分配
	 */
	public static String key = "1231sdfasdfqwer34234faasfdasfasdf";
	/**
	 * 生成请求签名
	 * 
	 * @param args
	 *            接口请求参数; JSON格式字符串
	 * @return 请求签名
	 */
	public static String getRequestSign(String ua, String key,  String args) {
		if (ua == null || ua.trim().length() == 0) {
			throw new RuntimeException("参数 ua 非法，原因：不可以为空值");
		}
		if (key == null || key.trim().length() == 0) {
			throw new RuntimeException("参数 key 非法，原因：不可以为空值");
		}
		if (args == null || args.trim().length() == 0) {
			throw new RuntimeException("参数 args 非法，原因：不可以为空值");
		}
		/**
		 * 拼接生成signKey
		 */
		String signKey = ua + key + ua;
		/**
		 * MD5哈希生成签名
		 */
		String sign = getMD5(signKey + args + signKey);
		return sign;
	}

	/**
	 * 验证请求签名
	 * @param args
	 *            接口请求参数; JSON格式字符串
	 * @param sign
	 *            接口请求签名
	 * @return 是否匹配
	 */
	public static boolean authRequstSign(String ua, String key, String args, String sign) {
		if (ua == null || ua.trim().length() == 0) {
			throw new RuntimeException("参数 ua 非法，原因：不可以为空值");
		}
		if (key == null || key.trim().length() == 0) {
			throw new RuntimeException("参数 key 非法，原因：不可以为空值");
		}
		if (args == null || args.trim().length() == 0) {
			throw new RuntimeException("参数 args 非法，原因：不可以为空值");
		}
		if (sign == null || sign.trim().length() == 0) {
			throw new RuntimeException("参数 sign 非法，原因：不可以为空值");
		}
		String okSign = getRequestSign(ua, key, args);
		return sign.equalsIgnoreCase(okSign);
	}

	/**
	 * 生成md5
	 * 
	 * @param message
	 * @return
	 */
	public static String getMD5(String message) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes("UTF-8");

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str.toLowerCase();
	}

	/**
	 * 二进制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString();
	}
	
	public static void main(String[] args) {
		String sign = getRequestSign(ua, key, "{\r\n" + 
				"	\"args\": {\r\n" + 
				"		\"user_name\": \"刘先森\",\r\n" + 
				"		\"user_phone\": \"13245678***\",\r\n" + 
				"		\"user_idcard\": \"610121190001011212\"\r\n" + 
				"	}\r\n" + 
				"}");
		System.out.println("签名生成:" + sign);
		System.out.println("验证签名");
		boolean flag = authRequstSign(ua, key, "{\r\n" + 
				"	\"args\": {\r\n" + 
				"		\"user_name\": \"刘先森\",\r\n" + 
				"		\"user_phone\": \"13245678***\",\r\n" + 
				"		\"user_idcard\": \"610121190001011212\"\r\n" + 
				"	}\r\n" + 
				"}", sign);
		System.out.println("验签结果:" + flag);
		
	}

}

