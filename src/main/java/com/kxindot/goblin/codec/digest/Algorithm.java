package com.kxindot.goblin.codec.digest;

/**
 * 摘要算法枚举
 * @author ZhaoQingJiang
 * @date 2017-10-30
 */
public enum Algorithm {

	
	MD5("MD5"), SHA1("SHA1"), SHA224("SHA-224"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512");
	
	
	private String alg;
	
	
	private Algorithm(String alg) {
		this.alg = alg;
	}
	
	
	
	@Override
	public String toString() {
		return alg;
	}
	
}
