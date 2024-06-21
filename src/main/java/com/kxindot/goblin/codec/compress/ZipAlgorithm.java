package com.kxindot.goblin.codec.compress;

import com.kxindot.goblin.EnumValue;

/**
 * 压缩算法: stored, deflated
 * 
 * @author ZhaoQingJiang
 */
public enum ZipAlgorithm implements EnumValue<Integer>, CompressAlgorithm<Integer> {
	
	STORED(0), 
	
	DEFLATED(8);
	
	private Integer value;

	private ZipAlgorithm(Integer value) {
		this.value = value;
	}

	@Override
	public Integer value() {
		return value;
	}
}