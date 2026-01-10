package org.lambda;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

public class StreamStudy {
    /*
    Java从8开始，不但引入了Lambda表达式，还引入了一个全新的流式API：Stream API。它位于java.util.stream包中。
    划重点：这个Stream不同于java.io的InputStream和OutputStream，它代表的是任意Java对象的序列。两者对比如下：
            java.io	            java.util.stream
    存储	顺序读写的byte或char	顺序输出的任意Java对象实例
    用途	序列化至文件或网络	内存计算／业务逻辑
    Stream和List也不一样，List存储的每个元素都是已经存储在内存中的某个Java对象，而Stream输出的元素可能并没有预先存储在内存中，而是实时计算出来的。

    换句话说，List的用途是操作一组已存在的Java对象，而Stream实现的是惰性计算，两者对比如下：
            java.util.List	        java.util.stream
    元素	已分配并存储在内存	    可能未分配，实时计算
    用途	操作一组已存在的Java对象	惰性计算

    Stream API的特点是：
    1.Stream API提供了一套新的流式处理的抽象序列；
    2.Stream API支持函数式编程和链式操作；
    3.Stream可以表示无限序列，并且大多数情况下是惰性求值的。
     */
    //比如我们要表示一个全体自然数的集合，list无法写出来，因为自然数无限,stream可以
    /*
    int result = createNaturalStream() // 创建Stream 不计算
            .filter(n -> n % 2 == 0) // 任意个转换 不计算
            .map(n -> n * n) // 任意个转换 不计算
            .limit(100) // 任意个转换 不计算
            .sum(); // 最终计算结果 计算 ,上面只存储了转换规则，没有任何计算
     */
    //Stream主要有两类操作，转换和聚合，区分，转换不会触发任何计算
    //聚合操作是真正需要从Stream请求数据的，对一个Stream做聚合计算后，结果就不是一个Stream，而是一个其他的Java对象。


    //创建Stream
    @Test
    public void test1(){
        //1.Stream.of()
        Stream<String> stream = Stream.of("a", "b", "c");
        stream.forEach(System.out::println);
        //2.基于数组或Collection
        Stream<String> stream2 = Arrays.stream(new String[]{"a", "b", "c"});
        stream2.forEach(System.out::println);
        //3.基于Supplier, 这方法创建的stream会不断调用Supplier.get()产生下个元素，所以Stream保存的是算法，可以用来表示无限序列
        Stream<Integer> natual = Stream.generate(new NatualSupplier());
        //对于无限序列，如果直接调用forEach()或者count()这些最终求值操作，会进入死循环
        //无限序列必须先变成有限序列再打印
        natual.limit(20).forEach((n)->{System.out.print(n + " ");});
        //还有其它方法是可以通过一些api的接口
        //比如遍历文件内容
        try(Stream<String> lines = Files.lines(Paths.get("D:\\log4j.properties"))){
            lines.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //为了防止基本类型无法使用泛型的问题，而包装类又有拆箱装箱的资源消耗问题，所以提供了
        //IntStream, LongStream, DoubleStream
    }


    //使用map, 可以将一个Stream转换为另一个Stream
    @Test
    public void test2(){
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        var stream2 = stream.map(n -> n * n);
        stream2.forEach(System.out::println);
        List.of(" Apple", "pear  ", "ORANGE ")
                .stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .forEach(s->System.out.print(s + " "));
        System.out.println();
    }


    //使用filter
    /*
        //filter接收的对象
        @FunctionalInterface
        public interface Predicate<T> {
            // 判断元素t是否符合条件:
            boolean test(T t);
        }
     */
    @Test
    public void test3(){
        Stream.generate(new LocalDateSupplier())
                .limit(31)
                .filter(ldt -> ldt.getDayOfWeek() == DayOfWeek.SATURDAY || ldt.getDayOfWeek() == DayOfWeek.SUNDAY)
                .forEach(System.out::println);

    }


    //使用reduce, 可以将所有元素按照聚合函数聚合成一个结果
    /*
    reduce传入的对象
    @FunctionalInterface
    public interface BinaryOperator<T> {
        // Bi操作：两个输入，一个输出
        T apply(T t, T u);
    }
     */
    @Test
    public void test4(){
        int sum = Stream.of(1, 2, 3, 4, 5).reduce(0, (acc, n) -> acc + n);//0是初始值，如果不设置初始值的话就得返回Optional判断是否为空
        System.out.println(sum);
        //将配置文件的key=value转换成Map
        List<String> props = List.of("profile=native", "debug=true", "logging=warn", "interval=500");
        Map<String, String> map = props.stream()
                .map(s -> {
                    String[] as = s.split("\\=", 2);
                    return Map.of(as[0], as[1]);
                })
                .reduce(new HashMap<String, String>(), (m, v) -> {
                    m.putAll(v);
                    return m;
                });
        map.forEach((k, v)->System.out.println(k + "=" + v));
    }


    //输出集合
    @Test
    public void test5(){
        //1.输出为List
        Stream<String> stream = Stream.of("Apple", "Orange", "", null, "Lemon");
        List<String> list = stream.filter((s) -> s != null && !s.isBlank()).collect(Collectors.toList());
        System.out.println(list);

        //2.输出为map
        Stream<String> stream1 = Stream.of("APPLE:apple", "MSFT:Microsoft");
        Map<String, String> map = stream1.collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
        System.out.println(map);

        //3.输出为数组
        List<String> list2 = List.of("Apple", "Banana", "Orange");
        String[] array = list.stream().map(String::toLowerCase).toArray(String[]::new);
        System.out.println(Arrays.toString(array));
    }

    //输出集合：分组输出
    @Test
    public void test6(){
        List<String> list = List.of("Apple", "Banana", "Blackberry", "Coconut", "Avocado", "Cherry", "Apricots");
        //分组输出使用Collectors.groupingBy()，它需要提供两个函数：一个是分组的key，这里使用s -> s.substring(0, 1)，
        //表示只要首字母相同的String分到一组，第二个是分组的value，这里直接使用Collectors.toList()，表示输出为List
        Map<String, List<String>> collect = list.stream().collect(Collectors.groupingBy(s -> s.substring(0, 1), Collectors.toList()));
        System.out.println(collect);
    }

    //其它操作
    @Test
    public void test7(){
        //排序
        List<String> list = List.of("Orange", "apple", "Banana");
        List<String> list1 = list.stream().sorted().collect(Collectors.toList());
        System.out.println(list1);
        //去重
        List<String> list2 = list.stream().distinct().collect(Collectors.toList());
        System.out.println(list2);
        //截取
        List<String> list3 = list.stream().skip(1).limit(1).collect(Collectors.toList());
        System.out.println(list3);
        //flatMap:是指把Stream的每个元素（这里是List）映射为Stream，然后合并成一个新的Stream
        Stream<List<Integer>> s = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));
        Stream<Integer> i = s.flatMap(l -> l.stream());
        i.forEach(n -> System.out.print(n + " "));
        System.out.println();
        //合并，将两个stream合起来
        Stream<String> s1 = List.of("A", "B", "C").stream();
        Stream<String> s2 = List.of("D", "E").stream();
        Stream<String> ss = Stream.concat(s1, s2);
        System.out.println(ss.collect(Collectors.toList())); // [A, B, C, D, E]
        //并行
        //通常情况下，对Stream的元素进行处理是单线程的，即一个一个元素进行处理。但是很多时候，我们希望可以并行处理Stream的元素，因为在元素数量非常大的情况，并行处理可以大大加快处理速度。
        String[] array = list.stream().parallel().sorted().toArray(String[]::new);
        System.out.println(Arrays.toString(array));

    }
}

class NatualSupplier implements Supplier<Integer>{

    int n = 0;
    @Override
    public Integer get() {
        return ++n;
    }
}

class LocalDateSupplier implements Supplier<LocalDate>{
    LocalDate start = LocalDate.of(2022, 1, 1);
    int n = -1;
    @Override
    public LocalDate get() {
        return start.plusDays(++n);
    }
}