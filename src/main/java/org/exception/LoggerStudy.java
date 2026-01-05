package org.exception;

//Commons Logging是一个第三方日志库，它是由Apache创建的日志模块。
//它可以挂接不同的日志系统，并通过配置文件指定挂接的日志系统。默认情况下，Commons Loggin自动搜索并使用Log4j（Log4j是另一个流行的日志系统），如果没有找到Log4j，再使用JDK Logging。
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerStudy {
    //定义一个静态变量，可以直接使用在静态方法里
    static final Log log = LogFactory.getLog(LoggerStudy.class);
    static void foo(){
        log.info("foo");
        Integer.parseInt(null);
    }
    //在实例方法里引用Log，通常是定义一个实例变量
    protected final Log log2 = LogFactory.getLog(getClass());//这个返回的是运行时的实例，也就是说子类继承使用的话他返回的就是子类的名字,相当于LogFactory.getLog(LoggerChild.class)

    public void test(){
        //commons logging测试
        log2.info("hello world");
        log2.error("error");
    }

    //slf4j代替Common logback代替log4j，这个组合更加的灵活高效.slf4j的接口更加灵活，logback的速度更加高效
    static final Logger logg = LoggerFactory.getLogger(LoggerStudy.class);
    public void test2(){
        logg.info("hello {} I am {}, this is a error", "world", "zhangsan");//SLF4J日志接口的改进,方便了日志的拼接
    }

}
class LoggerChild extends LoggerStudy{
    public void testChild(){
        try{
            foo();
        }catch (Exception e){
            log2.error("error", e);//使用日志打印异常
        }
    }
}
