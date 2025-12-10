package org.oop;

//java15开始，可以使用sealed修饰class通过permits限制能从class继承的子类名称
public sealed abstract class Person permits Student{
    private int age;

    public Person() {
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    private String[] names;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    //可变参数，相当于数组,可以传任意个数个元素
    //如果写成string[]也可以，但是调用方需要自己先构造String[]
    public void SetAddress(String... names){
        this.names = names;
    }

    public String[] GetAddress(){
        return names;
    }

    public void test(){
        System.out.println("test");
    }

    //定义为抽象方法，继承它的类必须实现
    //类也要抽象定义
    public abstract void run();
}
