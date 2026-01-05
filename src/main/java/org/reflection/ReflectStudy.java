package org.reflection;

import java.lang.reflect.Field;

public class ReflectStudy {

    //如果Person一无所知，没有引用，我们布置如何调用其方法，编译也无法通过
    static void getFullName(Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //编译不通过
        //Person p = (Person) o;
        //System.out.println(p.getName());

        //获取一个class的Class实例的三个方法
        //方法一：直接通过class的静态变量class获取
        Class cls = String.class;
        //方法二：如果我们有一个实例变量，可以通过该实例变量提供的getClass()方法获取：
        String s = "hello";
        cls = s.getClass();
        //方法三：如果知道一个class的完整类名，可以通过静态方法Class.forName()获取：
        cls = Class.forName("java.lang.String");
        //因为Class实例在JVM中是唯一的，所以，上述方法获取的Class实例是同一个实例。可以用==比较两个Class实例

        //通过反射获取参数的class信息
        Class o = obj.getClass();
        System.out.println(o.getName());//打印包名
        System.out.println(o.getDeclaredField("name"));//获取私有字段名, 不加Declared是公共的
        System.out.println(o.getDeclaredField("age"));

        //使用反射得到字段值
        Field f = o.getDeclaredField("name");//获取字段
        f.setAccessible(true);//访问私有字段的时候需要先设置这个，不管是不是public都进行访问，但是不是在所有情况下都会成功
        Object value = f.get(obj);
        System.out.println(value);

        //使用设置字段值设置
        f.set(obj, "wangwu");
        System.out.println(f.get(obj));
    }



    //JVM在执行Java程序的时候，并不是一次性把所有用到的class全部加载到内存，而是第一次需要用到class时才加载
    //Commons Logging利用这个动态加载特性，实现了日志实现的动态切换。
    // Commons Logging优先使用Log4j实现:
    /*
    LogFactory factory = null;
    if (isClassPresent("org.apache.logging.log4j.Logger")) {
        factory = createLog4j();
    } else {
        factory = createJdkLog();
    }
    boolean isClassPresent(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
     */
}
