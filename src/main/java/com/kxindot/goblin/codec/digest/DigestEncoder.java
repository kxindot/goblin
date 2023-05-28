package com.kxindot.goblin.codec.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 摘要加密算法工具
 * @author ZhaoQingJiang
 * @date 2017-04-25
 * @version 1.0
 * @since 1.8
 */
public final class DigestEncoder {
	
	/**
	 * 十六进制字符
	 */
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	
	/**
	 * 缓冲区大小 1M
	 */
	private static final int BUFFER_SIZE = 1024 * 1024;
	
	/**
	 * 私有构造器,保证类不被实例化
	 */
	private DigestEncoder() {}
	
	
	/**
	 * 摘要算法加密
	 * @param text 明文
	 * @param algorithm 摘要算法
	 * @return 密文
	 * @throws NullPonterException text == null or algorithm == null
	 */
	public static String encode(final String text, final Algorithm algorithm) {
		return toString(encoder(text.getBytes(), algorithm));
	}
	
	
	/**
	 * 摘要算法加密
	 * @param text 明文
	 * @param charset 编码格式
	 * @param algorithm 摘要算法
	 * @return 密文
	 * @throws UnsupportedEncodingException 不支持的编码格式
	 * @throws NullPonterException text == null or charset == null or algorithm == null
	 */
	/*public static String encode(final String text, final String charset, final Algorithm algorithm) throws UnsupportedEncodingException {
		AssertUtils.nonNull(text, "input text == null");
		AssertUtils.nonNull(charset, "charset == null");
		return toString(encoder(StringUtil.switchCharset(text, charset).getBytes(), algorithm));
	}*/
	
	
	/**
	 * 摘要算法加密
	 * @param bytes 明文
	 * @param algorithm 摘要算法
	 * @return 密文
	 * @throws NullPonterException bytes == null or algorithm == null
	 */
	public static String encode(final byte[] bytes, final Algorithm algorithm) {
		return toString(encoder(bytes, algorithm));
	}
	
	
	/**
	 * 摘要加密算法 
	 */
	private static char[] encoder(final byte[] bytes, final Algorithm algorithm) {
		MessageDigest messageDigest = getMessageDigest(algorithm);
		messageDigest.reset();
		messageDigest.update(bytes);
		return toHex(messageDigest.digest());
	}
	
	
	/**
	 * 获取MessageDigest实例
	 */
	private static MessageDigest getMessageDigest(final Algorithm algorithm) {
		try {
			return MessageDigest.getInstance(algorithm.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new NullPointerException(); //不会发生
		}
	}
	
	
	/**
	 * 将字节数组转换为十六进制的字符数组 
	 */
	private static char[] toHex(final byte[] bytes) {
		int j = bytes.length;
		char[] cipherChars = new char[j * 2];
		int k = 0;
		byte byteValue = 0;
		for (int i = 0; i < j; i++) {
			byteValue = bytes[i];
			cipherChars[k++] = HEX_DIGITS[byteValue >>> 4 & 0xf];
			cipherChars[k++] = HEX_DIGITS[byteValue & 0xf];
		}
		return cipherChars; 
	}
	
	
	/**
	 * 摘要算法加密
	 * @param file 文件
	 * @param algorithm 摘要算法
	 * @return 密文
	 * @throws IOException
	 */
	public static String encode(final File file, final Algorithm algorithm) throws IOException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			return toString(encoder(inputStream, algorithm));
		} catch (IOException e) {
			throw e;
		}
	}
	
	
	/**
	 * 摘要算法加密
	 * @param inputStream InputStream
	 * @param algorithm 摘要算法
	 * @return 密文
	 * @throws IOException
	 */
	public static String encode(final InputStream inputStream, final Algorithm algorithm) throws IOException {
		return toString(encoder(inputStream, algorithm));
	}
	

	/**
	 * 大文件摘要加密算法 
	 */
	private static char[] encoder(final InputStream inputStream, final Algorithm algorithm) throws IOException {
		MessageDigest messageDigest = getMessageDigest(algorithm);
		DigestInputStream iStream = new DigestInputStream(inputStream, messageDigest);
		byte[] buffer = new byte[BUFFER_SIZE];
		while (iStream.read(buffer) > 0);
		
		return toHex(iStream.getMessageDigest().digest());
	}
	
	
	/**
	 * 将字符数组转换成字符串
	 */
	private static String toString(char[] array) {
		return String.valueOf(array);
	}
}
