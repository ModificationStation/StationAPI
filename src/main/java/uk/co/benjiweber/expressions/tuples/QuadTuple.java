package uk.co.benjiweber.expressions.tuples;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.functions.ExceptionalQuadConsumer;
import uk.co.benjiweber.expressions.functions.ExceptionalQuadFunction;

public interface QuadTuple<A,B,C,D> {
    A one();
    B two();
    C three();
    D four();
    static <A,B,C,D> QuadTuple<A,B,C,D> of(A a, B b, C c, D d) {
        abstract class QuadTupleValue extends Value<QuadTuple<A,B,C,D>> implements QuadTuple<A,B,C,D> {}
        return new QuadTupleValue() {
            public A one() { return a; }
            public B two() { return b; }
            public C three() { return c; }
            public D four() { return d; }
        }.using(QuadTuple::one, QuadTuple::two, QuadTuple::three, QuadTuple::four);
    }


    default <R, E extends Exception> R map(ExceptionalQuadFunction<A, B, C, D, R, E> f) throws E {
        return f.apply(one(), two(), three(), four());
    }

    default <E extends Exception> void consume(ExceptionalQuadConsumer<A, B, C, D, E> consumer) throws E {
        consumer.accept(one(), two(), three(), four());
    }
}
