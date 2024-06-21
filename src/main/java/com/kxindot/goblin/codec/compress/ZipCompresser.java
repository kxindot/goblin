package com.kxindot.goblin.codec.compress;

/**
 * Zip压缩处理器。
 * 
 * @author ZhaoQingJiang
 */
public interface ZipCompresser extends Compresser<ZipCompresser> {

	/**
	 * 设置压缩等级.
	 * 
	 * @param level 压缩级别[0-9],数字越大文件越小,压缩效率越低
	 * @return ZipCompresser
	 * @throws IllegalArgumentException 当输入值不在[0,9]之间时抛出
	 */
	ZipCompresser level(int level);
	
	/**
	 * 设置压缩算法。
	 * 
	 * @param algorithm {@link ZipAlgorithm}
	 * @return ZipCompresser
	 */
	ZipCompresser algorithm(ZipAlgorithm algorithm);
	
	/**
	 * 设置压缩文件注释。
	 * 
	 * @param comment 注释
	 * @return ZipCompresser
	 * @throws NullPointerException 若comment==null，则抛出此异常
	 */
	ZipCompresser comment(CharSequence comment);
	
}
