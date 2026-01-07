package org.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.ValueSources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    Calculator calculator;
    private int x;
    //下面这些注解的函数最终会变成
    /*
    invokeBeforeAll(CalculatorTest.class);
    for (Method testMethod : findTestMethods(CalculatorTest.class)) {
        var test = new CalculatorTest(); // 创建Test实例
        invokeBeforeEach(test);
        invokeTestMethod(test, testMethod);
        invokeAfterEach(test);
    }
    invokeAfterAll(CalculatorTest.class);
     */

    //有这个注解的函数会在每次执行test之前执行
    @BeforeEach
    public void setUp(){
        this.calculator = new Calculator();
    }

    //有这个注解的函数会在每次执行test之后执行
    @AfterEach
    public void clean(){
        this.calculator = null;
    }

    //有这个注解的函数会在所有test之前执行
    @BeforeAll
    public static void init(){
        System.out.println("init");
    }

    @AfterAll
    public static void destroy(){
        System.out.println("destroy");
    }

    @Test
    void testAdd(){
        assertEquals(-100, calculator.add(-100));
        assertEquals(-150, calculator.add(50));
        assertEquals(-130, calculator.add(-20));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS) //条件测试，只有在Windows系统下执行
    void testSub(){
        assertEquals(100, calculator.sub(100));
        assertEquals(150, calculator.sub(-50));
        assertEquals(130, calculator.sub(20));
    }

    //参数化测试
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void testData(int x){
        calculator.add(x);
    }

    //针对异常进行测试
    @Test
    void testDiv(){
//        assertThrows()在捕获到指定异常时表示通过测试，未捕获到异常，或者捕获到的异常类型不对，均表示测试失败
        assertThrows(ArithmeticException.class, () -> {calculator.div(0);});
//        calculator.div(0);
    }
}
