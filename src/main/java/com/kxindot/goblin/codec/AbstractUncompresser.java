package com.kxindot.goblin.codec;

import static com.kxindot.goblin.Objects.isNull;
import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Objects.requireTrue;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.threx;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.kxindot.goblin.io.IIOException;

/**
 * 抽象压缩文件解压器。
 * 
 * @author ZhaoQingJiang
 */
 abstract class AbstractUncompresser<T extends Uncompresser<T>> implements Uncompresser<T> {
	 
	protected byte[] buf;
	private Path file;
	private Path destination;
	private boolean creatable;
	
	AbstractUncompresser() {
		this(8 * 1024);
	}

	AbstractUncompresser(int bufSize) {
		buffer(bufSize);
		this.creatable = true;
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
		this.file = path;
		return self();
	}

	@Override
	public T set(File file) {
		return set(file.toPath());
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
	public List<Path> uncompress() {
		return uncompress(true);
	}
	
	@Override
	public List<Path> uncompress(boolean override) {
		requireNotNull(file, "请设置待解压文件");
		if (isNull(destination)) {
			destination = file.getParent();
		} else if (!exists(destination)) {
			if (!creatable) {
				threx(IIOException::new, "解压输出文件夹不存在: %s", destination);
			}
			mkDirs(destination);
		}
		return uncompress(file, destination, override);
	}

	
	protected abstract T self();
	
	
	protected abstract List<Path> uncompress(Path file, Path destination, boolean override);
	
}
