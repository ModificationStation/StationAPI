package uk.co.benjiweber.expressions;

public class Print {
    public static <T> T println(T t) {
        System.out.println(t);
        return t;
    }
}
