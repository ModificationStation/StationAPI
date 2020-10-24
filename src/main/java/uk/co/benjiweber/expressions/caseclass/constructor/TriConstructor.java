package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;
import uk.co.benjiweber.expressions.functions.TriFunction;

public class TriConstructor<A,B,C,T> {
    private TriFunction<A, B, C, T> constructor;

    public TriConstructor(TriFunction<A, B, C, T> ctor) {
        this.constructor = ctor;
    }

    public static <A,B,C,T> TriConstructor<A,B,C,T> a(TriFunction<A, B, C, T> ctor) {
        return new TriConstructor<>(ctor);
    }
    public static <A,B,C,T> TriConstructor<A,B,C,T> an(TriFunction<A, B, C, T> ctor) {
        return new TriConstructor<>(ctor);
    }


    public NoMatch<T> matching(A arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1, arg2);
    }
    public <D> UniMatch<T,D> matching(A arg0, B arg1, UniMatch<C,D> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }
    public <D> UniMatch<T,D> matching(A arg0, UniMatch<B,D> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }
    public <D> UniMatch<T,D> matching(UniMatch<A,D> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public UniMatch<T,A> matching(A arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, arg1, null);
    }
    public UniMatch<T,B> matching(A arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(arg0, null, arg2);
    }
    public UniMatch<T,C> matching(MatchesAny arg0, B arg1, C arg2) {
        return () -> constructor.apply(null, arg1, arg2);
    }


    public <D,E> BiMatch<T,D,E> matching(BiMatch<A,D,E> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public <D,E> BiMatch<T,D,E> matching(A arg0, BiMatch<B,D,E> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }

    public <D,E> BiMatch<T,D,E> matching(A arg0, B arg1, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }

    public BiMatch<T,B,C> matching(A arg0, MatchesAny arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, null, null);
    }
    public BiMatch<T,A,C> matching(MatchesAny arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(null, arg1, null);
    }
    public BiMatch<T,A,B> matching(MatchesAny arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(null, null, arg2);
    }

    public <M0> BiMatch<T,A,M0> matching(MatchesAny arg0, B arg1, UniMatch<C,M0> arg2) {
        return () -> constructor.apply(null, arg1, arg2.comparee());
    }
    public <M0> BiMatch<T,B,M0> matching(A arg0, MatchesAny arg1, UniMatch<C,M0> arg2) {
        return () -> constructor.apply(arg0, null, arg2.comparee());
    }
    public <M0> BiMatch<T,M0,C> matching(MatchesAny arg0, UniMatch<B,M0> arg1, C arg2) {
        return () -> constructor.apply(null, arg1.comparee(), arg2);
    }
    public <M0> BiMatch<T,M0,C> matching(A arg0, UniMatch<B,M0> arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), null);
    }
    public <M0> BiMatch<T,M0,B> matching(UniMatch<A,M0> arg0, MatchesAny arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), null, arg2);
    }
    public <M0> BiMatch<T,M0,C> matching(UniMatch<A,M0> arg0, B arg1, MatchesAny arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, null);
    }

    public <D,E> TriMatch<T,D,E,B> matching(MatchesAny _,  BiMatch<B,D,E> arg1, C arg2) {
        return () -> constructor.apply(null, arg1.comparee(), arg2);
    }

    public <D,E> TriMatch<T,D,E,B> matching(MatchesAny _, B arg1, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(null, arg1, arg2.comparee());
    }

    public <D,E> TriMatch<T,D,E,B> matching(BiMatch<A,D,E> arg0, MatchesAny _, C arg2) {
        return () -> constructor.apply(arg0.comparee(), null, arg2);
    }

    public <D,E> TriMatch<T,D,E,B> matching(A arg0, MatchesAny _, BiMatch<C,D,E> arg2) {
        return () -> constructor.apply(arg0, null, arg2.comparee());
    }

    public <D,E> TriMatch<T,D,E,B> matching(A arg0, BiMatch<B,D,E> arg1, MatchesAny _) {
        return () -> constructor.apply(arg0, arg1.comparee(), null);
    }

    public <D,E> TriMatch<T,D,E,B> matching(BiMatch<A,D,E> arg0, B arg1, MatchesAny _) {
        return () -> constructor.apply(arg0.comparee(), arg1, null);
    }

    public <D,E,F> TriMatch<T,D,E,F> matching(TriMatch<A,D,E,F> arg0, B arg1, C arg2) {
        return () -> constructor.apply(arg0.comparee(), arg1, arg2);
    }

    public <D,E,F> TriMatch<T,D,E,F> matching(A arg0, TriMatch<B,D,E,F> arg1, C arg2) {
        return () -> constructor.apply(arg0, arg1.comparee(), arg2);
    }

    public <D,E,F> TriMatch<T,D,E,F> matching(A arg0, B arg1, TriMatch<C, D, E, F> arg2) {
        return () -> constructor.apply(arg0, arg1, arg2.comparee());
    }
}
