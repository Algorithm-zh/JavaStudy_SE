package org.io;

import java.io.File;
import java.io.IOException;

/*
    InputStream / OutputStream
    IO流以byte（字节）为最小单位，因此也称为字节流。例如，我们要从磁盘读入一个文件，包含6个字节，就相当于读入了6个字节的数据

    Reader / Writer
    如果我们需要读写的是字符，并且字符不全是单字节表示的ASCII字符，那么，按照char来读写显然更方便，这种流称为字符流
    Reader和Writer本质上是一个能自动编解码的InputStream和OutputStream
    使用Reader，数据源虽然是字节，但我们读入的数据都是char类型的字符，原因是Reader内部把读入的byte做了解码，转换成了char。
    使用InputStream，我们读入的数据和原始二进制数据一模一样，是byte[]数组，但是我们可以自己把二进制byte[]数组按照某种编码转换为字符串。
    究竟使用Reader还是InputStream，要取决于具体的使用场景。如果数据源不是文本，就只能使用InputStream，如果数据源是文本，使用Reader更方便一些。Writer和OutputStream是类似的

    上面这都是同步IO，同步IO是指，每次调用read()或write()方法时，程序会阻塞，等待数据被处理。同步IO的效率较低，但是简单易用。
 */
public class main {
    public static void main(String[] args) throws IOException {
        FileStudy.test();
        StreamStudy.InputStreamTest();
        StreamStudy.OutputStreamTest();
        StreamStudy.FilterStreamTest();
    }
}
