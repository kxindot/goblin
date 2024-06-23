package com.kxindot.goblin.codec;

import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.threx;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.kxindot.goblin.io.IIOException;
import com.kxindot.goblin.io.IO;

/**
 * Zip文件格式解压缩器。
 * 
 * @author ZhaoQingJiang
 */
class UnzipUncompresser extends AbstractUncompresser<Unzip> implements Unzip {

	UnzipUncompresser() {
		super();
	}

	UnzipUncompresser(int bufSize) {
		super(bufSize);
	}

	@Override
	protected Unzip self() {
		return this;
	}
	
	@Override
	protected List<Path> uncompress(Path file, Path destination, boolean override) {
		ZipEntry entry = null;
		List<Path> files = newArrayList();
		BufferedOutputStream outputStream = null;
		try (ZipInputStream inputStream = new ZipInputStream(IO.openInputStream(file))) {
			Path path = null;
			while (null != (entry = inputStream.getNextEntry())) {
				path = destination.resolve(entry.getName());
				if (entry.isDirectory()) {
					if (!exists(path)) {
						mkDirs(path);
					}
					continue;
				}
				files.add(path);
				if (exists(path) && !override) {
					continue;
				}
				int i = 0;
				outputStream = new BufferedOutputStream(IO.openOutputStream(path));
				while ((i = inputStream.read(buf)) != -1) {
					outputStream.write(buf, 0, i);
				}
				IO.close(outputStream);
			}
		} catch (IOException e) {
			threx(IIOException::new, e);
		} finally {
			IO.close(outputStream);
		}
		return files;
	}

}
