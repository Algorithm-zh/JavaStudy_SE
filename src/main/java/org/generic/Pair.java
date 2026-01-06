package org.generic;

//泛型类
public class Pair<T,  U> {
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