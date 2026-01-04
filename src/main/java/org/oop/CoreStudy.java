package org.oop;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

public class CoreStudy {

    //定义枚举类
    enum Weekday {
        MON(1, "星期一"), TUE(2, "星期二"), WED(3, "星期三"), THU(4, "星期四"), FRI(5, "星期五"), SAT(6, "星期六"), SUN(0, "星期日");

        public final int dayValue;
        private final String chinese;

        private Weekday(int dayValue, String chinese) {
            this.dayValue = dayValue;
            this.chinese = chinese;
        }

        //使用toString()方法重写,比使用name更加灵活
        @Override
        public String toString() {
            return this.chinese;
        }
    }
    public void StringStudy(){
        System.out.println("-------------String--------------");
        //java的string是不可变类型，使用final修饰
        String str = "hello,java,cc";
        String[] ss = str.split(",");
        for (String s : ss) {
            System.out.print(s + " ");
        }
        System.out.println();

        //替换
        String out = str.replace("java", "python");
        System.out.println(out);

        //拼接
        String sss = String.join("***", ss);
        System.out.println(sss);

        //格式化字符串
        String s = "Hi %s, your score is %d !";
        System.out.println(s.formatted("Alice", 80));

        //任意类型转为字符串
        String.valueOf(123);

        //字符串转为其它类型
        int n1 = Integer.parseInt("123");

        //注意Integer的getInteger不是将字符串转为int,而是将对应系统变量转为Integer
        Integer v = Integer.getInteger("java.version");

        //较新的jdk String底层是用byte[]实现的，较旧的jdk String底层是用char[]实现的

        //java的char和cpp的char的区别
        //无符号0-65535 有符号-128-127
        //unicode ascii
        //如果在c++里处理unicode，应该使用char16_t  char32_t或其它库


        String s1 = "hello";
        String s2 = "hello";
        System.out.println(s1 == s2);
        //为什么上面会相同，因为Java编译器在编译期，会自动把所有相同的字符串当作一个对象放入常量池，自然s1和s2的引用就是相同的

    }
    public void StringBuilderStudy(){
        System.out.println("----------------------StringBuilder-------------------");
        //为什么要有StringBuilder,因为String是不可变的，一些操作会导致创建新的对象，效率较低
        //而StringBuilder是可变的，可以预分配缓冲区，所以不会创建新的临时对象
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 5; i++) {
            sb.append(',');
            sb.append(i);
        }
        String s = sb.toString();
        System.out.println(s);
        //链式操作
        sb.append(" hh")
                .append(" hello")
                .append(" world");
        System.out.println(sb.toString());

//      注意：对于普通的字符串+操作，并不需要我们将其改写为StringBuilder，
//      因为Java编译器在编译时就自动把多个连续的+操作编码为StringConcatFactory的操作。
//      在运行期，StringConcatFactory会自动把字符串连接操作优化为数组复制或者StringBuilder操作

        //StringBuffer是builder线程安全版本，但是同步执行速度慢,没有必要使用


//        高效拼接字符串最好使用StringBuilder
        String[] names = {"Alice", "Bob", "Tom"};
        var sb1 = new StringBuilder();//var用于局部变量类型推导，不能用于类成员变量，方法参数或返回值
        sb1.append("hello ");
        for (String name : names){
            sb1.append(name).append(", ");
        }
        sb1.delete(sb1.length() - 2, sb1.length());
        sb1.append("!");
        System.out.println(sb1.toString());

        //StringJointer专门可以事先设置拼接字符
        //还可以在第二个参数第三个参数上设置开头和结尾
        var sj = new StringJoiner(", ", "Hello ", "!");
        for(String name : names){
            sj.add(name);
        }
        System.out.println(sj.toString());

//        String 内部方法join,在内部使用了StringJointer,在不需要指定开头和结尾的时候更方便
        var sj1 = String.join(", ", names);
        System.out.println(sj1);

//        使用StringJointer构造一个SELECT语句
        String[] columns = {"id", "name", "age"};
        String table = "user";
        String select = buildSelectSql(columns, table);
        System.out.println(select);
    }
    static String buildSelectSql(String[] columns, String table){
        var sj = new StringJoiner(" ", "SELECT ", " FROM " + table);
        for(String column : columns){
            sj.add(column);
        }
        return sj.toString();
    }

    public void PackageTypeStudy(){
        System.out.println("----------------------PackageType-----------------");
//        基本类型	对应的引用类型
//        boolean	java.lang.Boolean
//        byte	    java.lang.Byte
//        short	    java.lang.Short
//        int	    java.lang.Integer
//        long	    java.lang.Long
//        float	    java.lang.Float
//        double	java.lang.Double
//        char	    java.lang.Character
        int i = 100;
        // 通过静态方法valueOf(int)创建Integer实例:
        Integer n2 = Integer.valueOf(i);
        // 通过静态方法valueOf(String)创建Integer实例:
        Integer n3 = Integer.valueOf("100");
        System.out.println(n3.intValue());

//        自动装箱 Auto Boxing
        //编译器可以帮我们自动在int和Integer直接转换
        Integer n4 = 100;
        int x = n4;
        //装箱在编译阶段执行，影响执行效率,因为编译后的class代码严格区分基本类型和引用类型

//        所有包装类都是不变类,final修饰
        Integer y = 127;
        Integer z = 127;
//        为什么是true，因为编译器自动把这个变为了Integer y = Integer.valueOf(127);
//        为了节省内存，对于较小的数，Integer.valueOf()会返回相同的实例
        System.out.println("y == z:" + (y == z));

//        创建新对象时优先使用静态工厂方法而不是new操作符,可以返回缓存的实例而不是一直都是新实例
        //把能创建“新”对象的静态方法称为静态工厂方法
        //方法1：Integer n = new Integer(100); new
        //方法2：Integer n = Integer.valueOf(100); 静态工厂

//        包装类里的静态变量
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        int sizeOfLong = Long.SIZE;
        int bytesOfLong = Long.BYTES;


        // 向上转型为Number:
        Number num = Integer.valueOf(999);
        // 获取byte, int, long, float, double:
        byte b = num.byteValue();
        int n = num.intValue();
        long ln = num.longValue();
        float f = num.floatValue();
        double d = num.doubleValue();
    }

    public void JavaBeanStudy(){
        //说白了就是字段是xyz
        //那么读方法是getXyz()
        //写方法setXyz()
        //这就是java病
    }

    //enum类和其它class的区别：
    //没有区别，但是有几个特点：
    //1.定义的enum类型总是继承自java.lang.Enum,无法被继承
    //2.只能定义enum的实例，无法通过new创建enum实例
    //3.定义的每个实例都是引用类型的唯一实例
    //4.可以将enum用于switch
    public void EnumStudy(){
        System.out.println("--------------------Enum----------------------");
        int day = 3;
        Weekday x = Weekday.FRI;
        if(x == Weekday.FRI){
            System.out.println("今天是周三");
        }
        //使用枚举类的好处：
        //enum常量本身带有类型的信息，即Weekday.SUN类型是Weekday
        //所以下面这种肯定编译不过
        //if(day == Weekday.SUN){}
        //这样可在编译期就自动检查出所有可能的潜在错误

        //返回常量名
        System.out.println(x.name());
        //使用toString()主要是为了可读性，判断枚举常量的名字一定要用name
        System.out.println(x.chinese);
    }
    //如果做自己定义枚举类Color
//    public enum Color {
//        RED, GREEN, BLUE;
//    }
    //编译出来的是
//    public final class Color extends Enum { // 继承自Enum，标记为final class
//        // 每个实例均为全局唯一:
//        public static final Color RED = new Color();
//        public static final Color GREEN = new Color();
//        public static final Color BLUE = new Color();
//        // private构造方法，确保外部无法调用new操作符:
//        private Color() {}
//    }


//    不变类的特点 1.final修饰，无法派生子类， 2.每个字段使用final
    //record定义的都是不变类(String Integer这些都是不变类）
    //record定义类相当于定义了一个final类，不可被继承，然后每个变量都是private final
    //并且会帮你重写toString(), equals(), hashCode() getter()方法
    record Point(int x, int y){
        //可以自己重写构造方法来加上特定逻辑
        public Point{
            if(x < 0 || y < 0){
                throw new IllegalArgumentException("x, y must be >= 0");
            }
        }
    }

    public void RecordStudy(){
        System.out.println("--------------------------Record---------------------------");
        Point p = new Point(1, 2);
        System.out.println(p.toString());
    }

    public void BigIntegerStudy(){
        System.out.println("-------------------------BigInteger----------------------------");
        //java最长支持64位long型整数，可以直接通过cpu指令计算，速度非常快
        //如果超了，就需要软件模拟，速度慢，使用BigInteger来实现
        BigInteger bi = new BigInteger("123456789");
        bi = bi.pow(10);
        System.out.println(bi);
        float f = bi.intValue();//转换为基本类型,如果溢出了结果就会出错，可以用下面的方法检查，并抛出异常
        try{
            f = bi.intValueExact();
        } catch (ArithmeticException e){//转换时超出范围会抛出这个异常
            System.out.println("超出范围");
        }
        System.out.println(f);
    }

    public void BigDecimalStudy(){
        System.out.println("-----------------------BigDecimal--------------------------");
        BigDecimal bd = new BigDecimal("123456789.123456789000");
        System.out.println(bd);
        BigDecimal bd2 = bd.stripTrailingZeros();//去掉末尾的0
        System.out.println(bd2);

        //设置四舍五入
        BigDecimal bd3 = bd.setScale(2, RoundingMode.HALF_UP);
        System.out.println(bd3);

        //除法时需要设置无法除尽时的截断
        BigDecimal bd4 = bd.divide(new BigDecimal("123213.12334"), 10, RoundingMode.HALF_UP);
        System.out.println(bd4);

        //在比较两个BigDecimal的值是否相等时，要特别注意，使用equals()方法不但要求两个BigDecimal的值相等，还要求它们的scale()相等
        BigDecimal d1 = new BigDecimal("123.456");
        BigDecimal d2 = new BigDecimal("123.45600");
        System.out.println(d1.equals(d2)); // false,因为scale不同
        System.out.println(d1.equals(d2.stripTrailingZeros())); // true,因为d2去除尾部0后scale变为3
        System.out.println(d1.compareTo(d2)); // 0 = 相等, -1 = d1 < d2, 1 = d1 > d2
        //如果查看BigDecimal的源码，可以发现，实际上一个BigDecimal是通过一个BigInteger和一个scale来表示的，即BigInteger表示一个完整的整数，而scale表示小数位数
    }


    //常用工具类
    public void ToolStudy(){
        //Math
        System.out.println("----------------------------ToolStudy---------------------------");
        System.out.println(Math.abs(-100));
        System.out.println(Math.max(100, 20));
        System.out.println(Math.log(4));//e为底
        System.out.println(Math.random());

        //Random
        //伪随机数，只要给定一个初始的种子，产生的随机数序列完全一样
        Random r = new Random();//如果不给种子，会默认用系统当前时间戳作为种子
        System.out.println(r.nextInt());

        //SecureRandom
        //真随机数，不可预测的安全的随机数，无法指定种子，通过RNG算法实现
//        需要使用安全随机数的时候，必须使用SecureRandom，绝不能使用Random！
        SecureRandom sr = null;
        try{
            sr = SecureRandom.getInstanceStrong();//高强度安全随机数生成器
        }catch(NoSuchAlgorithmException e){
            sr = new SecureRandom();//普通安全的随机数生成器
        }
        byte[] buffer = new byte[16];
        sr.nextBytes(buffer);
        System.out.println(Arrays.toString(buffer));
    }
}