package com.kxindot.goblin.io;

import static com.kxindot.goblin.Throws.silentThrex;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import org.apache.commons.io.output.FileWriterWithEncoding;

/**
 * IO便捷工具。
 * TODO 从BIO实现替换为NIO实现
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
	
	/**
	 * 获取文件输入字符流.
	 * 
	 * @param file File
	 * @return Reader
	 */
	public static Reader openReader(File file) {
		Reader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			silentThrex(e);
		}
		return reader;
	}
	
	/**
	 * 获取文件输入字符流.
	 * 
	 * @param file File
	 * @return Reader
	 */
	public static Reader openReader(Path file) {
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(file);
		} catch (IOException e) {
			silentThrex(e);
		}
		return reader;
	}
	
	/**
	 * 获取文件输入字符流.
	 * 
	 * @param file File
	 * @return Reader
	 */
	public static Reader openReader(Path file, Charset charset) {
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(file, charset);
		} catch (IOException e) {
			silentThrex(e);
		}
		return reader;
	}
	
	/**
	 * 获取文件输出字符流.
	 * 
	 * @param file Path
	 * @return Writer
	 */
	public static Writer openWriter(File file) {
		Writer writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			silentThrex(e);
		}
		return writer;
	}
	
	/**
	 * 获取文件输出字符流.
	 * 
	 * @param file Path
	 * @return Writer
	 */
	public static Writer openWriter(Path file) {
		Writer writer = null;
		try {
			writer = Files.newBufferedWriter(file);
		} catch (IOException e) {
			silentThrex(e);
		}
		return writer;
	}
	
	/**
	 * 获取文件输出字符流.
	 * 
	 * @param file Path
	 * @return Writer
	 */
	public static Writer openWriter(File file, Charset charset) {
		Writer writer = null;
		try {
			writer = new FileWriterWithEncoding(file, charset);
		} catch (IOException e) {
			silentThrex(e);
		}
		return writer;
	}
	
	/**
	 * 获取文件输出字符流.
	 * 
	 * @param file Path
	 * @return Writer
	 */
	public static Writer openWriter(Path file, Charset charset) {
		Writer writer = null;
		try {
			writer = Files.newBufferedWriter(file, charset);
		} catch (IOException e) {
			silentThrex(e);
		}
		return writer;
	}
	
	/**
	 * 获取文件输入流.
	 * 
	 * @param file File
	 * @return InputStream
	 */
	public static InputStream openInputStream(File file) {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			silentThrex(e);
		}
		return in;
	}

	/**
	 * 获取文件输入流
	 * 
	 * @param path Path
	 * @return InputStream
	 */
	public static InputStream openInputStream(Path path) {
		InputStream in = null;
		try {
			in = Files.newInputStream(path);
		} catch (IOException e) {
			silentThrex(e);
		}
		return in;
	}
	
	/**
	 * 获取文件输入流.
	 * 
	 * @param path Path
	 * @param options OpenOption[]
	 * @return InputStream
	 */
	public static InputStream openInputStream(Path path, OpenOption... options) {
		InputStream in = null;
		try {
			in = Files.newInputStream(path, options);
		} catch (IOException e) {
			silentThrex(e);
		}
		return in;
	}
	
	/**
	 * 获取文件输出流.
	 * 
	 * @param path Path
	 * @return OutputStream
	 */
	public static OutputStream openOutputStream(Path path) {
		OutputStream out = null;
		try {
			out = Files.newOutputStream(path);
		} catch (IOException e) {
			silentThrex(e);
		}
		return out;
	}
	
	/**
	 * 获取文件输出流.
	 * 
	 * @param path Path
	 * @param options OpenOption[]
	 * @return OutputStream
	 */
	public static OutputStream openOutputStream(Path path, OpenOption... options) {
		OutputStream out = null;
		try {
			out = Files.newOutputStream(path, options);
		} catch (IOException e) {
			silentThrex(e);
		}
		return out;
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
