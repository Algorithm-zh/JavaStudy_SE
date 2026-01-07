package org.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {
    private StringUtils stringUtils;

    @BeforeEach
    public void setUp(){
        stringUtils = new StringUtils();
    }

    @AfterEach
    public void clean(){
        stringUtils = null;
    }

    //测试capitalize方法，它会把字符串的第一个字母大写，后面的小写，我们不仅要给出输入，还要给出预期输出
    @ParameterizedTest
    @MethodSource
    public void testCapitalize(String input, String expected){
        assertEquals(expected, stringUtils.capitalize(input));
    }

    //我们需要写一个静态的同名方法来提供测试参数
    static List<Arguments> testCapitalize(){
        return List.of(
                Arguments.of("hello", "Hello"),
                Arguments.of("hello world", "Hello World"),
                Arguments.of("hello world!", "Hello World!")
        );
    }

    //另一种传入测试参数是CsvSource,它的每一个字符串表示一行，一行包含的若干参数用,分隔
    @ParameterizedTest
    @CsvSource({"hello, Hello", "hello world, Hello World", "hello world!, Hello World!"})
    public void testCapitalize2(String input, String expected){
        assertEquals(expected, stringUtils.capitalize(input));
    }

    //在测试数据非常多的时候我还可以将测试数据放到一个独立的CSV文件里，这时需要使用@CsvFileSourcec
    @ParameterizedTest
    @CsvFileSource(resources = {"/test.csv"})//甚至可以写好几个文件，resources = {"/test.csv", "/test2.csv"}
    public void testCapitalize3(String input, String expected){
        assertEquals(expected, stringUtils.capitalize(input));
    }
}
