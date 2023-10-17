package com.kxindot.goblin.io;

import static com.kxindot.goblin.Throws.silentThrex;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * IO便捷工具
 * 
 * @author ZhaoQingJiang
 */
public interface IO {
	
	/**
	 * 关闭流.
	 */
	void close();
	
	/**
	 * 创建IOInput对象.
	 * 
	 * @param in InputStream
	 * @return IOInput
	 */
	public static IOInput input(InputStream in) {
		return new IOInputImpl(in);
	}
	
	/**
	 * 创建IOReader对象.
	 * 
	 * @param reader Reader
	 * @return IOReader
	 */
	public static IOReader input(Reader reader) {
		return new IOReaderImpl(reader);
	}
	
	/**
	 * 创建IOOutput对象.
	 * 
	 * @param out OutputStream
	 * @return IOOutput
	 */
	public static IOOutput output(OutputStream out) {
		return out instanceof PrintStream 
				? new IOPrintOutputImpl(PrintStream.class.cast(out)) 
						: new IOOuputImpl(out);
	}
	
	/**
	 * 创建IOWriter对象.
	 * 
	 * @param out Writer
	 * @return IOWriter
	 */
	public static IOWriter output(Writer writer) {
		return new IOWriterImpl(writer);
	}
	
	/**
	 * 获取URI输入流.
	 * 
	 * @param uri URI
	 * @return InputStream
	 */
	public static InputStream open(URI uri) {
		InputStream in = null;
		try {
			in = uri.toURL().openStream();
		} catch (IOException e) {
			silentThrex(e);
		}
		return in;
	}
	
	/**
	 * 获取URL输入流.
	 * 
	 * @param url URL
	 * @return InputStream
	 */
	public static InputStream open(URL url) {
		InputStream in = null;
		try {
			in = url.openStream();
		} catch (IOException e) {
			silentThrex(e);
		}
		return in;
	}
	
	
	public static InputStream open(File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			silentThrex(e);
		}
		return in;
	}

	
	public static InputStream open(Path path) {
		InputStream in = null;
		try {
			in = Files.newInputStream(path);
		} catch (IOException e) {
			silentThrex(e);
		}
		return in;
	}
	
	/**
	 * 便捷方法: 调用{@link Closeable#close()}.
	 * 
	 * @param closeable Closeable
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				silentThrex(e);
			}
		}
	}
	
	/**
	 * 便捷方法: 调用{@link Flushable#flush()}.
	 * 
	 * @param flushable Flushable
	 */
	public static void flush(Flushable flushable) {
		if (flushable != null) {
			try {
				flushable.flush();
			} catch (IOException e) {
				silentThrex(e);
			}
		}
	}
	
	/**
	 * 便捷方法: 调用{@link InputStream#available()},返回可用字节数.
	 * 
	 * @param inputStream InputStream
	 * @return int
	 */
	public static int available(InputStream  inputStream) {
		int ava = 0;
		try {
			ava = inputStream.available();
		} catch (IOException e) {
			silentThrex(e);
		}
		return ava;
	}
 
}
