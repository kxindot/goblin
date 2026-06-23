package com.kxindot.goblin.codec;

import static com.kxindot.goblin.Objects.newArrayList;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.silentThrex;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
	protected List<Path> uncompress(InputStream inputStream, Path destination, boolean override) {
		List<Path> files = newArrayList();
		try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
			Path path = null;
			ZipEntry entry = null;
			byte[] buf = buffer();
			while (null != (entry = zipInputStream.getNextEntry())) {
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
				try (BufferedOutputStream outputStream = new BufferedOutputStream(IO.openOutputStream(path));) {
					while ((i = zipInputStream.read(buf)) != -1) {
						outputStream.write(buf, 0, i);
					}
				}
				
			}
		} catch (IOException e) {
			silentThrex(e);
		}
		return files;
	}

}
