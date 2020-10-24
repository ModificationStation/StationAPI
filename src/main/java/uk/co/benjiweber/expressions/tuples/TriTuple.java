package uk.co.benjiweber.expressions.tuples;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.functions.ExceptionalTriConsumer;
import uk.co.benjiweber.expressions.functions.ExceptionalTriFunction;

public interface TriTuple<A,B,C> {
    A one();
    B two();
    C three();
    static <A,B,C> TriTuple<A,B,C> of(A a, B b, C c) {
        abstract class TriTupleValue extends Value<TriTuple<A,B,C>> implements TriTuple<A,B,C> {}
        return new TriTupleValue() {
            public A one() { return a; }
            public B two() { return b; }
            public C three() { return c; }
        }.using(TriTuple::one, TriTuple::two, TriTuple::three);
    }

    default <R,E extends Exception> R map(ExceptionalTriFunction<A, B, C, R, E> f) throws E {
        return f.apply(one(),two(),three());

    }

    default <E extends Exception> void consume(ExceptionalTriConsumer<A,B,C,E> consumer) throws E {
        consumer.accept(one(), two(), three());
    }
}
