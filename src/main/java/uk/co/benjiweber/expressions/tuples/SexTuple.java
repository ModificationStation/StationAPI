package uk.co.benjiweber.expressions.tuples;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.functions.ExceptionalSexConsumer;
import uk.co.benjiweber.expressions.functions.ExceptionalSexFunction;

public interface SexTuple<A,B,C,D,E,F> {
    A one();
    B two();
    C three();
    D four();
    E five();
    F six();
    static <A,B,C,D,E,F> SexTuple<A,B,C,D,E,F> of(A a, B b, C c, D d, E e, F f) {
        abstract class SexTupleValue extends Value<SexTuple<A,B,C,D,E,F>> implements SexTuple<A,B,C,D,E,F> {}
        return new SexTupleValue() {
            public A one() { return a; }
            public B two() { return b; }
            public C three() { return c; }
            public D four() { return d; }
            public E five() { return e; }
            public F six() { return f; }
        }.using(SexTuple::one, SexTuple::two, SexTuple::three, SexTuple::four, SexTuple::five, SexTuple::six);
    }


    default <R,EX extends Exception> R map(ExceptionalSexFunction<A, B, C, D, E, F, R, EX> f) throws EX {
        return f.apply(one(), two(), three(), four(), five(), six());
    }

    default <EX extends Exception> void consume(ExceptionalSexConsumer<A, B, C, D, E, F, EX> consumer) throws EX {
        consumer.accept(one(), two(), three(), four(), five(), six());
    }
}
