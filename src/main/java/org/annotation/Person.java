package org.annotation;

//定义一个JavaBean
public class Person {
    @Range(min = 1, max = 100)
    public String name;

    @Range(max = 10)
    public String city;

    public Person(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Person() {
    }
}
