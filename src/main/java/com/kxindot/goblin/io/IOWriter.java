package com.kxindot.goblin.io;

import java.io.Reader;

/**
 * IO: {@link Writer}包装
 * 
 * @author ZhaoQingJiang
 */
public interface IOWriter extends IO {

	/**
	 * 设置缓冲区大小
	 * @param size 缓冲区大小
	 * @return IOWriter
	 */
	IOWriter buf(int size);
	
	/**
	 * 将数据{@code content}写入本输出字符流{@link IOWriter}.
	 * 注意: 写入完成后并未关闭输入及输出字符流,需调用{@link #close()}方法关闭.
	 * @param content CharSequence
	 * @return IOWriter
	 */
	IOWriter write(CharSequence content);
	
	/**
	 * 将数据{@code content}写入本输出字符流{@link IOWriter}.
	 * 注意: 写入完成后并未关闭输入及输出字符流,需调用{@link #close()}方法关闭.
	 * @param content {@code byte[]}
	 * @return IOWriter
	 */
	IOWriter write(byte[] content);
	
	/**
	 * 读取输入字符流{@code reader}中的数据并写入本输出字符流{@link IOWriter}.
	 * 注意: 写入完成后并未关闭输入及输出流,需调用{@link close()}方法关闭.
	 * @param reader Reader
	 * @return IO
	 */
	IO read(Reader reader);
}
