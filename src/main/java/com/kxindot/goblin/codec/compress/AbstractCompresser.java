package com.kxindot.goblin.codec.compress;

import static com.kxindot.goblin.Objects.isNotNull;
import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.newLinkedHashMap;
import static com.kxindot.goblin.Objects.requireNotBlank;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Resources.UUID;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.threx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.kxindot.goblin.io.IIOException;
import com.kxindot.goblin.io.IO;

/**
 * 抽象压缩处理器。
 * 
 * @author ZhaoQingJiang
 */
public abstract class AbstractCompresser<T extends Compresser<T>> implements Compresser<T> {
	
	private volatile boolean closed = false;
	protected byte[] buf;
	protected String name;
	protected Charset charset;
	private boolean creatable;
	protected Path destination;
	protected Map<Path, Object> sources;
	
	/**
	 * 缓冲区大小默认8196byte。
	 */
	AbstractCompresser() {
		this(8 * 1024);
	}
	
	AbstractCompresser(int bufSize) {
		buffer(bufSize);
		this.sources = newLinkedHashMap();
	}

	@Override
	public T fileName(String name) {
		this.name = requireNotBlank(name, "压缩文件名称不能为空");
		return self();
	}

	@Override
	public T charset(String charset) {
		this.charset = Charset.forName(charset);
		return self();
	}

	@Override
	public T charset(Charset charset) {
		this.charset = charset;
		return self();
	}

	@Override
	public T buffer(int size) {
		requireTrue(size > 0, "压缩缓冲区size必须大于0");
		this.buf = new byte[size];
		return self();
	}

	@Override
	public T add(Path path) {
		requireTrue(exists(path), "文件或文件夹不存在: %s", path);
		if (isNull(name)) {
			this.name = path.getFileName().toString();
		}
		if (isNull(destination)) {
			this.destination = path.getParent();
		}
		sources.put(path, path);
		return self();
	}

	@Override
	public T add(File file) {
		return add(file.toPath());
	}

	@Override
	public T add(InputStream inputStream) {
		return add(UUID(), inputStream);
	}

	@Override
	public T add(String name, InputStream inputStream) {
		requireNotBlank(name, "名称不能为空");
		requireNotNull(inputStream, "inputStream == null");
		if (isNull(name)) {
			this.name = name;
		}
		sources.put(Paths.get(name), inputStream);
		return self();
	}

	@Override
	public T destination(Path directory) {
		return destination(directory, true);
	}

	@Override
	public T destination(Path directory, boolean creatable) {
		requireNotNull(directory, "directory == null");
		requireTrue(!isFile(directory), "压缩文件输出文件夹不是一个文件夹: %s", directory);
		this.creatable = creatable;
		this.destination = directory;
		return self();
	}

	@Override
	public final Path compress() {
		return compress(true);
	}
	
	@Override
	public Path compress(boolean override) {
		check();
		String fileName = name;
		String ext = extension();
		if (isNotNull(ext) && !fileName.endsWith(ext)) {
			fileName += ext;
		}
		Path file = null;
		if (isNull(destination)) {
			file = Paths.get(fileName);
			destination = file.getParent();
			requireNotNull(destination, IIOException::new, "请指定压缩文件输出文件夹");
		} else {
			file = destination.resolve(fileName);
		}
		if (!exists(destination)) {
			if (!creatable) {
				threx(IIOException::new, "压缩文件输出文件夹不存在: %s", destination);
			}
			mkDirs(destination);
		}
		if (exists(file) && !override) {
			threx(IIOException::new, "压缩文件已存在: %s", file);
		}
		compressTo(IO.openOutputStream(file));
		return file;
	}
	
	@Override
	public byte[] compressToBytes() {
		check();
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 64);
		compressTo(out);
		return out.toByteArray();
	}

	@Override
	public final InputStream compressToInputStream() {
		return new ByteArrayInputStream(compressToBytes());
	}

	/**
	 * 压缩前检查。
	 * 
	 * @throws IIOException 若出现任何错误，则抛出此异常
	 */
	protected void check() {
		if (closed) {
			threx(IIOException::new, "压缩处理器已关闭");
		} else if (isNull(name) || sources.isEmpty()) {
			threx(IIOException::new, "待压缩内容不能为空");
		}
	}
	
	/**
	 * 关闭压缩处理器.
	 */
	protected void close() {
		this.closed = true;
		this.buf = null;
		this.sources.clear();
	}
	
	/**
	 * 返回压缩处理器实例。
	 */
	protected abstract T self();
	
	/**
	 * 返回压缩文件扩展名
	 */
	protected abstract String extension();
	
}
