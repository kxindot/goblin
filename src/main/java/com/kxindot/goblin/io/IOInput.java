package com.kxindot.goblin.io;

import java.io.OutputStream;

/**
 * IO: {@link InputStream}包装
 * 
 * @author ZhaoQingJiang
 */
public interface IOInput extends IO {
	
	/**
	 * 设置缓冲区大小
	 * @param size 缓冲区大小
	 * @return IOInput
	 */
	IOInput buf(int size);
	
	/**
	 * 将本输入流{@link IOInput}中的数据写入{@code outputStream}.
	 * 注意: 写入完成后并未关闭输入及输出流,需调用{@link #close()}方法关闭.
	 * @param outputStream OutputStream
	 * @return IO
	 */
    IO write(OutputStream outputStream);
    
    /**
     * 读取本输入流{@link IOInput}中的数据,返回其字符串(使用默认编码).
     * @return String
     */
    String readString();
    
    /**
     * 读取本输入流{@link IOInput}中的数据,返回其字节数组.
     * @return byte[]
     */
    byte[] readBytes();
}