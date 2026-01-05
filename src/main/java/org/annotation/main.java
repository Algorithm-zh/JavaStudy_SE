package org.annotation;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class main {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException {
        Car car = new Car();
        //判断Car类上是否有Report注解
        System.out.println(Car.class.isAnnotationPresent(Report.class));
        Method m = Car.class.getMethod("test");
        //判断test方法上是否有Report注解
        System.out.println(m.isAnnotationPresent(Report.class));
        System.out.println(car.p);

        Person p = new Person("", "北京");
        //检查字段长度
        Check(p);
    }

    //这个方法很好的体现了如何使用注解来验证字段
    static void Check(Person p) throws IllegalAccessException {
        //遍历所有的field
        for(Field f : p.getClass().getFields()){
            //获取Field的注解
            Range range = f.getAnnotation(Range.class);
            //如果存在
            if(range != null){
                //获取Field的值
                Object value = f.get(p);
                //判断其值符不符合注解设置的范围
                if(value instanceof String s){
                    if(s.length() < range.min() || s.length() > range.max()){
                        throw new IllegalArgumentException("字段" + f.getName() + "长度不符合要求");
                    }
                }
            }
        }
    }
}
