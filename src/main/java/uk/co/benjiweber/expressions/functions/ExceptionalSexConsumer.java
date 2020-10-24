package uk.co.benjiweber.expressions.functions;

public interface ExceptionalSexConsumer<A, B, C, D, E, F, EX extends Exception> {
    void accept(A a, B b, C c, D d, E e, F f) throws EX;
}
