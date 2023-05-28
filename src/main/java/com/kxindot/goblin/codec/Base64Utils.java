package com.kxindot.goblin.codec;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>Base64加密/解密算法工具</p>
 * <p>该工具是对apache-commons-codec的封装</p>
 * @author ZhaoQingJiang
 * @date 2017-11-08
 */
public final class Base64Utils {

	/**
	 * 私有构造器,保证类不被实例化
	 */
	private Base64Utils() {}
	
	
	/**
	 * Base64加密
	 * @param plainText 明文字符串
	 * @return 密文字符串
	 */
	public static String encodeToString(final String plainText) {
		return Base64.encodeBase64String(plainText.getBytes());
	}
	
	
	/**
	 * Base64加密
	 * @param plainText 明文byte数组
	 * @return 密文字符串
	 */
	public static String encodeToString(final byte[] plainText) {
		return Base64.encodeBase64String(plainText);
	}
	
	
	/**
	 * Base64加密
	 * @param plainText 明文字符串
	 * @return 密文byte数组
	 */
	public static byte[] encodeToByteArray(final String plainText) {
		return Base64.encodeBase64(plainText.getBytes());
	}
	
	
	/**
	 * Base64加密
	 * @param plainText 明文byte数组
	 * @return 密文byte数组
	 */
	public static byte[] encodeToByteArray(final byte[] plainText) {
		return Base64.encodeBase64(plainText);
	}
	
	
	/**
	 * Base64解密<br>
	 * 若不确定字符串是否为Base64密文,应先使用Base64Coder.isBase64(*)判断后,在进行解密
	 * @param cipherText 密文字符串
	 * @return 明文字符串
	 */
	public static String decodeToString(final String cipherText) {
		return String.valueOf(Base64.decodeBase64(cipherText));
	}
	
	
	/**
	 * Base64解密<br>
	 * 若不确定byte数组是否为Base64密文,应先使用Base64Coder.isBase64(*)判断后,在进行解密
	 * @param cipherText 密文byte数组
	 * @return 明文字符串
	 */
	public static String decodeToString(final byte[] cipherText) {
		return String.valueOf(Base64.decodeBase64(cipherText));
	}
	
	
	/**
	 * Base64解密<br>
	 * 若不确定字符串是否为Base64密文,应先使用Base64Coder.isBase64(*)判断后,在进行解密
	 * @param cipherText 密文字符串
	 * @return 明文byte数组
	 */
	public static byte[] decodeToByteArray(final String cipherText) {
		return Base64.decodeBase64(cipherText);
	}
	
	
	/**
	 * Base64解密<br>
	 * 若不确定byte数组是否为Base64密文,应先使用Base64Coder.isBase64(*)判断后,在进行解密
	 * @param cipherText 明文byte数组
	 * @return 明文byte数组
	 */
	public static byte[] decodeToByteArray(final byte[] cipherText) {
		return Base64.decodeBase64(cipherText);
	}
	
	
	/**
	 * 是否为Base64加密后的字符串
	 * @param text 待验证字符串
	 * @return true/false
	 */
	public static boolean isBase64(final String text) {
		return Base64.isBase64(text);
	}
	
	
	/**
	 * 是否为Base64加密后的byte数组
	 * @param text 待验证byte数组
	 * @return true/false
	 */
	public static boolean isBase64(final byte[] text) {
		return Base64.isBase64(text);
	}
}
