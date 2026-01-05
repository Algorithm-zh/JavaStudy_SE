package org.reflection;


//反射
//Java的反射是指程序在运行期可以拿到一个对象的所有信息。
//反射是为了解决在运行期，对某个实例一无所知的情况下，如何调用其方法

import org.reflection.ref.Person;

/*
    反射的原理:
    java每加载一个类，jvm都会为期创建一个Class类型的实例，并关联起来，这里的Class类型是一个名叫Class的class
    public final class Class {
        private Class() {}
    }
    比如String
    Class cls = new Class(String);
    所以JVM持有的每个Class实例都指向了一个数据类型，class或者interface
    Class实例包含了该class的所有信息，包括类名、包名、父类、实现的接口、所有方法、字段等，因此，如果获取了某个Class实例，我们就可以通过这个Class实例获取到该实例对应的class的所有信息。
    这种通过Class实例获取class信息的方法就是反射
 */
public class main {
    public static void main(String[] args) {
        Person p = new Person("lisi", 13);
        try{
            ReflectStudy.getFullName(p);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}