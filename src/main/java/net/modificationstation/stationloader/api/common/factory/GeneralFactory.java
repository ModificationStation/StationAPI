package net.modificationstation.stationloader.api.common.factory;

import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.function.Function;

public interface GeneralFactory extends HasHandler<GeneralFactory> {

    GeneralFactory INSTANCE = new GeneralFactory() {

        private GeneralFactory handler;

        @Override
        public void setHandler(GeneralFactory handler) {
            this.handler = handler;
        }

        @Override
        public boolean hasFactory(Class<?> clazz) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.hasFactory(clazz);
        }

        @Override
        public <T> T newInst(Class<T> clazz, Object... args) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.newInst(clazz, args);
        }

        @Override
        public void addFactory(Class<?> clazz, Function<Object[], Object> factory) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.addFactory(clazz, factory);
        }
    };

    boolean hasFactory(Class<?> clazz);

    <T> T newInst(Class<T> clazz, Object... args);

    void addFactory(Class<?> clazz, Function<Object[], Object> factory);
}
