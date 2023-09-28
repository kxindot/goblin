package com.kxindot.goblin.typeconvert.impl;

/**
 * @author ZhaoQingJiang
 */
public class CharSequenceToBooleanConverter extends CharSequenceConverter<Boolean> {

	@Override
	protected Boolean convertValue(CharSequence p) {
		return Boolean.valueOf(p.toString());
	}

}
