package org.io;

import java.io.*;
import java.util.Arrays;

/*
    序列化是指把一个Java对象变成二进制内容，本质上就是一个byte[]数组。
    为什么要把Java对象序列化呢？因为序列化后可以把byte[]保存到文件中，或者把byte[]通过网络传输到远程，这样，就相当于把Java对象存储到文件或者通过网络传输出去了。
    有序列化，就有反序列化，即把一个二进制内容（也就是byte[]数组）变回Java对象。有了反序列化，保存到文件中的byte[]数组又可以“变回”Java对象，或者从网络上读取byte[]并把它“变回”Java对象。
 */
public class SerializeStudy {
    public static void test() throws IOException, ClassNotFoundException {
        //把java对象变为byte[]数组，需要ObjectOutputStream
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try(ObjectOutputStream output = new ObjectOutputStream(buffer)){
            Person p = new Person("XiaoMing", 18);
            output.writeObject(p);
            output.writeUTF("Hello hh");
        }
        //从一个字节流读取java对象
        try(ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()))){
            Person p = (Person)input.readObject();
            String s = input.readUTF();
            System.out.println(p.name + " " + p.age + " " + s);
        }
    }
}
//java对象序列化需要实现Serializable接口
class Person implements Serializable {
    //InvalidClassException，这种情况常见于序列化的Person对象定义了一个int类型的age字段，但是反序列化时，Person类定义的age字段被改成了long类型，所以导致class不兼容。
    //为了避免这种class定义变动导致的不兼容，Java的序列化允许class定义一个特殊的serialVersionUID静态变量，用于标识Java类的序列化“版本”，通常可以由IDE自动生成。
    // 如果增加或修改了字段，可以改变serialVersionUID的值，这样就能自动阻止不匹配的class版本
    @Serial
    private static final long serialVersionUID = 2709425275741743919L;
    public String name;
    public int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
