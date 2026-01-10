package org.lambda;


import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

/*
函数式编程就是一种抽象程度很高的编程范式，纯粹的函数式编程语言编写的函数没有变量，因此，任意一个函数，只要输入是确定的，输出就是确定的，这种纯函数我们称之为没有副作用。
而允许使用变量的程序设计语言，由于函数内部的变量状态不确定，同样的输入，可能得到不同的输出，因此，这种函数是有副作用的。
函数式编程的一个特点就是，允许把函数本身作为参数传入另一个函数，还允许返回一个函数！
函数式编程最早是数学家阿隆佐·邱奇研究的一套函数变换逻辑，又称Lambda Calculus（λ-Calculus），所以也经常把函数式编程称为Lambda计算
函数式编程（Functional Programming）是把函数作为基本运算单元，函数可以作为变量，可以接收函数，还可以返回函数
 */
public class LambdaStudy {

    //只定义了单方法的接口称之为FunctionalInterface
    /*
    FunctionalInterface允许传入：
    1.接口的实现类（传统写法，代码较繁琐）；
    2.Lambda表达式（只需列出参数名，由编译器推断类型）；
    3.符合方法签名的静态方法；
    4.符合方法签名的实例方法（实例类型被看做第一个参数类型）；
    5.符合方法签名的构造方法（实例类型被看做返回类型）。
     */
    //比如Comparator或Callable
    @Test
    public void test1(){
        List<String> array = new ArrayList<>();
        array.add("b");
        array.add("a");
        array.add("e");
        array.add("d");
        //如果不是用函数式编程，排序方法需要这样写
        array.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(array);

        //洗牌一下
        Collections.shuffle(array);
        System.out.println(array);

        //使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码
        array.sort((o1, o2) -> o1.compareTo(o2));//返回值自动判断，参数类型省略，表达式简单甚至方法体也可以省略
        System.out.println(array);
    }


    //方法引用
    //所谓方法引用，是指如果某个方法签名和接口恰好一致，就可以直接传入方法引用
    @Test
    public void test2(){
        //因为Comparator<String>接口定义的方法是int compare(String, String)，和静态方法int cmp(String, String)相比，除了方法名外，方法参数一致，返回类型相同，
        // 因此，我们说两者的方法签名一致，可以直接把方法名作为Lambda表达式传入
        String[] array = new String[] { "Apple", "Orange", "Banana", "Lemon" };
        Arrays.sort(array, String::compareTo);
        System.out.println(String.join(", ", array));
        //为什么String::compareTo, 这个方法签名只有一个参数，为什么和int Comparator<String>.compare(String, String)能匹配呢
        //因为实例方法有一个隐含的this参数，相当于
        //public static int compareTo(String this, String o);

        //构造方法引用
        List<String> names = List.of("Bob", "Alice", "Tim");
        //如何转换为List<Person>?正常方法是定义List<Person>，for循环填充，但是我们可以引用Person构造方法
        List<Person> persons = names.stream().map(Person::new).collect(Collectors.toList());
        //map需要传入的函数接口定义为
        /*
        @FunctionalInterface
        public interface Function<T, R> {
            R apply(T t);
        }
         */
        //泛型一转换就是Person和String,方法签名一样
        System.out.println(persons);
    }

}
class Person {
    String name;
    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}