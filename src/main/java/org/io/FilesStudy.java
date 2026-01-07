package org.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/*
从Java 7开始，提供了Files这个工具类，能极大地方便我们读写文件。
虽然Files是java.nio包里面的类，但他俩封装了很多读写文件的简单方法
但是只能写小文件，比如配置文件，太大的文件还是要用文件流，每次只读一部分文件内容
 */
public class FilesStudy {
    public static void test() throws IOException {

        //一次读取所有内容到字节数组
        byte[] data = Files.readAllBytes(Path.of("D://test.txt"));

        //如果是文本文件，可以一次读取所有内容到字符串
        // 默认使用UTF-8编码读取:
        String content1 = Files.readString(Path.of("D://test.txt"));
        // 可指定编码:
        String content2 = Files.readString(Path.of("D://test.txt", "to", "file.txt"), StandardCharsets.ISO_8859_1);
        // 按行读取并返回每行内容:
        List<String> lines = Files.readAllLines(Path.of("D://test.txt"));

        // 写入二进制文件:
//        Files.write(Path.of("/path/to/file.txt"), data);
        // 写入文本并指定编码:
//        Files.writeString(Path.of("/path/to/file.txt"), "文本内容...", StandardCharsets.ISO_8859_1);
        // 按行写入文本:
//        List<String> lines = ...
//        Files.write(Path.of("/path/to/file.txt"), lines);
    }
}
