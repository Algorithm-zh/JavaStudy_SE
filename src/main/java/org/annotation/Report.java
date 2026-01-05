package org.annotation;

import java.lang.annotation.*;

//元注解，可以修饰其他注解
//@Target可以定义Annotation能够被用于源码的哪些位置
//例如，定义注解@Report可用在方法或字段上，可以用数组
//下面这两个元注解在自定义注解的时候一定要加
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)//定义Annotation的声明周期，编译期、class文件、运行期,默认是class,因为在自定义的一般都是RUNTIME，所以一定要加这个注解
@Inherited //加了之后，使用该注解的父类，子类也会继承该注解, @Inherited仅针对@Target(ElementType.TYPE)类型的annotation有效
public @interface Report{
    int type() default 0;
    String level() default "info";
    String value() default "";
}
