package org.generic;

import java.util.List;
import java.util.function.Supplier;

//泛型类
//可以设定extends来限制T类型，让其只能是某种类型或其子类
public class Pair<T extends Object,  U> {
    private T first;
    private U last;
    public Pair(T first, U last) {
        this.first = first;
//        this.first = new T(); 无法实例化T类型，可以通过下面反射的方式实现
        //不能实例化的原因：
        /*
            运行时他不知道他是啥类型，因为都被擦除成了Object类型(或自己设定的类上限)
            1.构造函数可能不能访问
            2.T可能是接口或者抽象类
            3.T可能没有
         */
        this.last = last;
    }
    //通过反射实现实例化
    public Pair(Class<T> c1, Class<U> c2){
        try {
            this.first = c1.getDeclaredConstructor().newInstance();
            this.last = c2.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //supplier就是一个能创建对象的方法
    // 使用 Supplier 的构造方法
    public Pair(Supplier<T> firstSupplier, Supplier<U> lastSupplier) {
        this.first = firstSupplier.get();  // 调用 Supplier 来创建实例
        this.last = lastSupplier.get();
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public void setLast(U last) {
        this.last = last;
    }

    public U getLast() {
        return last;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", last=" + last +
                '}';
    }

    //静态泛型方法应该使用其他类型区分（没法直接访问T） 静态泛型类型
    public static <K, V> Pair<K, V> makePair(K first, V last) {
        return new Pair<K, V>(first, last);
    }

}
class IntPair extends Pair<String, String> {
    public IntPair(String first, String last) {
        super(first, last);
    }
}

//通配符extends, 这样可以使得方法接受所有泛型类型为Number或Number子类
//<? extends Number>这种叫做上界通配符
class PairHelper{
    static int get(Pair<? extends Number, ? extends Number> p){
//        p.setFirst(new Integer("12"));//错误
        /*
            编译器看到 Pair<? extends Number> 时，它只知道：
            getFirst() 返回类型是 ? extends Number，所以可以用 Number 接收 ✅
            setFirst(? extends Number) 的参数类型是 ? extends Number，但因为 ? 不确定，除了 null 之外，不能安全地传入任何具体的 Number 子类型对象 ❌
            为什么 null 可以？因为 null 是所有引用类型的通用值。

            比如我传add调用的时候是Double，那我在这里写Integer，类型都不一样了
         */
        //因此写成这样就可以直接的认定为这是个只读方法，无法写入
        return p.getFirst().intValue() + p.getLast().intValue();
    }
//通配符super，这样可以接受所有泛型类型为Number或Number父类
//<? super Number>这种叫做下界通配符
    static void add(Pair<? super Number, ? super Number> p){
        //这样写是错误的，因为他传入的类型是Number的父类，get并不知道他是什么类型，可以是Object类型，然后它传个String进来，你不能用Integer来接受
        //Integer s = (Integer) p.getLast();

        //而set是可以的，因为这里设置值肯定就是Number类型，那它的父类肯定都可以接受
        p.setFirst(12);
        p.setLast(999);
    }
    //总结就是extends对参数只读，super对参数只写
    //PECS原则：Producer Extends Consumer Super

    //一个很好的体现了两个通配符的使用的例子就是标准库里的copy
    public static <T> void copy(List<? extends T> src, List<? super T> dst){
        dst.addAll(src);//dst是生产者，src是消费者
    }

    //TODO:泛型重要概念
//    协变
//    协变允许一个类型是另一个类型的子类型，在泛型或数组中，表现为子类型可以安全地赋值给父类型。例如，Integer 是 Number 的子类型，在协变的情况下，List<Integer> 可以在某些情况下被视为 List<? extends Number>。
//    逆变
//    逆变与协变相反，它允许一个类型是另一个类型的超类型。在泛型中，通过下界通配符实现，例如 List<? super Integer> 可以接受 List<Number> 或 List<Object> 等。

}
