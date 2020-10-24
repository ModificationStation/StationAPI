package uk.co.benjiweber.expressions.tuples;

public interface Tuple {
    static <A> UniTuple<A> tuple(A a) {
        return UniTuple.of(a);
    }

    static <A,B> BiTuple<A,B> tuple(A a, B b) {
        return BiTuple.of(a, b);
    }

    static <A,B,C> TriTuple<A,B,C> tuple(A a, B b, C c) {
        return TriTuple.of(a, b, c);
    }

    static <A,B,C,D> QuadTuple<A,B,C,D> tuple(A a, B b, C c, D d) {
        return QuadTuple.of(a, b, c, d);
    }

    static <A,B,C,D,E> QuinTuple<A,B,C,D,E> tuple(A a, B b, C c, D d, E e) {
        return QuinTuple.of(a, b, c, d, e);
    }

    static <A,B,C,D,E,F> SexTuple<A,B,C,D,E,F> tuple(A a, B b, C c, D d, E e, F f) {
        return SexTuple.of(a, b, c, d, e, f);
    }
}
