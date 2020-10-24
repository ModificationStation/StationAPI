package uk.co.benjiweber.expressions;

public class InstanceOfMethodReference {

    public static void main(String... args) throws Exception {
        Object foo = "curry_and_partially_apply";
        Object bar = 5;
        Integer result =
            when(foo)
                .is(String.class)
                .then(String::compareTo).apply("other");

        System.out.println(result);
    }

    interface NoArg<T,U> {
        U apply(T instance);
    }

    interface OneArg<T,U,ARG1> {
        U apply(T instance, ARG1 arg1);
    }

    static class OneArgApplicator<I,R,ARG1> {
        private final OneArg<I,R,ARG1> action;
        private final I instance;

        OneArgApplicator(OneArg<I, R, ARG1> action, I instance) {
            this.action = action;
            this.instance = instance;
        }

        public R apply(ARG1 arg1) {
            return action.apply(instance, arg1);
        }
    }

    static interface When {
        public <T> Is<T> is(Class<T> cls);
    }

    static class Is<T> {

        private T instance;

        public Is(T instance) {
            this.instance = instance;
        }

        <U,V> OneArgApplicator<T,U,V> then(OneArg<T,U,V> action) throws Exception {
            return new OneArgApplicator<T, U, V>(action, instance);
        }

    }

    public static When when(final Object input) {
        return new When() {
            public <T> Is<T> is(Class<T> cls) {
                return new Is<T>((T)input);
            }
        };
    }

    public static <T,U,V> OneArgApplicator<T,U,V> then(T instance, OneArg<T,U,V> action) throws Exception {
        return new OneArgApplicator<T, U, V>(action, instance);
    }

}
