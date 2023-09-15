package com.kxindot.goblin.io;

import java.io.InputStream;

/**
 * IO: {@link OutputStream}包装
 * 
 * @author ZhaoQingJiang
 */
public interface IOOutput extends IO {
    
	/**
	 * 设置缓冲区大小
	 * @param size 缓冲区大小
	 * @return IOInput
	 */
	IOOutput buf(int size);
	
	/**
	 * 将数据{@code content}写入本输出流{@link IOOutput}.
	 * 注意: 写入完成后并未关闭输入及输出流,需调用{@link close()}方法关闭.
	 * @param content CharSequence
	 * @return IOOutput
	 */
	IOOutput write(CharSequence content);
	
	/**
	 * 将数据{@code content}写入本输出流{@link IOOutput}.
	 * 注意: 写入完成后并未关闭输入及输出流,需调用{@link close()}方法关闭.
	 * @param content {@code byte[]}
	 * @return IOOutput
	 */
	IOOutput write(byte[] content);
	
	/**
	 * 读取输入流{@code inputStream}中的数据并写入本输出流{@link IOOutput}.
	 * 注意: 写入完成后并未关闭输入及输出流,需调用{@link close()}方法关闭.
	 * @param inputStream InputStream
	 * @return IO
	 */
	IO read(InputStream inputStream);
}