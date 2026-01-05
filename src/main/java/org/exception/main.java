package org.exception;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Scanner;

public class main {
    //Throwable继承自Object,是异常体系的根
    //Error和Exception继承自Throwable
    //一个表示严重错误，一个是运行时的错误可以被捕获并处理
    //Exception又分为RuntimeException和其它

//    Java规定：
//    必须捕获的异常，包括Exception及其子类，但不包括RuntimeException及其子类，这种类型的异常称为Checked Exception。
//    不需要捕获的异常，包括Error及其子类，RuntimeException及其子类。

    public static void main(String[] args) /*throws Exception*/ {//如果在main里写throws Exception就不需要捕获了，但是代价就是发生异常程序会立即退出
        //底层没有捕获，所以需要在高层捕获
        try{
            byte[] bs = toGBK("中文");
            System.out.println(Arrays.toString(bs));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        //多个异常捕获
        try{
            process1();
            process2("12");
            process3("sd");
        }catch(IOException | NumberFormatException e){//这两种异常的处理代码相同， 所以可以使用|合并
            System.out.println("Bad input");
            e.printStackTrace();//打印方法调用栈
        }catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Null pointer");
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("End");//finally先执行,如果finally里有抛出异常，上面的异常全部失效，因为异常只能有一个被抛出
        }
    }
    static byte[] toGBK(String s) throws UnsupportedEncodingException {//在方法定义处写throws的话就可以让它通过编译
        return s.getBytes("GBK");
    }

    public static void process1() throws IOException{
        Scanner sc = new Scanner(System.in);
        var s = sc.nextLine();
        System.out.println(s);
        //异常转移，从一个异常转移成另一个
        try{
            process2(s);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException(e);//把原异常信息也放进去，不然会丢失!!
        }
    }

    public static void process2(String s){//这个异常是runtime异常子类，所以不需要捕获
        Integer.parseInt(s);//这里面会抛出NumberFormatException
    }
    public static void process3(String s){
        if(s == null){
            throw new NullPointerException();
        }
    }
}
