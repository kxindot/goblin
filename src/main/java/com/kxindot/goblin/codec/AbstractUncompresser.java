package com.kxindot.goblin.codec;

import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.silentThrex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import com.kxindot.goblin.io.IO;

/**
 * 抽象压缩文件解压器。
 * 
 * @author ZhaoQingJiang
 */
 abstract class AbstractUncompresser<T extends Uncompresser<T>> implements Uncompresser<T> {
	
	private static final short CREATABLE = 1 << 0;
	private static final short CLOSED = 1 << 1;
	private byte[] buf;
	private short state = 0;
	private InputStream inputStream;
	private Path destination;
	
	AbstractUncompresser() {
		this(8 * 1024);
	}

	AbstractUncompresser(int bufSize) {
		buffer(bufSize);
		this.state |= CREATABLE;
	}

	@Override
	public T buffer(int size) {
		requireTrue(size > 0, "压缩缓冲区size必须大于0");
		this.buf = new byte[size];
		return self();
	}

	@Override
	public T set(Path path) {
		requireTrue(exists(path), "待解压文件不存在: %s", path);
		requireTrue(isFile(path), "传入的不是一个文件: %s", path);
		if (isNull(destination)) {
			this.destination = path.getParent();
		}
		return set(IO.openInputStream(path));
	}

	@Override
	public T set(File file) {
		return set(file.toPath());
	}
	
	@Override
	public T set(InputStream inputStream) {
		requireNotNull(inputStream, "待解压输入流不能等于null");
		this.inputStream = inputStream;
		return self();
	}

	@Override
	public T destination(Path directory) {
		return destination(directory, true);
	}
	
	@Override
	public T destination(Path directory, boolean creatable) {
		requireNotNull(directory, "压缩文件输出文件夹不能等于null");
		requireTrue(isDirectory(directory), "压缩文件输出必须是文件夹: %s", directory);
		if (!creatable) {
			this.state ^= CREATABLE;
		}
		this.destination = directory;
		return self();
	}

	@Override
	public List<Path> uncompress() {
		return uncompress(true);
	}
	
	@Override
	public List<Path> uncompress(boolean override) {
		if ((state & CLOSED) != 0) {
			silentThrex(IOException::new, "解压缩器实例已关闭");
		}
		requireNotNull(inputStream, IllegalArgumentException::new, "请设置待解压文件");
		if (!exists(destination)) {
			if ((state & CREATABLE) == 0) {
				silentThrex(IOException::new, "解压输出文件夹不存在: %s", destination);
			}
			mkDirs(destination);
		}
		try {
			return uncompress(inputStream, destination, override);
		} finally {
			this.state |= CLOSED;
			this.buf = null;
			this.inputStream = null;
			this.destination = null;
		}
	}
	
	/**
	 * 获取缓冲区。
	 * 
	 * @return byte[]
	 */
	protected byte[] buffer() {
		return buf;
	}
	
	/**
	 * 返回实例对象。
	 * 
	 * @return T
	 */
	protected abstract T self();
	
	/**
	 * 解压缩输入流。
	 * 
	 * @param inputStream InputStream
	 * @param destination Path
	 * @param override boolean
	 * @return {@code List<Path>}
	 */
	protected abstract List<Path> uncompress(InputStream inputStream, Path destination, boolean override);
	
}
