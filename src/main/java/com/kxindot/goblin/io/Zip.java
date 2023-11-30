package com.kxindot.goblin.io;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Objects.stringJoinWith;
import static com.kxindot.goblin.Resources.UUID;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.threx;
import static java.io.File.separator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.kxindot.goblin.EnumValue;

/**
 * 压缩工具,提供.zip,.gzip,.rar格式等定制化压缩.
 * 
 * @author ZhaoQingJiang
 */
public class Zip {
	
	/**
	 * 压缩文件后缀: .zip
	 */
	public static final String EXSTENSION = ".zip";
	
	/** 关闭标志位 */
	private boolean closed;
	/** 缓冲区 */
	private byte[] buf;
	/** 压缩级别[0-9],数字越大文件越小,压缩效率越低 */
	private int level = -1;
	/** 输出编码 */
	private Charset charset;
	/** 压缩算法 */
	private ZipMethod method;
	/** 是否覆盖 */
	private boolean override;
	/** 注释 */
	private String comment;
	/** 压缩文件名 */
	private String fileName;
	/** 压缩文件输出目录 */
	private Path directory;
	/** 压缩文件输出目录不存在时,是否支持自动创建 */
	private boolean creatable;
	/** 源文件 */
	private Map<Path, Object> sources;
	
	/**
	 * 使用默认缓冲区,大小64K
	 */
	public Zip() {
		this(64 * 1024);
	}
	
	/**
	 * 此构造器可指定zip对象在压缩过程中使用的缓冲区大小
	 * 
	 * @param bufSize 缓冲区大小
	 */
	public Zip(int bufSize) {
		requireTrue(bufSize > 0, "缓冲区大小必须大于0!");
		this.buf = new byte[bufSize];
		this.override = true;
		this.closed = false;
		this.sources = newLinkedHashMap();
	}
	
	/**
	 * 设置压缩等级.
	 * 
	 * @param level 压缩级别[0-9],数字越大文件越小,压缩效率越低
	 * @return Zip
	 * @throws IllegalArgumentException 当输入值不在[0,9]之间时抛出
	 */
	public Zip level(int level) {
		requireTrue(level >= 0 && level <= 9, "压缩等级取值: [0,9]");
		this.level = level;
		return this;
	}
	
	/**
	 * 设置字符串编码.
	 * 
	 * @param charset Charset
	 * @return Zip
	 */
	public Zip charset(Charset charset) {
		this.charset = charset;
		return this;
	}
	
	/**
	 * 设置压缩算法.
	 * 
	 * @param method ZipMethod
	 * @return Zip
	 */
	public Zip method(ZipMethod method) {
		this.method = requireNotNull(method, "method == null");
		return this;
	}
	
	/**
	 * 设置是否覆盖已存在的压缩文件.
	 * 
	 * @param override boolean
	 * @return Zip
	 */
	public Zip override(boolean override) {
		this.override = override;
		return this;
	}
	
	/**
	 * 设置压缩文件注释.
	 * 
	 * @param comment String
	 * @return Zip
	 */
	public Zip comment(String comment) {
		this.comment = comment;
		return this;
	}
	
	/**
	 * 设置压缩文件输出文件夹.
	 * 若文件夹不存在,则创建此文件夹.
	 * 
	 * @param directory String
	 * @return Zip
	 */
	public Zip directory(String directory) {
		return directory(directory, true);
	}
	
	/**
	 * 设置压缩文件输出文件夹.
	 * 若文件夹不存在,则创建此文件夹.
	 * 
	 * @param directory Path
	 * @return Zip
	 */
	public Zip directory(Path directory) {
		return directory(directory, true);
	}
	
	/**
	 * 设置压缩文件输出文件夹.
	 * 
	 * @param directory String
	 * @param creatable 是否可创建文件夹
	 * @return Zip
	 */
	public Zip directory(String directory, boolean creatable) {
		return isNull(directory) ? this : directory(Paths.get(directory));
	}
	
	/**
	 * 设置压缩文件输出文件夹.
	 * 
	 * @param directory Path
	 * @param creatable 是否可创建文件夹
	 * @return
	 */
	public Zip directory(Path directory, boolean creatable) {
		if (isNotNull(directory)) {
			this.directory = directory;
		}
		return this;
	}
	
	public Zip fileName(String fileName) {
		this.fileName = requireNotBlank(fileName, "压缩文件文件名不能为空!");
		return this;
	}
	
	/**
	 * 添加文件或文件夹.
	 * 
	 * @param path String
	 * @return Zip
	 * @throws IOException 当zip已关闭时抛出
	 */
	public Zip add(String path) {
		return add(Paths.get(path));
	}
	
	/**
	 * 添加文件或文件夹.
	 * 
	 * @param file File
	 * @return Zip
	 * @throws IOException 当zip已关闭时抛出
	 */
	public Zip add(File file) {
		return add(file.toPath());
	}
	
	/**
	 * 添加待压缩文件或文件夹
	 * @param path Path
	 * @return Zip
	 * @throws IOException 当zip已关闭时抛出
	 */
	public Zip add(Path path) {
		requireNotNull(path, "path == null");
		requireTrue(exists(path), "文件或文件夹不存在: %s", path);
		sources.put(path, path);
		return this;
	}
	
	/**
	 * 添加输入流,随机生成文件名.
	 * 
	 * @param inputStream InputStream
	 * @return Zip
	 * @throws IOException 当zip已关闭时抛出
	 */
	public Zip add(InputStream inputStream) {
		return add(UUID(), inputStream);
	}
	
	/**
	 * 添加输入流.
	 * 
	 * @param fileName 文件名
	 * @param inputStream InputStream
	 * @return Zip
	 * @throws IOException 当zip已关闭时抛出
	 */
	public Zip add(String fileName, InputStream inputStream) {
		requireNotBlank(fileName, "文件名不能为空!");
		sources.put(Paths.get(fileName), inputStream);
		return this;
	}
	
	/**
	 * 创建压缩文件.
	 */
	public void zip() {
		String name = fileName;
		if (isNull(name)) {
			threx(IIOException::new, "压缩文件名不能为空!");
		} else if (!name.endsWith(EXSTENSION)) {
			name += EXSTENSION;
		}
		
		if (isNotNull(directory)) {
			if (!exists(directory)) {
				if (!creatable) {
					threx(IIOException::new, "文件夹不存在: %s", directory);
				} else if (!directory.isAbsolute()) {
					threx(IIOException::new, "不能创建相对路径的文件夹: %s", directory);
				}
				mkDirs(directory);
			}
			if (!isDirectory(directory)) {
				threx(IIOException::new, "不是一个文件夹: %s", directory);
			}
			name = stringJoinWith(separator, directory.toString(), name);
		}
		Path file = Paths.get(name);
		if (!file.isAbsolute()) {
			threx(IIOException::new, "压缩文件路径不能为相对路径: %s", file);
		} else if (exists(file) && !override) {
			threx(IIOException::new, "压缩文件已存在: %s", file);
		}
		zip(IO.openOutputStream(file));
	}
	
	/**
	 * 创建压缩文件,并写入输出流.
	 * 
	 * @param outputStream
	 */
	public void zip(OutputStream outputStream) {
		requireNotNull(outputStream, "outputStream == null");
		if (closed) {
			threx(IIOException::new, "Zip已关闭!");
		}
		try (ZipOutputStream out = isNull(charset) 
				? new ZipOutputStream(outputStream) 
						: new ZipOutputStream(outputStream, charset)) {
			if (level != -1) {
				out.setLevel(level);
			}
			if (method != null) {
				out.setMethod(method.value());
			}
			if (comment != null) {
				out.setComment(comment);
			}
			for (Map.Entry<Path, Object> entry : sources.entrySet()) {
				Path key = entry.getKey();
				Object val = entry.getValue();
				if (val instanceof Path) {
					zip(null, Path.class.cast(val).toFile(), out);
				} else {
					zip(key.toString(), (InputStream) val, out);
				}
			}
		} catch (IOException e) {
			threx(IIOException::new, e);
		}
		this.closed = true;
	}
	
	/**
	 * 文件类压缩核心方法,递归压缩
	 */
	private void zip(String parent, File file, ZipOutputStream out) throws IOException {
		String name = isNull(parent) ? file.getName() : stringJoinWith(separator, parent, file.getName());
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children.length != 0) {
				for (File child : children) {
					zip(name, child, out);
				}
			} else {
				out.putNextEntry(new ZipEntry(name + separator));
				out.closeEntry();
			}
		} else {
			try (BufferedInputStream in = 
					new BufferedInputStream(new FileInputStream(file))) {
				zip(name, in, out);
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	/**
	 * 输入流类压缩核心方法
	 */
	private void zip(String name, InputStream in, ZipOutputStream out) throws IOException {
		int i;
		out.putNextEntry(new ZipEntry(name));
		while ((i = in.read(buf)) != -1) {
			out.write(buf, 0, i);
		}
		in.close();
		out.closeEntry();
	}
	
	/**
	 * 压缩算法: stored, deflated
	 * 
	 * @author ZhaoQingJiang
	 */
	public enum ZipMethod implements EnumValue<Integer> {
		
		STORED(0), 
		
		DEFLATED(8);
		
		private Integer value;

		private ZipMethod(Integer value) {
			this.value = value;
		}

		@Override
		public Integer value() {
			return value;
		}
	}
}