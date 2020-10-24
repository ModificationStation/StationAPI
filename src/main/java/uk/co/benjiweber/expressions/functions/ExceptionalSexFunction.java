package uk.co.benjiweber.expressions.functions;

public interface ExceptionalSexFunction<A, B, C, D, E, F, R, EX extends Exception> {
    R apply(A a, B b, C c, D d, E e, F f) throws EX;
}
