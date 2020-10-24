package uk.co.benjiweber.expressions;

import java.util.LinkedHashMap;
import java.util.Map;

public class Try {

    public static <T> TryBuilder<T> Try(Action<T> action) {
        return new TryBuilder<>(action);
    }

    static interface Catch<T> {
        <U extends Throwable> TryBuilder<T> Catch(Class<U> cls, ActionWithOneParam<T,U> action);
    }
    static interface Finally<T> {
        TryBuilder<T> Finally(Block action);
    }
    public static class TryBuilder<T> implements Catch<T>, Finally<T> {

        private Action<T> mainAction;

        Map<Class<? extends Throwable>, ActionWithOneParam<T,? extends Throwable>> catches = new LinkedHashMap<>();
        public Block finalAction;

        public TryBuilder(Action<T> mainAction) {

            this.mainAction = mainAction;
        }
        public T apply() {
            try {
                return mainAction.apply();
            } catch (RuntimeException e) {
                return handle(e, ex -> {throw ex;});
            } catch (Error e) {
                return handle(e, ex -> {throw ex;});
            } catch (Exception e) {
                return handle(e, ex -> {throw new RuntimeException(ex);});
            } finally {
                if (finalAction != null) {
                    try {
                        finalAction.apply();
                    } catch (RuntimeException e) {
                        return handle(e, ex -> {throw ex;});
                    } catch (Error e) {
                        return handle(e, ex -> {throw ex;});
                    } catch (Exception e) {
                        return handle(e, ex -> {throw new RuntimeException(ex);});
                    }
                }
            }
        }

        interface Thrower<E extends Throwable, W extends Throwable, T> {
            T doThrow(E e) throws W;
        }

        private <E extends Throwable, W extends Throwable> T handle(E e, Thrower<E,W,T> thrower) throws W {
            for (Class<? extends Throwable> cls : catches.keySet()) {
                if (cls.isAssignableFrom(e.getClass())) {
                    return runCatchBlock(catches.get(cls),e);
                }
            }
            return thrower.doThrow(e);
        }

        @SuppressWarnings("unchecked")
        public <U extends Throwable> T runCatchBlock(ActionWithOneParam<T,U> catchBlock, Throwable throwable) {
            try {
                return catchBlock.apply((U)throwable);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public TryBuilder<T> Finally(Block action) {
            this.finalAction = action;
            return this;
        }

        @Override
        public <U extends Throwable> TryBuilder<T> Catch(Class<U> cls, ActionWithOneParam<T,U> action) {
            ActionWithOneParam<T,U> foo = action::apply;
            this.catches.put(cls, foo);
            return this;
        }

    }
}
