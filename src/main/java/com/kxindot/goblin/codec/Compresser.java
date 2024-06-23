package com.kxindot.goblin.codec;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * 压缩编码器。
 * 
 * @author ZhaoQingJiang
 */
public interface Compresser<T extends Compresser<T>> {
	
	/**
	 * 设置压缩文件名称。
	 * 
	 * @param name 压缩文件名称
	 * @return T
	 * @throws IllegalArgumentException 若名称为空，则抛出此异常
	 */
	T fileName(String name);

	/**
	 * 设置压缩字符串编码。
	 * 
	 * @see Charset#forName(String)
	 * @param charset String
	 * @return T
	 */
	T charset(String charset);

	/**
	 * 设置压缩字符串编码。
	 * 
	 * @param charset Charset
	 * @return T
	 */
	T charset(Charset charset);
	
	/**
	 * 设置压缩缓冲区大小。
	 * 
	 * @param size int
	 * @return T
	 * @throws IllegalArgumentException 若size小于等于0，则抛出此异常
	 */
	T buffer(int size);
	
	/**
	 * 添加待压缩文件或文件夹。
	 * 
	 * @param path 待压缩文件或文件夹
	 * @return T
	 * @throws IllegalArgumentException 若带压缩文件或文件夹不存在，则抛出此异常
	 */
	T add(Path path);
	
	/**
	 * 添加待压缩文件或文件夹。
	 * 
	 * @param file 待压缩文件或文件夹
	 * @return T
	 * @throws IllegalArgumentException 若带压缩文件或文件夹不存在，则抛出此异常
	 */
	T add(File file);
	
	/**
	 * 添加待压缩输入流。随机生成一个文件名并输入流中内容写入此文件。
	 * 
	 * @param inputStream InputStream
	 * @return T
	 * @throws IllegalArgumentException 若inputStream已关闭，则抛出此异常
	 */
	T add(InputStream inputStream);
	
	/**
	 * 添加待压缩输入流，并指定输入流生成的文件名称。
	 * 
	 * @param name 待生成文件名称
	 * @param inputStream InputStream
	 * @return T
	 * @throws IllegalArgumentException 若name为空或inputStream已关闭，则抛出此异常
	 */
	T add(String name, InputStream inputStream);
	
	/**
	 * 设置压缩文件输出文件夹路径。若文件夹不存在，则文件夹将会被自动创建。
	 * 
	 * @param directory 压缩文件输出文件夹路径
	 * @return T
	 * @throws IllegalArgumentException 输出文件夹路径为null或不是一个文件夹，则抛出此异常
	 */
	T destination(Path directory);

	/**
	 * 设置压缩文件输出文件夹路径。若文件夹不存在，则会根据creatable判定是否自动创建。
	 * 
	 * @param directory 压缩文件输出文件夹路径
	 * @param creatable 当输出文件夹不存在，是否可以自动创建
	 * @return T
	 * @throws IllegalArgumentException 输出文件夹路径为null或不是一个文件夹，则抛出此异常
	 */
	T destination(Path directory, boolean creatable);

	/**
	 * 压缩文件至磁盘，返回压缩文件路径。
	 * 
	 * @return 压缩文件
	 */
	Path compress();
	
	/**
	 * 压缩文件至磁盘，返回压缩文件路径。
	 * 若override=true且压缩文件已存在，则会将旧文件覆盖。
	 * 反之若override=false且压缩文件已存在，则直接返回。
	 * 
	 * @param override boolean
	 * @return 压缩文件
	 */
	Path compress(boolean override);
	
	/**
	 * 压缩文件，返回压缩文件字节数据。
	 * 
	 * @return 压缩文件字节数据
	 */
	byte[] compressToBytes();
	
	/**
	 * 压缩文件，返回压缩文件输入流。
	 * 
	 * @return 压缩文件输入流
	 */
	InputStream compressToInputStream();
	
	/**
	 * 压缩文件，并将压缩数据写入输出流。
	 * 
	 * @param outputStream OutputStream
	 */
	void compressTo(OutputStream outputStream);
	
}
