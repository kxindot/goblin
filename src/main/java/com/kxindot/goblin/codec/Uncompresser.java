package com.kxindot.goblin.codec;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * 压缩文件解压器。
 * 
 * @author ZhaoQingJiang
 */
public interface Uncompresser<T extends Uncompresser<T>> {
	
	/**
	 * 设置解压缓冲区大小。
	 * 
	 * @param size int
	 * @return T
	 * @throws IllegalArgumentException 若size小于等于0，则抛出此异常
	 */
	T buffer(int size);
	
	/**
	 * 设置待解压文件。
	 * 
	 * @param path 待解压文件
	 * @return T
	 * @throws IllegalArgumentException 若待解压文件不存在，则抛出此异常
	 */
	T set(Path path);
	
	/**
	 * 设置待解压文件。
	 * 
	 * @param file 待解压文件
	 * @return T
	 * @throws IllegalArgumentException 若待解压文件不存在，则抛出此异常
	 */
	T set(File file);

	/**
	 * 设置压缩文件解压输出文件夹路径。若文件夹不存在，则文件夹将会被自动创建。
	 * 
	 * @param directory 输出文件夹路径
	 * @return T
	 * @throws IllegalArgumentException 输出文件夹路径为null或不是一个文件夹，则抛出此异常
	 */
	T destination(Path directory);

	/**
	 * 设置压缩文件解压输出文件夹路径。若文件夹不存在，则会根据creatable判定是否自动创建。
	 * 
	 * @param directory 输出文件夹路径
	 * @param creatable 当输出文件夹不存在，是否可以自动创建
	 * @return T
	 * @throws IllegalArgumentException 输出文件夹路径为null或不是一个文件夹，则抛出此异常
	 */
	T destination(Path directory, boolean creatable);
	
	/**
	 * 解压缩。若解压后的文件在磁盘上已存在，则默认覆盖。
	 * 
	 * @return 压缩文件解压后输出的文件列表
	 */
	List<Path> uncompress();
	
	/**
	 * 解压缩。
	 * 
	 * @param override 解压后的文件是否覆盖已存在的文件
	 * @return 压缩文件解压后输出的文件列表
	 */
	List<Path> uncompress(boolean override);
	
}
