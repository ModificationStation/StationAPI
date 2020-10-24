package uk.co.benjiweber.expressions.tuples;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.functions.ExceptionalQuinConsumer;
import uk.co.benjiweber.expressions.functions.ExceptionalQuinFunction;

public interface QuinTuple<A,B,C,D,E> {
    A one();
    B two();
    C three();
    D four();
    E five();
    static <A,B,C,D,E> QuinTuple<A,B,C,D,E> of(A a, B b, C c, D d, E e) {
        abstract class QuinTupleValue extends Value<QuinTuple<A,B,C,D,E>> implements QuinTuple<A,B,C,D,E> {}
        return new QuinTupleValue() {
            public A one() { return a; }
            public B two() { return b; }
            public C three() { return c; }
            public D four() { return d; }
            public E five() { return e; }
        }.using(QuinTuple::one, QuinTuple::two, QuinTuple::three, QuinTuple::four, QuinTuple::five);
    }


    default <R,EX extends Exception> R map(ExceptionalQuinFunction<A, B, C, D, E, R, EX> f) throws EX {
        return f.apply(one(), two(), three(), four(), five());
    }

    default <EX extends Exception> void consume(ExceptionalQuinConsumer<A, B, C, D, E, EX> consumer) throws EX {
        consumer.accept(one(), two(), three(), four(), five());
    }
}
