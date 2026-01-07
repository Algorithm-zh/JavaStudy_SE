package org.test;

public class Calculator {
    private long n = 0;

    public long add(long x) {
        n = n + x;
        return n;
    }

    public long sub(long x) {
        n = n - x;
        return n;
    }

    public long div(long x) {
        if(x < 0){
            throw new ArithmeticException("x must be positive");
        }
        n = n / x;
        return n;
    }
}
