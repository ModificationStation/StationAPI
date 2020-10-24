package uk.co.benjiweber.expressions.exceptions;

import uk.co.benjiweber.expressions.functions.ExceptionalFunction;
import uk.co.benjiweber.expressions.functions.ExceptionalSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Result<T> {
    T unwrap();
    boolean success();
    <R> Result<R> map(Function<T,R> f);
    <R, E extends Exception> Result<R> mapExceptional(ExceptionalFunction<T,R,E> f);
    <R, E extends Exception> Result<R> map(Class<E> cls, Function<E,R> f);

    static class Success<T> implements Result<T> {
        private T value;

        public Success(T value) {
            this.value = value;
        }

        public T unwrap() {
            return value;
        }

        public boolean success() {
            return true;
        }

        public <R> Result<R> map(Function<T, R> f) {
            return new Success<R>(f.apply(value));
        }

        public <R, E extends Exception> Result<R> mapExceptional(ExceptionalFunction<T, R, E> f) {
            try {
                return new Success<R>(f.apply(value));
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                return new Failure<R>(e);
            }
        }

        public <R, E extends Exception> Result<R> map(Class<E> cls, Function<E, R> f) {
            return new Failure<>(new IllegalStateException());
        }
    }

    static class Failure<T> implements Result<T> {

        private Exception e;

        public Failure(Exception e) {
            this.e = e;
        }

        public <R> Failure(Result<R> t) {
            this.e = t instanceof Failure ? ((Failure)t).e : new IllegalStateException();
        }

        public T unwrap() {
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
        }

        public boolean success() {
            return false;
        }

        public <R> Result<R> map(Function<T, R> f) {
            return new Failure<R>(e);
        }

        public <R, E extends Exception> Result<R> mapExceptional(ExceptionalFunction<T, R, E> f) {
            return new Failure<R>(e);
        }

        public <R, E extends Exception> Result<R> map(Class<E> cls, Function<E, R> f) {
            if (e.getClass().isAssignableFrom(cls)) return new Success<R>(f.apply((E)e));
            return new Failure<R>(e);
        }
    }

    public static <R, E extends Exception> Supplier<Result<R>> wrapReturn(ExceptionalSupplier<R,E> f) {
        return () -> {
            try {
                return new Success<R>(f.supply());
            } catch (Exception e) {
                return new Failure<R>(e);
            }
        };
    }

    public static <T> Consumer<Result<T>> wrapConsumer(Consumer<T> f) {
        Function<T,T> f2 = t -> {
            f.accept(t);
            return t;
        };
        return t -> {
            try {
                t.map(f2);
            } catch (Exception e) {

            }
        };
    }

    public static <T, R, E extends Exception> Function<T,Result<R>> wrapReturn(ExceptionalFunction<T,R,E> f) {
        return t -> {
            try {
                return new Success<R>(f.apply(t));
            } catch (Exception e) {
                return new Failure<R>(e);
            }
        };
    }

    public static <T, R> Function<Result<T>,Result<R>> wrap(Function<T,R> f) {
        return t -> {
            try {
                return t.map(f);
            } catch (Exception e) {
                return new Failure<R>(e);
            }
        };
    }

    public static <T,R,E extends Exception> Function<Result<T>,Result<R>> wrapExceptional(ExceptionalFunction<T,R,E> f) {
        return t -> {
            try {
                return t.mapExceptional(f);
            } catch (Exception e) {
                return new Failure<R>(e);
            }
        };
    }

    public static class ResultMapper<T,R> {
        private Function<T, R> successMapper;
        private List<ExceptionHandler> exceptionHandlers = new ArrayList<>();

        public ResultMapper(Function<T,R> successMapper) {
            this.successMapper = successMapper;
        }

        public Function<Result<T>, Result<R>> mapper() {
            return t -> {
                if (t instanceof Success) return t.map(successMapper);
                for (ExceptionHandler handler : this.exceptionHandlers) {
                    Result<R> r = t.map(handler.type, handler.function);
                    if (r instanceof Success) return r;
                }
                return new Failure<R>(t);
            };
        }

        public <E extends Exception> ResultMapper<T,R> on(Class<E> eCls, Function<E,R> f) {
            exceptionHandlers.add(new ExceptionHandler(eCls, f));
            return this;
        }

    }

    static class ExceptionHandler<R> {
        final Class type;
        final Function<Exception, R> function;

        public ExceptionHandler(Class type, Function<Exception, R> function) {
            this.type = type;
            this.function = function;
        }
    }

    public static <T,R> ResultMapper<T,R> onSuccess(Function<T,R> f) {
        return new ResultMapper<T, R>(f);
    }

}
