package com.kxindot.goblin.io.file;

import static com.kxindot.goblin.Objects.requireNotNull;
import static com.kxindot.goblin.Resources.delete;
import static com.kxindot.goblin.Resources.deleteIfExists;
import static com.kxindot.goblin.Resources.exists;
import static com.kxindot.goblin.Resources.isDirectory;
import static com.kxindot.goblin.Resources.isFile;
import static com.kxindot.goblin.Resources.mkDirs;
import static com.kxindot.goblin.Throws.silentThrex;
import static com.kxindot.goblin.Throws.threx;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 复制或移动文件及目录。
 * 
 * @author ZhaoQingJiang
 * @since 1.1.9
 */
public class FileCopier {
	
	private Path source;
	private Path target;
	
	
	public FileCopier(Path source, Path target) {
		this.source = requireNotNull(source, "文件或目录不能等于null");
		if (!Files.exists(source)) {
			threx(IllegalArgumentException::new, "文件或目录不存在：%s", source);
		}
		this.target = requireNotNull(target, "文件或目录不能等于null");
	}

	
	/**
	 * 复制文件或目录。<br>
	 * <pre>
	 * 若source与target都是文件，则直接复制，返回target；
	 * 若source是文件，target是目录，则将source复制至target目录内，返回复制后的文件；
	 * 若source与target都是目录且copyContent=false，则将source目录整体复制到target目录内，返回复制后的目录；
	 * 若source与target都是目录且copyContent=true，则将source目录内所有文件及目录复制到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录复制过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see Files#copy(Path, Path, CopyOption...)
	 * @param copyContent boolean
	 * @param override boolean
	 * @return Path
	 * @throws IIOException 复制过程中出现任何问题将抛出此异常
	 */
	public Path copy(boolean copyContent, boolean override) {
		CopyOption[] options = getCopyOptions(override);
		if (isFile(source)) {
			return copyFile(options);
		} else if (isFile(target)) {
			silentThrex(FileAlreadyExistsException::new, "文件已存在：%s", target);
		}
		return new DirectoryCopyVisitor(copyContent, options).copy();
	}
	
	/**
	 * 移动文件或目录。<br>
	 * <pre>
	 * 若source与target都是文件，则直接移动，返回target；
	 * 若source是文件，target是目录，则将source移动至target目录内，返回移动后的文件；
	 * 若source与target都是目录且moveContent=false，则将source目录整体移动到target目录内，返回移动后的目录；
	 * 若source与target都是目录且moveContent=true，则将source目录内所有文件及目录移动到target目录内，返回target目录；
	 * </pre>
	 * 异常情况：
	 * <pre>
	 * 若override=false且目标位置文件已存在时抛出{@link IIOException}；
	 * 若source是目录且target是文件时抛出{@link IIOException}；
	 * 若source是目录且target也是目录，在目录移动过程中发现目标位置已存在相同名称的文件时抛出{@link IIOException}；
	 * </pre>
	 * 
	 * @see Files#move(Path, Path, CopyOption...)
	 * @param moveContent boolean
	 * @param override boolean
	 * @return Path
	 * @throws IIOException 移动过程中出现任何问题将抛出此异常
	 */
	public Path move(boolean moveContent, boolean override) {
		CopyOption[] options = getMoveOptions(override);
		if (isFile(source)) {
			return moveFile(options);
		} else if (isFile(target)) {
			silentThrex(FileAlreadyExistsException::new, "文件已存在：%s", target);
		}
		return new DirectoryMoveVisitor(moveContent, options).move();
	}
	
	/**
	 * 重命名文件或目录，返回重命名后的文件或目录。<br>
	 * <pre>
	 * 若deleteExisting=false且target已存在则抛出{@link IIOException}；
	 * 若deleteExisting=true且target已存在，则先删除target再进行重命名；
	 * </pre>
	 * 
	 * @param deleteExisting boolean
	 * @return Path
	 * @throws IIOException 重命名过程中出现任何问题将抛出此异常
	 */
	public Path rename(boolean deleteExisting) {
		if (source.equals(target)) {
			return source;
		} else if (exists(target) && !deleteExisting) {
			silentThrex(FileAlreadyExistsException::new, "重命名文件或目录已存在：%s", target);
		} else if (isDirectory(target) && deleteExisting) {
			delete(target);
		}
		CopyOption[] options = getMoveOptions(deleteExisting);
		try {
			Files.move(source, target, options);
		} catch (IOException e) {
			silentThrex(e, "重命名%s失败：%s", isFile(source) ? "文件" : "目录", e.getMessage());
		}
		return target;
	}
	
	/**
	 * 复制文件。
	 * 
	 * @param options CopyOption[]
	 * @return Path
	 */
	private Path copyFile(CopyOption[] options) {
		Path target = this.target;
		if (isDirectory(target)) {
			target = this.target.resolve(source.getFileName());
		}
		try {
			target = Files.copy(source, target, options);
		} catch (IOException e) {
			silentThrex(e, "复制文件失败：%s", e.getMessage());
		}
		return target;
	}
	
	/**
	 * 移动文件。
	 * 
	 * @param options CopyOption[]
	 * @return Path
	 */
	private Path moveFile(CopyOption[] options) {
		Path target = this.target;
		if (isDirectory(target)) {
			target = this.target.resolve(source.getFileName());
		}
		try {
			target = Files.move(source, target, options);
		} catch (IOException e) {
			silentThrex(e, "移动文件失败：%s", e.getMessage());
		}
		return target;
	}
	
	/**
	 * 获取复制选项。 
	 * 
	 * @param override boolean
	 * @return CopyOption[]
	 */
	private CopyOption[] getCopyOptions(boolean override) {
		return override ? new CopyOption[] {COPY_ATTRIBUTES, REPLACE_EXISTING} : new CopyOption[] {COPY_ATTRIBUTES};
	}
	
	/**
	 * 获取移动选项。
	 * 
	 * @param override boolean
	 * @return CopyOption[]
	 */
	private CopyOption[] getMoveOptions(boolean override) {
		return override ? new CopyOption[] {REPLACE_EXISTING} : new CopyOption[] {};
	}
	
	/**
	 * 解析目标路径。
	 * 
	 * @param source Path
	 * @param subSource Path
	 * @param target Path
	 * @return Path
	 */
	private Path resolveTarget(Path source, Path subSource, Path target) {
		return target.resolve(source.relativize(subSource));
	}
	
	/**
	 * 访问并遍历复制整个目录/目录内容。
	 * 
	 * @author ZhaoQingJiang
	 */
	class DirectoryCopyVisitor extends SimpleFileVisitor<Path> {
		
		Path copyTarget;
		CopyOption[] copyOptions;
		
		DirectoryCopyVisitor(boolean copyContent, CopyOption[] copyOptions) {
			copyTarget = copyContent ? target : target.resolve(source.getFileName());
			mkDirs(copyTarget);
			this.copyOptions = copyOptions;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Path dirTarget = resolveTarget(source, dir, copyTarget);
			if (isFile(dirTarget)) {
				threx(FileAlreadyExistsException::new, "复制目录失败，%s已存在且是文件", dirTarget);
			} else if (!exists(dirTarget)) {
				Files.copy(dir, dirTarget, copyOptions);
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Files.copy(file, resolveTarget(source, file, copyTarget), copyOptions);
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * 遍历并复制目录。
		 * 
		 * @return Path
		 */
		public Path copy() {
			try {
				Files.walkFileTree(source, this);
			} catch (IOException e) {
				silentThrex(e, "复制目录失败：%s", e.getMessage());
			}
			return copyTarget;
		}
		
	}
	
	/**
	 * 访问并遍历移动整个目录内容。
	 * 
	 * @author ZhaoQingJiang
	 */
	class DirectoryMoveVisitor extends SimpleFileVisitor<Path> {
		
		Path moveTarget;
		CopyOption[] moveOptions;
		
		DirectoryMoveVisitor(boolean moveContent, CopyOption[] moveOptions) {
			moveTarget = moveContent ? target : target.resolve(source.getFileName());
			mkDirs(moveTarget);
			this.moveOptions = moveOptions;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Path dirTarget = resolveTarget(source, dir, moveTarget);
			if (isFile(dirTarget)) {
				threx(FileAlreadyExistsException::new, "移动目录失败，%s已存在且是文件", dirTarget);
			} else if (!exists(dirTarget)) {
				mkDirs(dirTarget);
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Path fileTarget = resolveTarget(source, file, moveTarget);
			Files.move(file, fileTarget, moveOptions);
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * 遍历并移动目录。
		 * 
		 * @return Path
		 */
		Path move() {
			try {
				Files.walkFileTree(source, this);
				deleteIfExists(source);
			} catch (IOException e) {
				silentThrex(e, "移动目录失败：%s", e.getMessage());
			}
			return moveTarget;
		}
		
	}
	
}
