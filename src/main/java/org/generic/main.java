package org.generic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//Java里的泛型是使用擦拭法实现的
//所谓擦拭法是指，虚拟机对泛型其实一无所知，所有的工作都是编译器做的。
//java的泛型就是个语法糖，并不是真正的泛型，c++的泛型是真正的泛型，两者的区别
/*
       对比维度	    Java 泛型	                                                        C++ 模板
       核心实现机制	类型擦除。编译后将类型信息替换为Object或指定上界，运行时无实际类型参数信息。	编译时代码生成（或模板实例化）。编译器为每个使用的不同类型生成一份独立的代码副本。
       类型参数约束	类型安全，支持编译时检查。类型参数只能是引用类型，不能是基本类型。	        功能兼容。只要类型支持模板中的操作（如运算符、方法），即可编译通过，也被称为“鸭子类型”。
       代码与实例	无论用何种具体类型，在JVM中只有一份原始类（如List）的字节码。              对每个不同类型，编译器都会生成一份独立的目标代码。vector<int>和vector<string>是完全不同的类型。
                    List<String>和List<Integer>在运行时的类是相同的。
       特化/通配符	不支持模板特化，但支持使用?、? extends T、? super T等通配符来增加灵活性。	支持模板特化，可以为特定类型提供特殊实现。本身不支持类似Java的通配符。
       静态成员	    类的静态成员在所有泛型实例间共享。MyClass<T>的不同T共享同一个静态变量。	类的静态成员在不同模板实例间是独立的。MyClass<int>和MyClass<string>拥有各自独立的静态变量副本。
*/
//说白了，它的实现方式就是在编译期间保留类型信息，然后运行的时候就去掉类型信息，运行的时候都是同一个类，c++都是不同的类，一个类生成一版副本
//所以就产生了几个缺点：
/*
    1.无法进行运行时的类型检查，比如obj instanceof T
    2.无法创建泛型数组
    3.无法重载泛型方法(擦除后两个方法定义完全一样)
    4.静态成员共享（c++是每个类都有自己的静态变量副本）
    5.T不能是基础类型，因为Object无法持有基础类型
    6.无法取得带泛型的Class
    7.无法实例化T类型(但是可以通过反射来实现)
 */
//一个很重要的问题，比如你在泛型类里写了equals方法，这会报错，因为擦除后会变成Object，Object本身就有这个方法,编译器会阻止这种覆写行为
public class main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //为什么要有泛型
        ArrayList list = new ArrayList();
        list.add("mojap");
        String f = (String) list.getFirst();
        System.out.println(f);
//        Integer s = (Integer) list.get(0);
//        System.out.println(s);
        //从上面的代码就可以总结出来没有泛型的问题，1.需要强制转型，2.容易出错
        /*
            泛型就是编写模板代码来适应任意类型；
            泛型的好处是使用时不必对类型进行强制转换，它通过编译器对类型进行检查；
            注意泛型的继承关系：可以把ArrayList<Integer>向上转型为List<Integer>（T不能变！），但不能把ArrayList<Integer>向上转型为ArrayList<Number>（T不能变成父类）。
         */

        //无需强制转换，无编译器警告
        List<String> l = new ArrayList<>();
        l.add("hh");
        String h = l.getFirst();
        System.out.println(h);

        Person[] ps = new Person[]{new Person("eb", 18), new Person("cb", 18)};
        Arrays.sort(ps);
        System.out.println(Arrays.toString(ps));

        Pair<String, Integer> p = Pair.makePair("mojap", 18);
        System.out.println(p);

        Pair<String, Person> p2 = new Pair(String.class, Person.class);
        p2.setFirst("mojap");
        p2.setLast(ps[0]);
        System.out.println(p2);


        //子类获取父类泛型类型(正常情况不可以通过反射得出T的类型，但是子类可以获取父类的)
        //下面就是能获取的原因，因为正常情况下过来编译期就擦除了，但是子类继承父类的话就得保留到class文件里，所以我们就能反射获取了
        //在父类是泛型类型的情况下，编译器就必须把类型T（对IntPair来说，也就是String类型）保存到子类的class文件中，不然编译器就不知道IntPair只能存取String这种类型。
        Class<IntPair> clazz = IntPair.class;
        Type t = clazz.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] types = pt.getActualTypeArguments(); // 可能有多个泛型类型
            Type firstType = types[0]; // 取第一个泛型类型
            Class<?> typeClass = (Class<?>) firstType;
            System.out.println(typeClass); // Integer
        }

        //通配符部分
        System.out.println(PairHelper.add(new Pair<Integer, Integer>(1, 2)));
    }
}
