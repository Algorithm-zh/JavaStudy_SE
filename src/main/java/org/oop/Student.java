package org.oop;

//加了final后任何类都不能继承它了
//可以再加sealed来指定继承的类或者加final限制它
public sealed class Student extends Person permits Teacher{
    public int getScore() {
        return score;
    }

    public Student(int score) {
        this.score = score;
    }

    public Student(int age, String name, int score) {
        super(age, name);//子类构造必须先调用父类的构造方法
        this.score = score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String Hello(){
       return "hello, " + super.name;
    }

    private int score;

    @Override
    public void test(){
        System.out.println("Student.test");
    }

    @Override
    public void run() {
        System.out.println("运行");
        Hello hello = new Hello();
        hello.hi();
    }

    public class Hello{
        public void hi(){
            score = 100;
            System.out.println("我是内部类");
        }
    }
}
