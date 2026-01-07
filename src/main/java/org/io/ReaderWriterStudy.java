package org.io;

import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
/*
    Reader和InputStream有什么关系？
    除了特殊的CharArrayReader和StringReader，普通的Reader实际上是基于InputStream构造的，因为Reader需要从InputStream中读入字节流（byte），
    然后，根据编码设置，再转换为char就可以实现字符流。如果我们查看FileReader的源码，它在内部实际上持有一个FileInputStream
    既然Reader本质上是一个基于InputStream的byte到char的转换器，那么，如果我们已经有一个InputStream，想把它转换为Reader，是完全可行的。
    InputStreamReader就是这样一个转换器，它可以把任何InputStream转换为Reader
 */
public class ReaderWriterStudy {
    public static void ReaderTest() throws IOException {
        try(Reader reader = new FileReader("D:\\test.txt",StandardCharsets.UTF_8)){
            char[] buffer = new char[1024];
            int n;
            while((n = reader.read(buffer)) != -1){
                System.out.println("read " + n + "chars.");
            }
        }
        //CharArrayReader可以在内存中模拟一个Reader,可以把char[]数组变成一个Reader
        try(Reader reader = new CharArrayReader("Hello World".toCharArray())){

        }
        //StringReader则可以把String变成一个Reader
        try(Reader reader = new StringReader("Hello World")){
            char[] buffer = new char[1024];
            int n;
            while((n = reader.read(buffer)) != -1){
                System.out.println("read " + n + "chars.");
            }
            for (char c : buffer) {
                if(c == '\0')break;
                System.out.print(c);
            }
        }
        //使用InputStreamReader转换输入流为Reader
        try(Reader reader = new InputStreamReader(new FileInputStream("D:\\test.txt"))){

        }
    }
//    Reader是带编码转换器的InputStream，它把byte转换为char，而Writer就是带编码转换器的OutputStream，它把char转换为byte并输出
    public static void WriterTest() throws IOException {
        //它和上面就是反着来的，都一样
    }

    //PrintStream和PrintWriter
    //PrintStream是一种FilterOutputStream，它在OutputStream的接口上，额外提供了一些写入各种数据类型的方法
    //就是System.out.print/ln(); 还不用抛出异常，
    //PrintStream最终输出的总是byte数据，而PrintWriter则是扩展了Writer接口，它的print()/println()方法最终输出的是char数据。两者的使用方法几乎是一模一样的
    public static void PrintWriterTest() throws IOException {
        StringWriter buffer = new StringWriter();//内部维护了一个StringBuffer
        Writer out = new FileWriter("D:\\test2.txt");
        try(PrintWriter writer = new PrintWriter(/* out */buffer)){
            writer.println("Hello");
            writer.println(123);
            writer.println(true);
        }
        System.out.println(buffer.toString());
    }
}
