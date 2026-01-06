package org.collection;

import java.util.*;

public class ListStudy {

    //ArrayList是数组，LinkedList是链表
    public static void test(){
        //使用of根据给定的元素快速创建List
        List<Integer> list = List.of(1, 2, 3, 4);
        //比那里最好使用迭代器而不是索引
        for(Iterator<Integer> it = list.iterator(); it.hasNext();){
            System.out.print(it.next() + " ");
        }
        System.out.println();
        //或者使用foreach，会自动转为迭代器


        //List转Array  给toArray(T[])传入一个类型相同的Array, list内部自动把元素复制到传入的Array里
        Integer[] array = list.toArray(new Integer[3]);

        //Array转List
        List<Integer> list1 = List.of(array);//只读

        //从start到end里随机删一个数然后不按顺序放到list里，该怎么找到哪个缺了
        //两个都加起来，用第一个减去第二个就是少的
        int start = 20, end = 40, sum = 0;
        List<Integer> list2 = new ArrayList<>();
        for(int i = start; i <= end; i++){
            list2.add(i);
            sum += i;
        }
        list2.remove(4);
        for (Integer i : list2) {
            sum -= i;
        }
        System.out.println(sum);

        //使用List的contains方法的时候你就算传入不同的实例进去他也能比较，因为List的contains方法会调用equals方法，但前提是你得实现equals方法
        //所以如何编写equals? 必须满足一下条件
        /*
        前四条都是对于非null来说的
        1. 自反性：对于任意的x，x.equals(x)必须返回true
        2. 对称性：对于任意的x和y，如果x.equals(y)返回true，那么y.equals(x)必须返回true
        3. 传递性：对于任意的x，y和z，如果x.equals(y)返回true，y.equals(z)返回true，那么x.equals(z)必须返回true
        4. 一致性：对于任意的x和y，只要它们的equal方法被调用，那么返回值必须保持一致
        5. 对于任意的x，x.equals(null)必须返回false
         */
        Person[] p = new Person[2];
        p[0] = new Person("wangwu", 18);
        p[1] = new Person("lisi", 19);
        List<Person> list3 = Arrays.asList(p);//这个可以修改，但是不能改变元素数量，就是大小不能改变，包装的原始数组
//        List<Person> list3 = List.of(p); //这个生成的列表完全不可变，不能修改
        System.out.println(list3.contains(new Person("wangwu", 18)));
        list3.set(0, new Person("haha", 19));
        System.out.println(list3.contains(new Person("wangwu", 18)));
        System.out.println(list3);
    }
}

class Person{
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Person p){//java 14写法，检查类型的同时完成转型，不需要下面再强转
//            return this.name.equals(p.name) && this.age == p.age; //这种写法在this.name是null的时候会报错
            return Objects.equals(this.name, p.name) && this.age == p.age;//可以使用Objects.equals替代
        }
        return false;
        /*
        总结一下equals()方法的正确编写方法：
            1.先确定实例“相等”的逻辑，即哪些字段相等，就认为实例相等；
            2.用instanceof判断传入的待比较的Object是不是当前类型，如果是，继续比较，否则，返回false；
            3.对引用类型用Objects.equals()比较，对基本类型直接用==比较。
            使用Objects.equals()比较两个引用类型是否相等的目的是省去了判断null的麻烦。两个引用类型都是null时它们也是相等的。
            如果不调用List的contains()、indexOf()这些方法，那么放入的元素就不需要实现equals()方法。
         */
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}