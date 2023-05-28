package com.kxindot.goblin.codec.symmetric;

/**
 * 对称加密算法枚举
 * @author ZhaoQingJiang
 * @date 2017-11-08
 */
public enum Algorithm {


	AES_128("AES", 128), 
	
	AES_192("AES", 192),
	
	AES_256("AES", 256), 
	
	DES("DES", 56);
	
	
	/**
	 * 算法名称
	 */
	private String alg;
	
	/**
	 * 密钥长度
	 */
	private int len;
	
	
	private Algorithm(String alg, int len) {
		this.alg = alg;
		this.len = len;
	}
	
	
	public String getAlgName() {
		return alg;
	}
	
	
	public int getKeyLength() {
		return len;
	}

	
}
