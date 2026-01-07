package org.regex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexStudy {

    @Test
    public void test(){
        String re = "learn\\s(java|php|go)";//注意需要对\转义 \s代表空格
        System.out.println("learn java".matches(re));
        System.out.println("learn Java".matches(re));
        System.out.println("learn php".matches(re));
        System.out.println("learn Go".matches(re));
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-1234567", "0-345617", "134-123sd678"})
    public void test2(String s){
        //匹配区号-电话号，匹配成功后把区号和电话号分别保存
        Pattern p = Pattern.compile("(\\d{3,4})-(\\d{7,8})");//为什么要加括号，这样方便提取
        Matcher m = p.matcher(s);
        if(m.matches()){//匹配成功
            //可以直接从group返回子串
            String g1 = m.group(1);
            String g2 = m.group(2);
            System.out.println(g1 + " " + g2);
        }else{
            System.out.println("匹配失败");
        }
    }

    @Test
    public void test3(){
        Pattern pattern = Pattern.compile("(\\d+?)(0*)");//使用非贪婪匹配,就是在后面加个？，让其尽量少的匹配，然后让0*尽量多匹配
        Matcher matcher = pattern.matcher("1230000");
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1)); // "123"
            System.out.println("group2=" + matcher.group(2)); // "0000"
        }
    }

    //分割字符串
    @Test
    public void test4(){
        List<String> a = List.of("a b c".split("\\s")); // { "a", "b", "c" }
        List<String> b = List.of("a b  c".split("\\s")); // { "a", "b", "", "c" } 这个少了个+，应该是至少一个空格才能全部删掉
        List<String> c = List.of("a, 2b ;; c".split("[\\,\\;\\s]+")); // { "a", "b", "c" }
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
    }

    //查找字符串
    @Test
    public void test5(){
        String s = "the quick brown fox jumps over the lazy dog.";
        Pattern p = Pattern.compile("\\wo\\w");
        Matcher m = p.matcher(s);
        //不是使用matches而是find,在字符串里搜索能匹配上这个规则的子串
        while (m.find()) {
            String sub = s.substring(m.start(), m.end());
            System.out.println(sub);
        }
    }

    //替换字符串
    @Test
    public void test6(){
        String s = "The     quick\t\t brown   fox  jumps   over the  lazy dog.";
        //可以将多余的空格全部删掉
        String r = s.replaceAll("\\s+", " ");
        System.out.println(r);
    }

    //反向引用
    @Test
    public void test7(){
        String s = "the quick brown fox jumps over the lazy dog.";
        String r = s.replaceAll("\\s([a-z]{4})\\s", "<b>$1</b>");//使用$1来反向引用到匹配的子串
        System.out.println(r);
    }
}
