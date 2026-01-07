package org.collection;

import java.util.*;

//Queue实际上是实现了一个先进先出（FIFO：First In First Out）的有序表
/*
api介绍,添加和获取都有两个方法，他们的行为不同
                    throw Exception	返回false或null
添加元素到队尾	    add(E e)	    boolean offer(E e)
取队首元素并删除	    E remove()	    E poll()
取队首元素但不删除	E element()	    E peek()
*/
//不要把null添加到队列里，不然取到了null元素还是不知道队列是不是空的
public class QueueStudy {
    public static void test(){
        Queue<String> q = new LinkedList<>();//LinkedList实现了deque接口.deque是queue子类

        //优先队列PriorityQueue,可以实现插队业务,出队顺序和元素的优先级相关
        //必须实现Comparable接口,根据元素的排序顺序决定出队优先级
        Queue<User> pq = new PriorityQueue<>(new UserComparator());
        pq.offer(new User("Alice", "A2"));
        pq.offer(new User("Bob", "A1"));
        pq.offer(new User("Tom", "A3"));
        pq.offer(new User("Sam", "V1"));
        System.out.println(pq.poll());
        System.out.println(pq.poll());
        System.out.println(pq.poll());
        System.out.println(pq.poll());

        //Deque就是能添加和删除在两端的队列
        //持有接口说明代码的抽象层次更高，而且接口本身定义的方法代表了特定的用途
        // 不推荐的写法:
        LinkedList<String> d1 = new LinkedList<>();
        d1.offerLast("z");
        // 推荐的写法：
        Deque<String> d2 = new LinkedList<>();
        d2.offerLast("z");
        //注意java里没有Stack，但是Deque可以模拟Stack
        //当我们把Deque作为Stack使用时，注意只调用push()/pop()/peek()方法，不要调用addFirst()/removeFirst()/peekFirst()方法，这样代码更加清晰。

        //Stack练习1：字符串中缀表达式转后缀表达式，然后再利用栈执行后缀表达式获得计算结果
        String exp = "1 + 2 * (18 - 5)";
        SuffixExpression se = compile(exp);
        System.out.println(se.toString());
        int result = se.execute();
        System.out.println(exp + " = " + result + " " + (result == 1 + 2 * (18 - 5) ? "✓" : "✗"));
    }
    static SuffixExpression compile(String exp) {
        SuffixExpression sf = new SuffixExpression(exp);
        return sf;
    }
}
class UserComparator implements Comparator<User>{
    @Override
    public int compare(User o1, User o2) {
        if(o1.number.charAt(0) == o2.number.charAt(0)){
            return o1.number.compareTo(o2.number);
        }
        //如果有V就是最高级别
        if(o1.number.charAt(0) == 'V'){
            return -1;
        }else{
           return 1;
        }
    }
}
class User{
    public final String name;
    public final String number;

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String toString() {
        return name + "/" + number;
    }

}
class SuffixExpression {
    private Deque<Character> stk = new LinkedList<>();
    private Deque<Integer> itk = new LinkedList<>();
    private StringBuilder sb = new StringBuilder();

    //中缀转后缀
    public SuffixExpression(String exp) {
        for(int i = 0; i < exp.length(); i++){
            char ch = exp.charAt(i);
            if(Character.isDigit(ch)){
                //多位数字处理
                while(i < exp.length() && Character.isDigit(exp.charAt(i))){
                    sb.append(exp.charAt(i ++));
                }
                sb.append(' ');
                i --;
            }else if(ch == '('){
                stk.push(ch);
            }else if(ch == ')'){
                while(!stk.isEmpty() && stk.peek() != '('){
                    sb.append(stk.pop());
                    sb.append(' ');
                }
                stk.pop();//弹出左括号
            }else if(isOperator(ch)){
                //获取运算符的优先级
                int currentPrecedence = getPrecedence(ch);
                //优先级更高的运算符出栈
                while(!stk.isEmpty() && getPrecedence(stk.peek()) >= currentPrecedence){
                    sb.append(stk.pop());
                    sb.append(' ');
                }
                stk.push(ch);
            }
        }
        //弹出剩余操作符
        while(!stk.isEmpty()){
            sb.append(stk.pop());
            sb.append(' ');
        }
    }
    private int getPrecedence(char op){
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }
    //后缀表达式求值
    public int execute() {
        String s = sb.toString();
        //为什么要放进一个list里？因为有的数字可能是好几位，所以不能用char
        List<String> tokens = Arrays.asList(s.split(" "));
        for (String t : tokens) {
            if(t.length() == 1 && isOperator(t.charAt(0))){
                int b = itk.pop();
                int a = itk.pop();
                switch (t.charAt(0)){
                    //java12 新特性，switch case 不需要break
                    case '+' -> itk.push(a + b);
                    case '-' -> itk.push(a - b);
                    case '*' -> itk.push(a * b);
                    case '/' -> itk.push(a / b);
                }
            }else{
                itk.push(Integer.parseInt(t));
            }
        }

        return itk.pop();
    }
    private boolean isOperator(char ch){
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }
    public String toString() {
        return sb.toString();
    }

}