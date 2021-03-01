package net.modificationstation.stationapi.api.common.factory;

import net.modificationstation.stationapi.api.common.util.HasHandler;

import java.util.function.*;

public interface GeneralFactory extends HasHandler<GeneralFactory> {

    GeneralFactory INSTANCE = new GeneralFactory() {

        private GeneralFactory handler;

        @Override
        public void setHandler(GeneralFactory handler) {
            this.handler = handler;
        }

        @Override
        public boolean hasFactory(Class<?> clazz) {
            checkAccess(handler);
            return handler.hasFactory(clazz);
        }

        @Override
        public <T> T newInst(Class<T> clazz, Object... args) {
            checkAccess(handler);
            return handler.newInst(clazz, args);
        }

        @Override
        public <T> void addFactory(Class<T> clazz, Function<Object[], T> factory) {
            checkAccess(handler);
            handler.addFactory(clazz, factory);
        }
    };

    boolean hasFactory(Class<?> clazz);

    <T> T newInst(Class<T> clazz, Object... args);

    <T> void addFactory(Class<T> clazz, Function<Object[], T> factory);
}
