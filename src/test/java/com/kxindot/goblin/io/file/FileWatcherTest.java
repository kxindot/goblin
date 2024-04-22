package com.kxindot.goblin.io.file;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

class FileWatcherTest {

    private FileWatcher fileWatcher;

    @Test
    void testWatch() throws IOException {
        File file = new File("src/test/resources");
        fileWatcher = new FileWatcher(Paths.get(file.toURI()));
        fileWatcher.watch();
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            if (t.getName().equals("FileWatcher")) {
                Assertions.assertTrue(t.isDaemon());
                Assertions.assertTrue(t.isAlive());
            }
            t.interrupt();
        }
    }

    @Test
    void testWatchNotExist() throws IOException {
        File file = new File("src/test/notExist");
        fileWatcher = new FileWatcher(Paths.get(file.toURI()));
        IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fileWatcher.watch();
        });
        Assertions.assertTrue(ex.getMessage().contains("src/test/notExist is not exist!"));
        File file2 = new File("src/test/resources/test.json");
        fileWatcher = new FileWatcher(Paths.get(file2.toURI()));
        IllegalArgumentException ex2 = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fileWatcher.watch();
        });
        Assertions.assertTrue(ex2.getMessage().contains("src/test/resources/test.json is not a directory!"));
    }

    @Test
    void testRun() throws Exception {
        File dir = new File("src/test/resources");
        Path path = Paths.get(dir.toURI());
        final Map<String, String> map = new HashMap<>();
        fileWatcher = new FileWatcher(path);
        fileWatcher.setWhistle(new Whistle() {
            public void onCreated(Path filePath) {
                System.out.println("file created: " + filePath);
                map.put("file.created", filePath.toString());
            }

            public void onModified(Path filePath) {
                System.out.println("file modified: " + filePath);
                map.put("file.modified", filePath.toString());
            }

            public void onDeleted(Path filePath) {
                System.out.println("file deleted: " + filePath);
                map.put("file.deleted", filePath.toString());
            }
        }).watch();
        Assertions.assertNotNull(fileWatcher.getWhistle());
        //创建文件
        File fileNew = new File("src/test/resources/testCreate.json");
        fileNew.createNewFile();
        Thread.sleep(30000);
        //写入文件
        try (FileWriter writer = new FileWriter(fileNew)) {
            writer.write(
                    "{\n" + "  \"name\": \"jenius\",\n" + "    \"version\": \"1.0.0\",\n" + "      \"description\": \"create\"\n" + "}");
        }
        Thread.sleep(30000);
        //删除文件
        fileNew.delete();
        Thread.sleep(40000);
        Assertions.assertEquals(3, map.size());
        Assertions.assertTrue(map.get("file.created").contains("testCreate.json"));
    }

    @Test
    void testRun2() throws Exception {
        File dir = new File("src/test/resources");
        Path path = Paths.get(dir.toURI());
        final Map<String, String> map = new HashMap<>();
        fileWatcher = new FileWatcher(path);
        fileWatcher.setWhistle(new Whistle() {
            public void onCreated(Path filePath) {
                System.out.println("file created: " + filePath);
                map.put("file.created", filePath.toString());
            }

            public void onModified(Path filePath) {
                System.out.println("file modified: " + filePath);
                map.put("file.modified", filePath.toString());
            }

            public void onDeleted(Path filePath) {
                System.out.println("file deleted: " + filePath);
                map.put("file.deleted", filePath.toString());
            }
        }).watch();
        Assertions.assertNotNull(fileWatcher.getWhistle());
        //创建文件
        File fileNew = new File("src/test/resources/test");
        fileNew.mkdir();
        Thread.sleep(30000);
        Assertions.assertTrue(fileNew.isDirectory());
        File fileNew1 = new File("src/test/resources/test/testCreate.json");
        fileNew1.createNewFile();
        Thread.sleep(30000);
        FileUtils.deleteDirectory(fileNew);
        Thread.sleep(40000);
        Assertions.assertEquals(2, map.size());
        Assertions.assertTrue(map.get("file.created").contains("src/test/resources/test/testCreate.json"));
        Assertions.assertNull(map.get("file.modified"));
        Assertions.assertTrue(map.get("file.deleted").contains("test"));
        Assertions.assertFalse(map.get("file.deleted").contains("testCreate.json"));
    }

    @Test
    void testClose() throws IOException, InterruptedException {
        File file = new File("src/test/resources");
        fileWatcher = new FileWatcher(Paths.get(file.toURI()));
        fileWatcher.watch();
        fileWatcher.close();
        Thread.sleep(2000);
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t : threadSet) {
            if (t.getName().equals("FileWatcher")) {
                Assertions.assertFalse(t.isAlive());
            }
        }
    }

}
