package org.example;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)   {
        System.out.println("Hello world!");
//        java 13 开始"""..."""表示多行字符串
        String s = """
                   SELECT * FROM
                    users
                   WHERE id > 100
                   ORDER BY name DESC""";
        System.out.println(s);
        Scanner scanner  = new Scanner(System.in);
        String name = scanner.nextLine();
        int age = scanner.nextInt();
        System.out.printf("name: %s, age: %d\n", name, age);
//       java 12 开始， switch升级更简洁的表达式
//        不要写break。因为新语法只会执行匹配语句，没有穿透效应
        scanner.nextLine();
        String fruit = scanner.nextLine();
        switch (fruit){
            case "apple" -> System.out.println("apple");
            case "pear" -> System.out.println("pear");
            case "mango" -> {
                System.out.println("mango");
                System.out.println("good");
            }
            default -> System.out.println("no select" + fruit);
        }
//        直接返回值
        int opt = switch (fruit){
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> 0;
        };
        System.out.println("opt = " + opt);
//        java 14 yield,如果需要复杂的语句，可以使用yield返回一个值作为switch的返回值
        int opt2 = switch (fruit){
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> {
               yield fruit.hashCode();
            }
        };
        System.out.println("opt2 = " + opt2);
        Integer[] arr = {12, 23, 41, 2, 13};
        Arrays.sort(arr, (a1, a2) -> a2 - a1);
//        二维数组打印
        int[][] ns = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println(Arrays.deepToString(ns));

    }
}