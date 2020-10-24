package uk.co.benjiweber.expressions.caseclass.constructor;

import uk.co.benjiweber.expressions.caseclass.MatchesAny;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.NoMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;

import java.util.function.Function;

public class UniConstructor<A,T> {
    private Function<A, T> constructor;

    public UniConstructor(Function<A, T> ctor) {
        this.constructor = ctor;
    }

    public static <A,T> UniConstructor<A,T> a(Function<A, T> ctor) {
        return new UniConstructor<>(ctor);
    }
    public static <A,T> UniConstructor<A,T> an(Function<A, T> ctor) {
        return new UniConstructor<>(ctor);
    }


    public NoMatch<T> $(A arg0) {
        return () -> constructor.apply(arg0);
    }
    public UniMatch<T,A> $(MatchesAny _) {
        return () -> constructor.apply(null);
    }
    public <M0> UniMatch<T,A> $(UniMatch<A, M0> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
    public <M0,M1> BiMatch<T,M0,M1> $(BiMatch<A, M0, M1> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
    public <M0,M1,M2> TriMatch<T,M0,M1,M2> $(TriMatch<A, M0, M1, M2> arg0) {
        return () -> constructor.apply(arg0.comparee());
    }
}
