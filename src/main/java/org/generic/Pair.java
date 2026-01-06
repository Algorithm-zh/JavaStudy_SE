package org.generic;

//泛型类
//可以设定extends来限制T类型，让其只能是某种类型或其子类
public class Pair<T extends Object,  U> {
    private T first;
    private U last;
    public Pair(T first, U last) {
        this.first = first;
//        this.first = new T(); 无法实例化T类型，可以通过下面反射的方式实现
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
    static int add(Pair<? extends Number, ? extends Number> p){
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
}