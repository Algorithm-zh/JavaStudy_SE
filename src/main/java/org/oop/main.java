package org.oop;

public class main {
    public static void main(String[] args) {
        //抽象类无法实例化, 因为它设计出来就是被继承的
//        Person person = new Person();
        Person person = new Student(18, "lisi", 100);
        String name = "zhangsan";
        person.setName(name);
        System.out.println(person.getName());
        name = "lisi";
        System.out.println(person.getName());
        person.setAge(18);
        System.out.println(person);
        person.SetAddress("北京", "上海", "广州");

        //多态，父类引用指向子类对象
        Person p1 = new Student(18, "lisi", 100);
        //使用instanceof可以判断是不是某种类型或者该类型的子类
        System.out.println(p1 instanceof Person);
        System.out.println(p1 instanceof Student);
        System.out.println(p1 instanceof Teacher);
        // java 14开始，判断instanceof后可以直接转型为指定变量，避免再次强转
        Object obj = "Hello";
        if(obj instanceof String s){
            System.out.println(s.toUpperCase());
        }
        // java实例方法调用时基于运行时的实际类型的动态调用，而非变量的声明类型，这就是多态
        Person p2 = new Student(18, "lisi", 100);
        p2.test();


    }
}
