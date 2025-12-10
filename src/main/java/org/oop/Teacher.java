package org.oop;

/*
    final的作用:
    方法可以阻止被子类覆写
    class可以被阻止继承
    创建的field在创建对象时初始化，随后不可修改
 */
public final class Teacher extends Student {
    public Teacher(int score) {
        super(score);
    }

    public Teacher(int age, String name, int score) {
        super(age, name, score);
    }
}
