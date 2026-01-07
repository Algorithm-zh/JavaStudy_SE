package org.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStudy {
    public static void test() throws IOException {
        //java.io提供了File来操作文件和目录
        File f = new File("D:\\log4j.properties");
        if(f.exists()){
            System.out.println(f);
            System.out.println(File.separator);//打印当前平台的系统分隔符, windows是\，linux是/
            //File对象即使传入的文件或目录不存在，也不会报错
            System.out.println(f.isFile());
            System.out.println(f.isDirectory());
            System.out.println(f.canRead());
        }else{
            //创建新文件
//            boolean ret = f.createNewFile();
            //创建临时文件
            File t = File.createTempFile("tmp-", ".txt");
            t.deleteOnExit();//在jvm退出时删除
            System.out.println(t.getAbsoluteFile());
        }
        //遍历文件和目录
        File d = new File("D:\\迅雷下载");
        File[]fs = d.listFiles();
        printFiles(fs);
        //过滤，只列出我们想看的文件
        File[]fs2 = d.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".exe");
            }
        });
        printFiles(fs2);

//        File ff = new File("D:\\test");
//        ff.mkdir();//创建目录
//        ff.delete();//删除目录,目录下有文件则不能删除,要删除目录下所有文件后，才能删除该目录


        //Path, 是一个比File用起来更简单的类,需要对目录进行复杂拼接遍历的时候可以使用
        Path p1 = Paths.get("..", "generic");//里面填啥，填多少个，就会生成多少级目录
        System.out.println(p1);
    }
    static void printFiles(File[] fs){
        System.out.println("===================");
        if(fs == null)return;
        for (File f : fs) {
            System.out.println(f);
        }
        System.out.println("===================");
    }
}
