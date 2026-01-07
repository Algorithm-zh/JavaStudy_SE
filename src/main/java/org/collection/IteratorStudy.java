package org.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IteratorStudy {
    public static void test(){
        ReverseList<Integer> list = new ReverseList<>();
        List<Integer> l = Arrays.asList(1, 2, 3, 4);
        list.addAll(l);
        System.out.print("反转序列:");
        for (Integer i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
//想要使用for each循环，集合类需要实现Iterable接口,然后返回一个Iterator对象
class ReverseList<T> implements Iterable<T> {
    private List<T> list = new ArrayList<>();
    public void add(T t){
        this.list.add(t);
    }
    public void addAll(List<T> list){
        this.list.addAll(list);
    }

    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator(list.size());
    }

    //使用内部类实现Iterator接口,这个内部类可以直接访问外部类的所有字段和方法
    class ReverseIterator implements Iterator<T>{
        int index;

        public ReverseIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            index --;
            return ReverseList.this.list.get(index);
        }
    }
}


