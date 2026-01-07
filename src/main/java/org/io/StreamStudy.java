package org.io;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

//InputStream就是Java标准库提供的最基本的输入流。它位于java.io这个包里。java.io包提供了所有同步IO的功能,这个类是抽象类，不是接口
//read这个方法会读取输入流的下一个字节，并返回字节表示的int值（0~255）。如果已读到末尾，返回-1表示不能继续读取了
public class StreamStudy {
    public static void InputStreamTest() throws IOException {
        //java7以前的写法，比较麻烦
        InputStream input1 = null;
        try{
            input1 = new FileInputStream("D:\\log4j.properties");
            for(;;){
                int n = input1.read();
                if(n == -1)break;
                System.out.print(n + " ");
            }
            System.out.println();
        }finally {//放到finally里，这样在io错误的时候也会关闭输入流
            if(input1 != null){
                input1.close();//使用完必须关闭，不然程序占用资源会越来越多
            }
        }
        //java7以后的写法,编译器自动编写finally关闭资源
        //编译器会看try(resource = ...)中的对象是否实现了java.lang.AutoCloseable接口，如果实现了，就自动加上finally语句并调用close()方法
        try (InputStream input = new FileInputStream("D:\\log4j.properties")) {
            for (; ; ) {
                int n = input.read();
                if (n == -1) break;
                System.out.print(n + " ");
            }
            System.out.println();
        }

        //缓冲
        //很多流支持一次性读取多个字节到缓冲区，对于文件和网络流来说，利用缓冲区一次性读取多个字节效率往往要高很多
        try (InputStream input = new FileInputStream("D:\\log4j.properties")){
            byte[] buf = new byte[1024];
            int n;
            while((n = input.read(buf)) != -1){
                System.out.println("read " + n + "bytes.");
            }
        }
        //ByteArrayInputStream可以在内存里模拟字节输入，可以用来测试
    }

    //OutputStream
    //write这个方法会写入一个字节到输出流。要注意的是，虽然传入的是int参数，但只会写入一个字节，即只写入int最低8位表示字节的部分（相当于b & 0xff）
    //OutPutStream还有一个flush方法，目的是将缓冲区内容真正输出到目的地
    //为什么要有flush()？因为向磁盘、网络写入数据的时候，出于效率的考虑，操作系统并不是输出一个字节就立刻写入到文件或者发送到网络，
    // 而是把输出的字节先放到内存的一个缓冲区里（本质上就是一个byte[]数组），等到缓冲区写满了，再一次性写入文件或者网络。
    // 对于很多IO设备来说，一次写一个字节和一次写1000个字节，花费的时间几乎是完全一样的，所以OutputStream有个flush()方法，能强制把缓冲区内容输出
    // 通常情况下，我们不需要调用这个flush()方法，因为缓冲区写满了OutputStream会自动调用它，并且，在调用close()方法关闭OutputStream之前，也会自动调用flush()方法。
    /*
    但是，在某些情况下，我们必须手动调用flush()方法。举个栗子：
    小明正在开发一款在线聊天软件，当用户输入一句话后，就通过OutputStream的write()方法写入网络流。小明测试的时候发现，发送方输入后，接收方根本收不到任何信息，怎么回事？
    原因就在于写入网络流是先写入内存缓冲区，等缓冲区满了才会一次性发送到网络。如果缓冲区大小是4K，则发送方要敲几千个字符后，操作系统才会把缓冲区的内容发送出去，
    这个时候，接收方会一次性收到大量消息。解决办法就是每输入一句话后，立刻调用flush()，不管当前缓冲区是否已满，强迫操作系统把缓冲区的内容立刻发送出去。
     */
    //input也有缓冲区，你读一个字节它会读若干字节到缓冲区，每次read它会直接返回缓冲区的下一个字节，避免了频繁的io
    public static void OutputStreamTest() throws IOException {
        try(OutputStream output = new FileOutputStream("D:\\test.txt")){
            for(int i = 0; i < 26; i++){
                output.write('A' + i);
            }
            //可以调用Write(byte[])方法一次写若干字节
            output.write("\nHello XiaoMing".getBytes(StandardCharsets.UTF_8));
        }


        // 读取input.txt，写入output.txt:
        try (InputStream input = new FileInputStream("D:\\test.txt");
             OutputStream output = new FileOutputStream("D:\\output.txt"))//同时操作多个资源时可以使用;隔开
        {
            input.transferTo(output); // transferTo的作用是? 读取input的内容，写入output
        }
    }

    //当我们需要给一个“基础”InputStream附加各种功能时，我们先确定这个能提供数据源的InputStream
    //紧接着，我们希望FileInputStream能提供缓冲的功能来提高读取的效率，因此我们用BufferedInputStream包装这个InputStream
    //最后，假设该文件已经用gzip压缩了，我们希望直接读取解压缩的内容，就可以再包装一个GZIPInputStrea
    /*
            ┌─────────────────────────┐
            │GZIPInputStream          │
            │┌───────────────────────┐│
            ││BufferedFileInputStream││
            ││┌─────────────────────┐││
            │││   FileInputStream   │││
            ││└─────────────────────┘││
            │└───────────────────────┘│
            └─────────────────────────┘
     */
    //上述这种通过一个“基础”组件再叠加各种“附加”功能组件的模式，称之为Filter模式（或者装饰器模式：Decorator）。它可以让我们通过少量的类来实现各种功能的组合
    public static void FilterStreamTest() throws IOException {
        byte[] data = "Hello World.".getBytes(StandardCharsets.UTF_8);
        try(CountInputStream input = new CountInputStream(new ByteArrayInputStream(data))){
            int n;
            while((n = input.read()) != -1){
                System.out.print((char)n);
            }
            System.out.println();
            System.out.println("Total bytes read: " + input.getBytesRead() +" bytes");
        }
    }
}
//自己编写FilterInputStream
/*
装饰器模式包含以下几个核心角色：
抽象组件（Component）：定义了原始对象和装饰器对象的公共接口或抽象类，可以是具体组件类的父类或接口。
具体组件（Concrete Component）：是被装饰的原始对象，它定义了需要添加新功能的对象。
抽象装饰器（Decorator）：继承自抽象组件，它包含了一个抽象组件对象，并定义了与抽象组件相同的接口，同时可以通过组合方式持有其他装饰器对象。
具体装饰器（Concrete Decorator）：实现了抽象装饰器的接口，负责向抽象组件添加新的功能。具体装饰器通常会在调用原始对象的方法之前或之后执行自己的操作。
 */
//FilterInputStream就是抽象装饰器 InputStream就是抽象组件
//那我们的这个类就是具体装饰器
class CountInputStream extends FilterInputStream{
    private int count = 0;
    /**
     * Creates a {@code FilterInputStream}
     * by assigning the  argument {@code in}
     * to the field {@code this.in} so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or {@code null} if
     *           this instance is to be created without an underlying stream.
     */
    protected CountInputStream(InputStream in) {
        super(in);
    }
    public int getBytesRead(){
        return this.count;
    }

    @Override
    public int read() throws IOException {
        int n = in.read();//委托给被装饰者，也就是ByteArrayInputStream
        if(n != -1){
            this.count ++;
        }
        return n;
    }
}