package net.modificationstation.stationloader.api.common.factory;

import net.modificationstation.stationloader.api.common.util.HasHandler;

public interface EnumFactory extends HasHandler<EnumFactory> {

    EnumFactory INSTANCE = new EnumFactory() {

        private EnumFactory handler;

        @Override
        public void setHandler(EnumFactory handler) {
            this.handler = handler;
        }

        @Override
        public <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues) {
            checkAccess(handler);
            return handler.addEnum(enumType, enumName, paramTypes, paramValues);
        }
    };

    <T extends Enum<? >> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object[] paramValues);
}
