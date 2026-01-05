package org.annotation;

public class Car {
    //注解的作用：从JVM的角度看，注解本身对代码逻辑没有任何影响，如何使用注解完全由工具决定
    /*
    第一类是由编译器使用的注解，例如：
        @Override：让编译器检查该方法是否正确地实现了覆写；
        @SuppressWarnings：告诉编译器忽略此处代码产生的警告。
        这类注解不会被编译进入.class文件，它们在编译后就被编译器扔掉了。

    第二类是由工具处理.class文件使用的注解，比如有些工具会在加载class的时候，对class做动态修改，实现一些特殊的功能。
        这类注解会被编译进入.class文件，但加载结束后并不会存在于内存中。这类注解只被一些底层库使用，一般我们不必自己处理。

    第三类是在程序运行期能够读取的注解，它们在加载后一直存在于JVM中，这也是最常用的注解。
        例如，一个配置了@PostConstruct的方法会在调用构造方法后自动被调用（这是Java代码读取该注解实现的功能，JVM并不会识别该注解）
     */
    @Report(type = 1, level = "warn")
    public void test(){}


    @Check(min=0, max=100, value=55)
    public int n;

    @Check(value=99)
    public int p;

    @Check(99) // @Check(value=99)
    public int x;

    @Check
    public int y;
}
