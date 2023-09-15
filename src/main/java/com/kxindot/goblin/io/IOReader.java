package com.kxindot.goblin.io;

import java.io.Writer;

/**
 * IO: {@link Reader}包装
 * 
 * @author ZhaoQingJiang
 */
public interface IOReader extends IO {

	
	/**
	 * 设置缓冲区大小
	 * @param size 缓冲区大小
	 * @return IOReader
	 */
	IOReader buf(int size);
	
	/**
	 * 将本字符流{@link IOReader}中的字符数据写入{@code writer}.
	 * 注意: 写入完成后并未关闭输入及输出字符流,需调用{@link #close()}方法关闭.
	 * @param writer Writer
	 * @return IO
	 */
	IO write(Writer writer);
	
	/**
	 * 读取本字符流{@link IOReader}中的字符数据.
	 * @return String
	 */
	String read();
	
}
