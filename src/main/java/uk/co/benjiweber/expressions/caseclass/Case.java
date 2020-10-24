package uk.co.benjiweber.expressions.caseclass;

import uk.co.benjiweber.expressions.EqualsHashcode;
import uk.co.benjiweber.expressions.caseclass.constructor.references.BiMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.TriMatch;
import uk.co.benjiweber.expressions.caseclass.constructor.references.UniMatch;
import uk.co.benjiweber.expressions.functions.TriFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public interface Case<T> extends EqualsHashcode<T> {
    default MatchBuilder<T> match() {
        return new MatchBuilder<T>() {
            public <R> MatchBuilderR<T, R> when(T value, Function<T, R> f) {
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value, f)), Case.this);
            }
            public <R, A, B> MatchBuilderR<T, R> when(TwoMissing<T, A, B> value, BiFunction<A, B, R> f) {
                Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)Case.this), value.prop2((T)Case.this));
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value.original(), valueExtractor)), Case.this);
            }

            public ZeroMatchConstructorBuilder<T> when(Supplier<T> constructor) {
                return new ZeroMatchConstructorBuilder<T>() {
                    public <R> MatchBuilderR<T, R> then(Function<T, R> f) {
                        T original = constructor.get();
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original, f)), Case.this);
                    }
                };
            }

            public <A> ZeroMatchConstructorBuilder<T> when(Function<A,T> constructor, A a) {
                return new ZeroMatchConstructorBuilder<T>() {
                    public <R> MatchBuilderR<T, R> then(Function<T, R> f) {
                        T original = constructor.apply(a);
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original, f)), Case.this);
                    }
                };
            }

            public <A> UniMatchConstructorBuilder<T, A> when(Function<A, T> constructor, MatchesAny a) {
                return new UniMatchConstructorBuilder<T, A>() {
                    public <R> MatchBuilderR<T, R> then(Function<A, R> f) {
                        T original = constructor.apply(null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }


            @Override
            public <A> UniMatchConstructorBuilder<T, A> when(UniMatch<T, A> ref) {
                return new UniMatchConstructorBuilder<T, A>() {
                    public <R> MatchBuilderR<T, R> then(Function<A, R> f) {
                        T original = ref.comparee();
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            @Override
            public <A,B> BiMatchConstructorBuilder<T, A, B> when(BiMatch<T, A, B> ref) {
                return new BiMatchConstructorBuilder<T, A, B>() {
                    public <R> MatchBuilderR<T, R> then(BiFunction<A, B, R> f) {
                        T original = ref.comparee();
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            @Override
            public <A, B, C> TriMatchConstructorBuilder<T, A, B, C> when(TriMatch<T, A, B, C> ref) {
                return new TriMatchConstructorBuilder<T, A, B, C>() {
                    public <R> MatchBuilderR<T, R> then(TriFunction<A, B, C, R> f) {
                        T original = ref.comparee();
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1), (C)missingProps.get(2));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };

            }


            public <A,B> ZeroMatchConstructorBuilder<T> when(BiFunction<A,B,T> constructor, A a, B b) {
                return new ZeroMatchConstructorBuilder<T>() {
                    public <R> MatchBuilderR<T, R> then(Function<T, R> f) {
                        T original = constructor.apply(a,b);
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original, f)), Case.this);
                    }
                };
            }

            public <A, B> BiMatchConstructorBuilder<T, A, B> when(BiFunction<A, B, T> constructor, MatchesAny a, MatchesAny b) {
                return new BiMatchConstructorBuilder<T, A, B>() {
                    public <R> MatchBuilderR<T, R> then(BiFunction<A, B, R> f) {
                        T original = constructor.apply(null,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            public <A, B> UniMatchConstructorBuilder<T, A> when(BiFunction<A, B, T> constructor, MatchesAny a, B b) {
                return new UniMatchConstructorBuilder<T, A>() {
                    public <R> MatchBuilderR<T, R> then(Function<A, R> f) {
                        T original = constructor.apply(null,b);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            public <A, B> UniMatchConstructorBuilder<T, B> when(BiFunction<A, B, T> constructor, A a, MatchesAny b) {
                return new UniMatchConstructorBuilder<T, B>() {
                    public <R> MatchBuilderR<T, R> then(Function<B, R> f) {
                        T original = constructor.apply(a,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }




            public <A,B,C> ZeroMatchConstructorBuilder<T> when(TriFunction<A,B,C,T> constructor, A a, B b, C c) {
                return new ZeroMatchConstructorBuilder<T>() {
                    public <R> MatchBuilderR<T, R> then(Function<T, R> f) {
                        T original = constructor.apply(a,b,c);
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original, f)), Case.this);
                    }
                };
            }

            public <A, B, C> TriMatchConstructorBuilder<T, A, B, C> when(TriFunction<A, B, C, T> constructor, MatchesAny a, MatchesAny b, MatchesAny c) {
                return new TriMatchConstructorBuilder<T, A, B, C>() {
                    public <R> MatchBuilderR<T, R> then(TriFunction<A, B, C, R> f) {
                        T original = constructor.apply(null,null,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1), (C)missingProps.get(2));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            public <A, B, C> BiMatchConstructorBuilder<T, A, B> when(TriFunction<A, B, C, T> constructor, MatchesAny a, MatchesAny b, C c) {
                return new BiMatchConstructorBuilder<T, A, B>() {
                    public <R> MatchBuilderR<T, R> then(BiFunction<A, B, R> f) {
                        T original = constructor.apply(null,null,c);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }


            public <A, B, C> BiMatchConstructorBuilder<T, B, C> when(TriFunction<A, B, C, T> constructor, A a, MatchesAny b, MatchesAny c) {
                return new BiMatchConstructorBuilder<T, B, C>() {
                    public <R> MatchBuilderR<T, R> then(BiFunction<B, C, R> f) {
                        T original = constructor.apply(a,null,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0), (C)missingProps.get(1));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            public <A, B, C> BiMatchConstructorBuilder<T, A, C> when(TriFunction<A, B, C, T> constructor, MatchesAny a, B b, MatchesAny c) {
                return new BiMatchConstructorBuilder<T, A, C>() {
                    public <R> MatchBuilderR<T, R> then(BiFunction<A, C, R> f) {
                        T original = constructor.apply(null,b,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (C)missingProps.get(1));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            @Override
            public <A, B, C> UniMatchConstructorBuilder<T, A> when(TriFunction<A, B, C, T> constructor, MatchesAny a, B b, C c) {
                return new UniMatchConstructorBuilder<T, A>() {
                    public <R> MatchBuilderR<T, R> then(Function<A, R> f) {
                        T original = constructor.apply(null,b,c);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            @Override
            public <A, B, C> UniMatchConstructorBuilder<T, B> when(TriFunction<A, B, C, T> constructor, A a, MatchesAny b, C c) {
                return new UniMatchConstructorBuilder<T, B>() {
                    public <R> MatchBuilderR<T, R> then(Function<B, R> f) {
                        T original = constructor.apply(a,null,c);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }


            @Override
            public <A, B, C> UniMatchConstructorBuilder<T, C> when(TriFunction<A, B, C, T> constructor, A a, B b, MatchesAny c) {
                return new UniMatchConstructorBuilder<T, C>() {
                    public <R> MatchBuilderR<T, R> then(Function<C, R> f) {
                        T original = constructor.apply(a,b,null);
                        List<Object> missingProps = missingProps((Case<T>)Case.this, (Case<T>)original);
                        Function<T,R> valueExtractor = t -> f.apply((C)missingProps.get(0));
                        return new MatchBuilderR<T, R>(asList(MatchDefinition.create(original,valueExtractor)),Case.this);
                    }
                };
            }

            public <R, A, B> MatchBuilderR<T, R> when(OneMissing<T, A> value, Function<A, R> f) {
                Function<T,R> valueExtractor = t -> f.apply(value.prop1((T) Case.this));
                return new MatchBuilderR<T, R>(asList(MatchDefinition.create(value.original(), valueExtractor)), Case.this);
            }
        };
    }

    static <T> List<Object> missingProps(Case<T> value, Case<T> toMatch) {
        Stream<Object> missingPropertiesFromNestedCaseProperties =
            toMatch.props().stream()
                .filter(prop -> prop.apply((T) toMatch) instanceof Case)
                .flatMap(prop -> missingProps((Case<Object>) prop.apply((T) value), (Case<Object>) prop.apply((T) toMatch)).stream());

        Stream<Object> missingDirectProperties = toMatch.props().stream()
                .filter(prop -> prop.apply((T) toMatch) == null)
                .map(prop -> prop.apply((T) value));

        return Stream.concat(missingPropertiesFromNestedCaseProperties,missingDirectProperties)
                .collect(Collectors.toList());
    }

    default <A> OneMissing<T,A> missing(Function<T,A> prop1) {
        return new OneMissing<T, A>() {
            public A prop1(T extractFrom) { return prop1.apply(extractFrom); }
            public T original() { return (T)Case.this; }
        };
    }

    default <A,B> TwoMissing<T,A,B> missing(Function<T,A> prop1, Function<T,B> prop2) {
        return new TwoMissing<T, A, B>() {
            public A prop1(T extractFrom) { return prop1.apply(extractFrom); }
            public B prop2(T extractFrom) { return prop2.apply(extractFrom); }
            public T original() { return (T)Case.this; }
        } ;
    }

    public interface TwoMissing<T,A,B> {
        A prop1(T extractFrom);
        B prop2(T extractFrom);
        T original();
    }
    public interface OneMissing<T,A> {
        A prop1(T extractFrom);
        T original();
    }

    public interface MatchBuilder<T> {
        <R> MatchBuilderR<T,R> when(T value, Function<T, R> f);
        <R,A,B> MatchBuilderR<T,R> when(TwoMissing<T,A,B> value, BiFunction<A,B,R> f);

        <A,B,C> BiMatchConstructorBuilder<T,A,B> when(TriFunction<A,B,C,T> constructor, MatchesAny a, MatchesAny b, C c);
        <A,B,C> BiMatchConstructorBuilder<T,A,C> when(TriFunction<A,B,C,T> constructor, MatchesAny a, B b, MatchesAny c);
        <A,B,C> BiMatchConstructorBuilder<T,B,C> when(TriFunction<A,B,C,T> constructor, A a, MatchesAny b, MatchesAny c);

        <A,B,C> UniMatchConstructorBuilder<T,A> when(TriFunction<A,B,C,T> constructor, MatchesAny a, B b, C c);
        <A,B,C> UniMatchConstructorBuilder<T,B> when(TriFunction<A,B,C,T> constructor, A a, MatchesAny b, C c);
        <A,B,C> UniMatchConstructorBuilder<T,C> when(TriFunction<A,B,C,T> constructor, A a, B b, MatchesAny c);

        <A,B,C> TriMatchConstructorBuilder<T,A,B,C> when(TriFunction<A,B,C,T> constructor, MatchesAny a, MatchesAny b, MatchesAny c);
        <A,B,C> ZeroMatchConstructorBuilder<T> when(TriFunction<A,B,C,T> constructor, A a, B b, C c);


        <A,B> ZeroMatchConstructorBuilder<T> when(BiFunction<A,B,T> constructor, A a, B b);
        <A,B> BiMatchConstructorBuilder<T,A,B> when(BiFunction<A,B,T> constructor, MatchesAny a, MatchesAny b);
        <A,B> UniMatchConstructorBuilder<T,A> when(BiFunction<A,B,T> constructor, MatchesAny a, B b);
        <A,B> UniMatchConstructorBuilder<T,B> when(BiFunction<A,B,T> constructor, A a, MatchesAny b);

        <A> ZeroMatchConstructorBuilder<T> when(Function<A,T> constructor, A a);
        <A> UniMatchConstructorBuilder<T,A> when(Function<A,T> constructor, MatchesAny a);

        ZeroMatchConstructorBuilder<T> when(Supplier<T> constructor);



        <M0> UniMatchConstructorBuilder<T,M0> when(UniMatch<T,M0> ref);
        <M0,M1> BiMatchConstructorBuilder<T,M0,M1> when(BiMatch<T,M0,M1> ref);
        <M0,M1,M2> TriMatchConstructorBuilder<T,M0,M1,M2> when(TriMatch<T,M0,M1,M2> ref);
    }

    public interface ZeroMatchConstructorBuilder<T> {
        <R> MatchBuilderR<T,R> then(Function<T,R> f);
    }

    public interface ZeroMatchConstructorBuilderR<T,R> {
        MatchBuilderR<T,R> then(Function<T,R> f);
    }


    public interface TriMatchConstructorBuilder<T,A,B,C> {
        <R> MatchBuilderR<T,R> then(TriFunction<A,B,C,R> f);
    }

    public interface TriMatchConstructorBuilderR<T,A,B,C,R> {
        MatchBuilderR<T,R> then(TriFunction<A,B,C,R> f);
    }

    public interface BiMatchConstructorBuilder<T,A,B> {
        <R> MatchBuilderR<T,R> then(BiFunction<A,B,R> f);
    }

    public interface BiMatchConstructorBuilderR<T,A,B,R> {
        MatchBuilderR<T,R> then(BiFunction<A,B,R> f);
    }

    public interface UniMatchConstructorBuilder<T,A> {
        <R> MatchBuilderR<T,R> then(Function<A,R> f);
    }

    public interface UniMatchConstructorBuilderR<T,A,R> {
        MatchBuilderR<T,R> then(Function<A,R> f);
    }

    interface MatchDefinition<T,R> {
        T value();
        Function<T,R> f();
        static <T,R> MatchDefinition<T,R> create(T value, Function<T,R> f) {
            return new MatchDefinition<T, R>() {
                public T value() { return value; }
                public Function<T, R> f() { return f; }
            };
        }

        static <T,R> Predicate<MatchDefinition<T,R>> matches(Case<T> value) {
            return match -> recursiveCompareIgnoringUnknownProperties(value, match.value());
        }

        static <T> boolean recursiveCompareIgnoringUnknownProperties(Case<T> value, T comparisonValue) {
            return value.props().stream()
                .allMatch(
                    prop -> {
                        Object lhs = prop.apply(comparisonValue);
                        if (lhs == null) return true;
                        if (lhs instanceof Case) return recursiveCompareIgnoringUnknownProperties((Case<Object>) prop.apply((T) value), lhs);
                        return (lhs.equals(prop.apply((T) value)));
                    }
                );
        }

    }

    public static class MatchBuilderR<T,R> {
        private List<MatchDefinition<T,R>> cases = new ArrayList<MatchDefinition<T, R>>();
        private Function<T, R> defaultCase;
        private Case<T> value;

        private MatchBuilderR(List<MatchDefinition<T,R>> cases, Case<T> value) {
            this.value = value;
            this.cases.addAll(cases);
        }

        public MatchBuilderR<T,R> when(T value, Function<T,R> f) {
            cases.add(MatchDefinition.create(value, f));
            return this;
        }

        public <A> MatchBuilderR<T,R> when(OneMissing<T,A> value, Function<A,R> f) {
            Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)this.value));
            cases.add(MatchDefinition.create(value.original(), valueExtractor));
            return this;
        }

        public <A,B> MatchBuilderR<T,R> when(TwoMissing<T,A,B> value, BiFunction<A,B,R> f) {
            Function<T,R> valueExtractor = t -> f.apply(value.prop1((T)this.value), value.prop2((T)this.value));
            cases.add(MatchDefinition.create(value.original(), valueExtractor));
            return this;
        }



        public ZeroMatchConstructorBuilderR<T,R> when(Supplier<T> constructor) {
            return new ZeroMatchConstructorBuilderR<T,R>() {
                public MatchBuilderR<T, R> then(Function<T, R> f) {
                    T original = constructor.get();
                    cases.add(MatchDefinition.create(original, f));
                    return MatchBuilderR.this;
                }
            };
        }





        public <A> ZeroMatchConstructorBuilderR<T,R> when(Function<A,T> constructor, A a) {
            return new ZeroMatchConstructorBuilderR<T,R>() {
                public MatchBuilderR<T, R> then(Function<T, R> f) {
                    T original = constructor.apply(a);
                    cases.add(MatchDefinition.create(original, f));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A> UniMatchConstructorBuilderR<T, A, R> when(Function<A, T> constructor, MatchesAny a) {
            return new UniMatchConstructorBuilderR<T, A, R>() {
                public MatchBuilderR<T, R> then(Function<A, R> f) {
                    T original = constructor.apply(null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                    cases.add(MatchDefinition.create(original, valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }





        public <A,B> ZeroMatchConstructorBuilderR<T,R> when(BiFunction<A,B,T> constructor, A a, B b) {
            return new ZeroMatchConstructorBuilderR<T,R>() {
                public MatchBuilderR<T, R> then(Function<T, R> f) {
                    T original = constructor.apply(a,b);
                    cases.add(MatchDefinition.create(original, f));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A, B> BiMatchConstructorBuilderR<T, A, B, R> when(BiFunction<A, B, T> constructor, MatchesAny a, MatchesAny b) {
            return new BiMatchConstructorBuilderR<T, A, B, R>() {
                public MatchBuilderR<T, R> then(BiFunction<A, B, R> f) {
                    T original = constructor.apply(null,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1));
                    cases.add(MatchDefinition.create(original, valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A, B> UniMatchConstructorBuilderR<T, A, R> when(BiFunction<A, B, T> constructor, MatchesAny a, B b) {
            return new UniMatchConstructorBuilderR<T, A, R>() {
                public MatchBuilderR<T, R> then(Function<A, R> f) {
                    T original = constructor.apply(null,b);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                    cases.add(MatchDefinition.create(original, valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A, B> UniMatchConstructorBuilderR<T, B, R> when(BiFunction<A, B, T> constructor, A a, MatchesAny b) {
            return new UniMatchConstructorBuilderR<T, B, R>() {
                public MatchBuilderR<T, R> then(Function<B, R> f) {
                    T original = constructor.apply(a,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0));
                    cases.add(MatchDefinition.create(original, valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }





        public <A,B,C> ZeroMatchConstructorBuilderR<T,R> when(TriFunction<A,B,C,T> constructor, A a, B b, C c) {
            return new ZeroMatchConstructorBuilderR<T,R>() {
                public MatchBuilderR<T, R> then(Function<T, R> f) {
                    T original = constructor.apply(a,b,c);
                    cases.add(MatchDefinition.create(original, f));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> TriMatchConstructorBuilderR<T, A, B, C, R> when(TriFunction<A,B,C,T> constructor, MatchesAny a, MatchesAny b, MatchesAny c) {
            return new TriMatchConstructorBuilderR<T, A, B, C, R>() {
                public MatchBuilderR<T, R> then(TriFunction<A, B, C, R> f) {
                    T original = constructor.apply(null,null,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1), (C)missingProps.get(2));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> BiMatchConstructorBuilderR<T, A, B, R> when(TriFunction<A,B,C,T> constructor, MatchesAny a, MatchesAny b, C c) {
            return new BiMatchConstructorBuilderR<T, A, B, R>() {
                public MatchBuilderR<T, R> then(BiFunction<A, B, R> f) {
                    T original = constructor.apply(null,null,c);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (B)missingProps.get(1));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> BiMatchConstructorBuilderR<T, A, C, R> when(TriFunction<A,B,C,T> constructor, MatchesAny a, B b, MatchesAny c) {
            return new BiMatchConstructorBuilderR<T, A, C, R>() {
                public MatchBuilderR<T, R> then(BiFunction<A, C, R> f) {
                    T original = constructor.apply(null,b,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0), (C)missingProps.get(1));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> BiMatchConstructorBuilderR<T, B, C, R> when(TriFunction<A,B,C,T> constructor, A a, MatchesAny b, MatchesAny c) {
            return new BiMatchConstructorBuilderR<T, B, C, R>() {
                public MatchBuilderR<T, R> then(BiFunction<B, C, R> f) {
                    T original = constructor.apply(a,null,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0), (C)missingProps.get(1));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> UniMatchConstructorBuilderR<T, A, R> when(TriFunction<A,B,C,T> constructor, MatchesAny a, B b, C c) {
            return new UniMatchConstructorBuilderR<T, A, R>() {
                public MatchBuilderR<T, R> then(Function<A, R> f) {
                    T original = constructor.apply(null,b,c);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((A)missingProps.get(0));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> UniMatchConstructorBuilderR<T, B, R> when(TriFunction<A,B,C,T> constructor, A a, MatchesAny b, C c) {
            return new UniMatchConstructorBuilderR<T, B, R>() {
                public MatchBuilderR<T, R> then(Function<B, R> f) {
                    T original = constructor.apply(a,null,c);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((B)missingProps.get(0));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public <A,B,C> UniMatchConstructorBuilderR<T, C, R> when(TriFunction<A,B,C,T> constructor, A a, B b, MatchesAny c) {
            return new UniMatchConstructorBuilderR<T, C, R>() {
                public MatchBuilderR<T, R> then(Function<C, R> f) {
                    T original = constructor.apply(a,b,null);
                    List<Object> missingProps = missingProps(MatchBuilderR.this.value, (Case<T>)original);
                    Function<T,R> valueExtractor = t -> f.apply((C)missingProps.get(0));
                    cases.add(MatchDefinition.create(original,valueExtractor));
                    return MatchBuilderR.this;
                }
            };
        }

        public R otherwise(R defaultValue) {
            return _(t -> defaultValue);
        }

        public R _(R defaultValue) {
            return _(t -> defaultValue);
        }

        public Optional<R> toOptional() {
            return Optional.ofNullable(_(t -> null));
        }

        public R _(Function<T,R> f) {
            defaultCase = f;
            return cases.stream()
                .filter(MatchDefinition.matches(value))
                .findFirst()
                .map(match -> match.f().apply((T)value))
                .orElseGet(() -> defaultCase.apply((T)value));
        }
    }

}



