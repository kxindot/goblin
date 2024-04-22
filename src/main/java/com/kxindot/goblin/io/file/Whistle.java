package com.kxindot.goblin.io.file;

import java.nio.file.Path;
import java.util.EventListener;

/**
 * Whistle接口用于定义处理文件系统事件的方法。实现此接口的类可以对文件的创建、修改和删除事件做出响应
 * @author jenius
 */
public interface Whistle extends EventListener {

    /**
     * 当文件被创建时调用
     * @param file 被创建的文件
     */
    void onCreated(Path file);

    /**
     * 当文件被修改时调用
     * @param file 被修改的文件
     */
    void onModified(Path file);

    /**
     * 当文件被删除时调用
     * @param file 被删除的文件
     */
    void onDeleted(Path file);
}
