package org.exception;

//自定义异常
//先设定一个基类异常
//后面各种业务可以派生各种异常
public class BaseException extends RuntimeException{
    public BaseException(){
        super();
    }
    public BaseException(String message, Throwable cause){
        super(message, cause);
    }
    public BaseException(String message){
        super(message);
    }
    public BaseException(Throwable cause){
        super(cause);
    }
}
