package com.kxindot.goblin.encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;

/**
 * A utility class to access resources in src/main/resources
 *
 * @author 2024-07-15
 */
public class ResourceUtils {

    public static String loadTxtFileFromResource(String fileName) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                // 使用BufferedReader逐行读取文件内容并附加到StringBuilder中
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contentBuilder.append(line).append("\n");
                    }
                }
                return contentBuilder.toString().trim();
            } else {
                throw new IllegalStateException("inputStream is null as expected");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
