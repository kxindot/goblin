package com.kxindot.goblin.codec;

import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Objects.stringJoinWith;
import static com.kxindot.goblin.Throws.threx;
import static java.io.File.separator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.kxindot.goblin.io.IIOException;

/**
 * Zip文件格式压缩工具。
 * 
 * @author ZhaoQingJiang
 */
class ZipCompresser extends AbstractCompresser<Zip> implements Zip {
	
	/** 压缩级别[0-9],数字越大文件越小,压缩效率越低 */
	private int level = -1;
	/** 压缩算法 */
	private ZipAlgorithm method;
	/** 注释 */
	private String comment;
	
	ZipCompresser() {
		super();
	}

	ZipCompresser(int bufSize) {
		super(bufSize);
	}

	@Override
	public ZipCompresser level(int level) {
		requireTrue(level >= 0 && level <= 9, "压缩等级取值: [0,9]");
		this.level = level;
		return this;
	}
	
	@Override
	public Zip algorithm(ZipAlgorithm algorithm) {
		this.method = requireNotNull(method, "method == null");
		return this;
	}

	@Override
	public Zip comment(CharSequence comment) {
		this.comment = comment.toString();
		return this;
	}
	
	@Override
	public void compressTo(OutputStream outputStream) {
		requireNotNull(outputStream, "outputStream == null");
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
					compress(null, Path.class.cast(val).toFile(), out);
				} else {
					compress(key.toString(), (InputStream) val, out);
				}
			}
		} catch (IOException e) {
			threx(IIOException::new, e);
		} finally {
			close();
		}
	}

	@Override
	protected ZipCompresser self() {
		return this;
	}

	@Override
	protected String extension() {
		return EXSTENSION;
	}
	
	/**
	 * 文件类压缩核心方法,递归压缩
	 */
	private void compress(String parent, File file, ZipOutputStream out) throws IOException {
		String name = isNull(parent) ? file.getName() : stringJoinWith(separator, parent, file.getName());
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children.length != 0) {
				for (File child : children) {
					compress(name, child, out);
				}
			} else {
				out.putNextEntry(new ZipEntry(name + separator));
				out.closeEntry();
			}
		} else {
			try (BufferedInputStream in = 
					new BufferedInputStream(new FileInputStream(file))) {
				compress(name, in, out);
			} catch (Exception e) {
				throw e;
			}
		}
	}
	
	/**
	 * 输入流类压缩核心方法
	 */
	private void compress(String name, InputStream in, ZipOutputStream out) throws IOException {
		int i;
		out.putNextEntry(new ZipEntry(name));
		while ((i = in.read(buf)) != -1) {
			out.write(buf, 0, i);
		}
		in.close();
		out.closeEntry();
	}

}