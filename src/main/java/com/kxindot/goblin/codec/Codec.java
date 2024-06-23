package com.kxindot.goblin.codec;

import java.nio.file.Path;
import java.util.List;

/**
 * 编码/解码工具。
 * 
 * @author ZhaoQingJiang
 */
public class Codec {
	
	
    /**
     * 获取Zip文件格式压缩工具。
     * 
     * @return {@link Zip}
     */
    public static Zip zip() {
    	return new ZipCompresser();
    }
    
    /**
     * 获取Zip文件格式解压工具。
     * 
     * @return {@link Unzip}
     */
    public static Unzip unzip() {
    	return new UnzipUncompresser();
    }
    
    /**
     * Zip文件解压缩。
     * 
     * @param file zip文件
     * @return 解压出的文件列表
     */
    public static List<Path> unzip(Path file) {
    	return unzip().set(file).uncompress();
    }
    
    /**
     * Zip文件解压缩。
     * 
     * @param file zip文件
     * @param destination 解压输出文件夹
     * @return 解压出的文件列表
     */
    public static List<Path> unzip(Path file, Path destination) {
    	return unzip().set(file).destination(destination).uncompress();
    }
    
}
