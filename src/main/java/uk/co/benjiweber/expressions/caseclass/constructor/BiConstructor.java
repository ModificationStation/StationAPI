package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;

import java.util.function.BiFunction;

public class BiConstructor<A,B,T> {
    private BiFunction<A, B, T> constructor;

    public BiConstructor(BiFunction<A, B, T> ctor) {
        this.constructor = ctor;
    }

    public static <A,B,T> BiConstructor<A,B,T> a(BiFunction<A, B, T> ctor) {
        return new BiConstructor<>(ctor);
    }
    public static <A,B,T> BiConstructor<A,B,T> an(BiFunction<A, B, T> ctor) {
        return new BiConstructor<>(ctor);
    }

    public NoMatch<T> matching(A arg0, B arg1) {
        return () -> constructor.apply(arg0, arg1);
    }

    public UniMatch<T,B> matching(A arg0, MatchesAny _) {
        return () -> constructor.apply(arg0, null);
    }
    public UniMatch<T,A> matching(MatchesAny _, B arg1) {
        return () -> constructor.apply(null, arg1);
    }
    public <M0> UniMatch<T,M0> matching(A arg0, UniMatch<B, M0> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public <M0> UniMatch<T,M0> matching(UniMatch<A, M0> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }

    public BiMatch<T,A,B> matching(MatchesAny arg0, MatchesAny arg1) {
        return () -> constructor.apply(null, null);
    }
    public <M0,M1> BiMatch<T,M0,M1> matching(A arg0, BiMatch<B, M0, M1> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public <M0,M1> BiMatch<T,M0,M1> matching(BiMatch<A, M0, M1> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }
    public <M0,M1> BiMatch<T,M0,M1> matching(UniMatch<A, M0> arg0, UniMatch<B, M1> arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1.comparee());
    }
    public <M0> BiMatch<T,M0,B> matching(UniMatch<A, M0> arg0, MatchesAny arg1) {
        return () -> constructor.apply(arg0.comparee(), null);
    }
    public <M0> BiMatch<T,A,M0> matching(MatchesAny arg0, UniMatch<B, M0> arg1) {
        return () -> constructor.apply(null, arg1.comparee());
    }

    public <M0,M1,M2> TriMatch<T,M0,M1,M2> matching(A arg0, TriMatch<B, M0, M1, M2> arg1) {
        return () -> constructor.apply(arg0, arg1.comparee());
    }
    public <M0,M1,M2>TriMatch<T,M0,M1,M2> matching(TriMatch<A, M0, M1, M2> arg0, B arg1) {
        return () -> constructor.apply(arg0.comparee(), arg1);
    }

    public <M1,M2> TriMatch<T,A,M1,M2> matching(MatchesAny arg0, BiMatch<B, M1, M2> arg1) {
        return () -> constructor.apply(null, arg1.comparee());
    }

    public <M1,M2> TriMatch<T,M1,M2,B> matching(BiMatch<A, M1, M2> arg0, MatchesAny arg1) {
        return () -> constructor.apply(arg0.comparee(), null);
    }
}
