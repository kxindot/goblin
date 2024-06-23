package com.kxindot.goblin.codec;

/**
 * Zip压缩处理器。
 * 
 * @author ZhaoQingJiang
 */
public interface Zip extends Compresser<Zip> {
	
	/**
	 * 压缩文件后缀: .zip
	 */
	String EXSTENSION = ".zip";

	/**
	 * 设置压缩等级.
	 * 
	 * @param level 压缩级别[0-9],数字越大文件越小,压缩效率越低
	 * @return ZipCompresser
	 * @throws IllegalArgumentException 当输入值不在[0,9]之间时抛出
	 */
	Zip level(int level);
	
	/**
	 * 设置压缩算法。
	 * 
	 * @param algorithm {@link ZipAlgorithm}
	 * @return ZipCompresser
	 */
	Zip algorithm(ZipAlgorithm algorithm);
	
	/**
	 * 设置压缩文件注释。
	 * 
	 * @param comment 注释
	 * @return ZipCompresser
	 * @throws NullPointerException 若comment==null，则抛出此异常
	 */
	Zip comment(CharSequence comment);
	
}
