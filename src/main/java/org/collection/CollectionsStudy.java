package org.collection;

import java.util.*;

public class CollectionsStudy {
    public static void test(){
        //创建空集合
        Collections.emptyList();
        //JDK9以上可以直接使用下面来创建空集合
        List.of();

        //创建单元素集合，不可变(无法添加或删除元素)
        Collections.singletonList(1);
        //JDK9以上可以直接使用下面来创建任意元素集合(也是不可变)
        Map.of(1,2,3,4);
        Set.of(1,2,3);

        //排序
        List<String> list = List.of("c", "b", "a");
//        Collections.sort(list);//错误，必须传入可变集合
        List<String> list2 = new ArrayList<>(list);
        Collections.sort(list2);
        System.out.println(list2);

        //洗牌,传入一个有序的List，可以随机打乱List内部顺序
        Collections.shuffle(list2);
        System.out.println(list2);


        //不可变集合，通过一组方法把可变集合封装为不可变集合
        //实现原理就是创建了个代理对象，然后这个对象的add set这些修改方法抛出异常
        List<String> im = Collections.unmodifiableList(list2);


    }
}
