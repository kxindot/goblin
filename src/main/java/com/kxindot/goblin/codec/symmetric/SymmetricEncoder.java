package com.kxindot.goblin.codec.symmetric;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.kxindot.goblin.codec.Base64Utils;
import com.kxindot.goblin.logger.Logger;
import com.kxindot.goblin.logger.LoggerFactory;


/**
 * 对称算法加密/解密工具类
 * @author ZhaoQingJiang
 * @date 2017-11-08
 */
public final class SymmetricEncoder {
    
    private static Logger log = LoggerFactory.getLogger(SymmetricEncoder.class);
    
	/**
	 * 私有构造器,保证类不被实例化
	 */
	private SymmetricEncoder() {}
	
	
	/**
	 * 对称算法加密<br>
	 * @param secretKey 密钥字符串(用户持有)
	 * @param plainText 明文字符串
	 * @param algorithm 对称算法
	 * @return 若加密过程中出现异常,会返回null,否则返回密文字符串
	 */
	public static String encode(final String secretKey, final String plainText, final Algorithm algorithm) {
		return encode(secretKey.getBytes(), plainText, algorithm);
	}
	
	
	/**
	 * 对称算法加密<br>
	 * @param secretKey 密钥byte数组(用户持有)
	 * @param plainText 明文字符串
	 * @param algorithm 对称算法
	 * @return 若加密过程中出现异常,会返回null,否则返回密文字符串
	 */
	public static String encode(final byte[] secretKey, final String plainText, final Algorithm algorithm) {
		try {
			//转化为UTF-8编码格式,防止中文乱码
			return encode(secretKey, plainText.getBytes("UTF-8"), algorithm);
		} catch (UnsupportedEncodingException e) {
			log.error("plainText can't encoding to UTF-8", e);
		}
		
		return null;
	}
	
	
	/**
	 * 对称算法加密<br>
	 * @param secretKey 密钥字符串(用户持有)
	 * @param plainText 明文byte数组
	 * @param algorithm 对称算法
	 * @return 若加密过程中出现异常,会返回null,否则返回密文字符串
	 */
	public static String encode(final String secretKey, final byte[] plainText, final Algorithm algorithm) {
		return encode(secretKey.getBytes(), plainText, algorithm);
	}
	
	
	/**
	 * 对称算法加密<br>
	 * @param secretKey 密钥byte数组(用户持有)
	 * @param plainText 明文byte数组
	 * @param algorithm 对称算法
	 * @return 若加密过程中出现异常,会返回null,否则返回密文字符串
	 */
	public static String encode(final byte[] secretKey, final byte[] plainText, final Algorithm algorithm) {
		Cipher cipher;
		byte [] byteArray;
		try {
			cipher = initializeCipher(Cipher.ENCRYPT_MODE, algorithm, secretKey);
			byteArray = cipher.doFinal(plainText);
	        return Base64Utils.encodeToString(byteArray);
		} catch (InvalidKeyException | NoSuchPaddingException 
				| IllegalBlockSizeException | BadPaddingException 
				|NoSuchAlgorithmException e) {
			log.error(algorithm.getAlgName() + " encoding failure, null will be return", e);
		}
        
		return null;
	}
	
	
	/**
	 * 对称算法解密<br>
	 * @param secretKey 密钥字符串(用户持有)
	 * @param cipherText 密文字符串
	 * @param algorithm 对称算法
	 * @return 若解密过程中出现异常,会返回null,否则返回明文字符串
	 */
	public static String decode(final String secretKey, final String cipherText, final Algorithm algorithm) {
		return decode(secretKey.getBytes(), cipherText, algorithm);
	}
	
	
	/**
	 * 对称算法解密<br>
	 * @param secretKey 密钥byte数组(用户持有)
	 * @param cipherText 密文字符串
	 * @param algorithm 对称算法
	 * @return 若解密过程中出现异常,会返回null,否则返回明文字符串
	 */
	public static String decode(final byte[] secretKey, final String cipherText, final Algorithm algorithm) {
		return decode(secretKey, cipherText.getBytes(), algorithm);
	}
	
	
	/**
	 * 对称算法解密<br>
	 * @param secretKey 密钥字符串(用户持有)
	 * @param cipherText 密文byte数组
	 * @param algorithm 对称算法
	 * @return 若解密过程中出现异常,会返回null,否则返回明文字符串
	 */
	public static String decode(final String secretKey, final byte[] cipherText, final Algorithm algorithm) {
		return decode(secretKey.getBytes(), cipherText, algorithm);
	}
	
	
	/**
	 * 对称算法解密<br>
	 * @param secretKey 密钥byte数组(用户持有)
	 * @param cipherText 密文byte数组
	 * @param algorithm 对称算法
	 * @return 若解密过程中出现异常,会返回null,否则返回明文字符串
	 */
	public static String decode(final byte[] secretKey, final byte[] cipherText, final Algorithm algorithm) {
		Cipher cipher;
		byte [] byteArray;
		try {
			cipher = initializeCipher(Cipher.DECRYPT_MODE, algorithm, secretKey);
			byteArray = cipher.doFinal(Base64Utils.decodeToByteArray(cipherText));
			return new String(byteArray);
		} catch (InvalidKeyException | NoSuchAlgorithmException 
				| NoSuchPaddingException | IllegalBlockSizeException 
				| BadPaddingException e) {
			log.error(algorithm.getAlgName() + " encoding failure, the cipherText will be return", e);
		}
		
		return null;
	}
	
	
	/**
	 * 初始化密码器
	 * @param mode 密码器模式(加密/解密)
	 * @param alg 对称算法类型
	 * @param encodeKey 加密密钥(用户持有)
	 * @return 密码器
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	private static Cipher initializeCipher(int mode, Algorithm alg, byte[] encodeKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		//构造密钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(alg.getAlgName());
        //根据密钥规则初始化密钥生成器
        keyGenerator.init(alg.getKeyLength(), getSecureRandom(encodeKey));
        //产生原始对称密钥
        SecretKey originalKey = keyGenerator.generateKey();
        //获得原始对称密钥的字节数组
        byte [] originalKeyBs = originalKey.getEncoded();
        //根据字节数组生成密钥
        SecretKey secretKey = new SecretKeySpec(originalKeyBs, alg.getAlgName());
        //根据指定算法生成密码器
        Cipher cipher = Cipher.getInstance(alg.getAlgName());
        //初始化密码器，mode = Cipher.Encrypt_mode(加密) 或 mode = Cipher.Decrypt_mode(解密)
        cipher.init(mode, secretKey);
        
        return cipher;
	}
	
	
	/**
	 * 获取SecureRandom实例
	 * @param seed 随机种子
	 * @return SecureRandom Instance
	 */
	private static SecureRandom getSecureRandom(byte[] seed) {
		return new SecureRandom(seed);
	}
	
}
