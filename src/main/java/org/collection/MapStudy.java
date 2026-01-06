package org.collection;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MapStudy {

    public static void test() throws IOException {
        Map<String ,Student> map = new HashMap<>();
        map.put("Xiao Ming", new Student("Xiao Ming", 100));
        map.put("lisi", new Student("lisi", 100));
        var s1 = map.get("Xiao Ming");
        System.out.println(s1);
        //不存在重复的key，重复放入只会把前一个value给改掉
        //遍历方式1，使用keySet()方法返回set集合,里面包含不重复的key集合
        for (String key : map.keySet()) {
            Student value = map.get(key);
            System.out.println(key + " = " + value);
        }
        System.out.println("==================================");
        //遍历方式2,使用for each遍历entrySet集合，它包含key和value
        for (Map.Entry<String, Student> ss : map.entrySet()) {
            System.out.println(ss.getKey() + " = " + ss.getValue());
        }

        //Hashmap是如何根据key来计算得到value存储的索引的？
        //通过key计算索引的方式就是调用key对象的hashCode()方法，它返回一个int整数。HashMap正是通过这个方法直接定位key对应的value的索引，继而直接返回value
        //因此，正确使用Map必须保证：
        /*
            1.作为key的对象必须正确覆写equals()方法，相等的两个key实例调用equals()必须返回true；
            2.作为key的对象还必须正确覆写hashCode()方法，且hashCode()方法要严格遵循以下规范：
            如果两个对象相等，则两个对象的hashCode()必须相等；
            如果两个对象不相等，则两个对象的hashCode()尽量不要相等。
         */
        Map<Student, String> map1 = new HashMap<>();
        map1.put(new Student("Xiao Ming", 100), "Xiao Ming");
        map1.put(new Student("lisi", 100), "lisi");
        System.out.println(map1.containsKey(new Student("Xiao Ming", 100)));

        //EnumMap内部是一个非常紧凑的数组存储value,根据enum类型的key直接定位到内部数组的索引，不需要计算hashcode


        //SortedMap保证遍历时以key的顺序来进行排序,这是个接口，底层实现类是TreeMap
        //放入的key必须实现Comparable接口,如果作为Key的class没有实现Comparable接口，那么，必须在创建TreeMap时同时指定一个自定义排序算法
        Map<Student, String> sm = new TreeMap<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                //相等的时候返回0，大返回1，小返回-1
                return o1.name.compareTo(o2.name);
            }
        });
        sm.put(new Student("Xiao Ming", 100), "Xiao Ming");
        sm.put(new Student("lisi", 100), "lisi");
        sm.put(new Student("Abc", 100), "Abc");
        System.out.println(sm);


        //使用Map存储配置文件配置
        // Properties设计的目的是存储String类型的key－value，但Properties实际上是从Hashtable派生的，它的设计实际上是有问题的，但是为了保持兼容性，现在已经没法修改了。
        // 除了getProperty()和setProperty()方法外，还有从Hashtable继承下来的get()和put()方法，这些方法的参数签名是Object，我们在使用Properties的时候，
        // 不要去调用这些从Hashtable继承下来的方法。
        Properties props = new Properties();//内部就是Hashtable(历史遗留问题)
        //编译后，resources 目录下的文件会被复制到 classpath 根目录
        props.load(MapStudy.class.getResourceAsStream("/log4j.properties"));

        String console = props.getProperty("log4j.appender.Console");
        String layout = props.getProperty("log4j.appender.Console.layout.ConversionPattern");
        System.out.println(console + " ========= " + layout);

        //写完后，可以使用store再写入配置文件里
    }
}
class Student {
    public String name;
    public int score;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
    /*
        编写equals()和hashCode()遵循的原则是：
        equals()用到的用于比较的每一个字段，都必须在hashCode()中用于计算；equals()中没有使用到的字段，绝不可放在hashCode()中计算。
        另外注意，对于放入HashMap的value对象，没有任何要求。
     */
    //HashMap的机制
    /*
        1.默认数组大小是16，无论hashcode()多大，都可以通过 int index = key.hashCode() & 0xf;来将其映射到0-15
        2.如果添加数量超过了16，自动翻倍扩容
        //扩容会重新分布key-value,影响性能，所以最好一开始就指定我们需要的容量, hashmap内部数组长度是2的幂次方，因此它会是我们指定长度的最近的2的幂次方
        3.如果hashcode()相同，那就会在同一个位置上加个List，这个List里面存放的都是这个hashcode()相同的对象, 所以这个很长的情况下也会影响性能
        如果两个对象不相等，则两个对象的hashCode()尽量不要相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return score == student.score && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
}