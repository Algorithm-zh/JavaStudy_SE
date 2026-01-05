package org.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

//@interface定义注解
public @interface Check {
    int min() default 0;
    int max() default 100;
    int value() default 0;
}
