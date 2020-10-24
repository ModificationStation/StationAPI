package uk.co.benjiweber.expressions.functions;

public interface ExceptionalQuinConsumer<A, B, C, D, E, EX extends Exception> {
    void accept(A a, B b, C c, D d, E e) throws EX;
}
