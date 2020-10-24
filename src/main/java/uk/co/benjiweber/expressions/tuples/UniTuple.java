package uk.co.benjiweber.expressions.tuples;

import uk.co.benjiweber.expressions.Value;
import uk.co.benjiweber.expressions.functions.ExceptionalConsumer;
import uk.co.benjiweber.expressions.functions.ExceptionalFunction;

import java.util.function.Function;

public interface UniTuple<A> {
    A one();
    static <A> UniTuple<A> of(A a) {
        abstract class UniTupleValue extends Value<UniTuple<A>> implements UniTuple<A> {}
        return new UniTupleValue() {
            public A one() { return a; }
        }.using(UniTuple::one);
    }

    default <R,E extends Exception> R map(ExceptionalFunction<A, R, E> f) throws E {
        return f.apply(one());
    }

    default <A1> UniTuple<A1> flatmap(Function<A, A1> f) {
        return of(f.apply(one()));
    }

    default <E extends Exception> void consume(ExceptionalConsumer<A,E> consumer) throws E{
        consumer.accept(one());
    }
}
