package org.reflection;


//反射
//Java的反射是指程序在运行期可以拿到一个对象的所有信息。
//反射是为了解决在运行期，对某个实例一无所知的情况下，如何调用其方法

import org.reflection.ref.Person;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
        try {
            ReflectStudy.getFullName(p);
            ReflectStudy.getMethod(p);
            ReflectStudy.getConstructor(p);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

        Hello h = new HelloWorld();
        h.morning("zhangsan");
        //有没有可能不编写实现类，直接在运行期创建某个interface的实例呢？
        //可以，动态代理就可以实现，在运行期动态创建interface实例
        //需要通过jdk提供的Proxy.newProxyInstance()方法
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method);
                if (method.getName().equals("morning")) {
                    System.out.println("Good Morning, " + args[0]);
                }
                return null;
            }
        };
        Hello hello = (Hello) Proxy.newProxyInstance(
                Hello.class.getClassLoader(),//传入classLoader
                new Class[]{Hello.class},//传入要实现的接口
                handler);//传入处理调研方法的
        hello.morning("Bob");
    }

    //就是jvm自动帮我们编写了一个实现类类似下面这种
    /*
    public class HelloDynamicProxy implements Hello {
        InvocationHandler handler;
        public HelloDynamicProxy(InvocationHandler handler) {
            this.handler = handler;
        }
        public void morning(String name) {
            handler.invoke(
                    this,
                    Hello.class.getMethod("morning", String.class),
                    new Object[] { name }
            );
        }
    }
    */
}