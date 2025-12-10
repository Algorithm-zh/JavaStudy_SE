package org.oop;

//如果抽象类没有字段并且所有方法都是抽象的
//可以直接改写为接口
//接口类的所有方法默认都是public abstract
public interface Vehicle {
    void run();
    void running();
    //接口里可以定义default方法
    //default的目的是，当需要给接口新增一个方法时，子类要全部修改，
    //但是如果新增的是default，子类就不必全部修改
    //接口类没有字段，所以它无法访问字段，这和抽象类的普通方法不同
    default void name(){
        System.out.println("Vehicle.name");
    }
}
