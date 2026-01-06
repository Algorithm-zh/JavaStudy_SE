package org.collection;

import java.util.*;

public class SetStudy {
    //Set用来存储不重复的元素集合, 就相当于不存储value的map
    //是用来去重的常用方式
    //add remove contains
    //最常用的是HashSet，它就是对HashMap做了一个简单封装
    /*
    public class HashSet<E> implements Set<E> {
        // 持有一个HashMap:
        private HashMap<E, Object> map = new HashMap<>();
        // 放入HashMap的value:
        private static final Object PRESENT = new Object();
        public boolean add(E e) {
            return map.put(e, PRESENT) == null;
        }
        public boolean contains(Object o) {
            return map.containsKey(o);
        }
        public boolean remove(Object o) {
            return map.remove(o) == PRESENT;
        }
    }
     */
    public static void test(){
        Set<String> set1 = new HashSet<>();//无序，因为没有实现SortedSet接口
        Set<String> set2 = new TreeSet<>();//有序，因为实现了SortedSet接口
        List<Message> received = List.of(
                new Message(1, "Hello!"),
                new Message(2, "发工资了吗？"),
                new Message(2, "发工资了吗？"),
                new Message(3, "去哪吃饭？"),
                new Message(3, "去哪吃饭？"),
                new Message(4, "Bye")
        );
        List<Message> displayMessages = process(received);
        for (Message message : displayMessages) {
            System.out.println(message.sequence + " " + message.text);
        }
    }


    //根据sequence去重
    static List<Message> process(List<Message> received) {
        //使用以下两种都可以，不过tree需要实现比较器，而hash需要实现equals和hashcode
        Set<Message> set = new TreeSet<>(new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return Integer.compare(o1.sequence, o2.sequence);
            }
        });
        Set<Message> set2 = new HashSet<>(received);
        return new ArrayList<>(set2);
    }
}
class Message {
    public final int sequence;
    public final String text;
    public Message(int sequence, String text) {
        this.sequence = sequence;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return sequence == message.sequence && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequence, text);
    }
}