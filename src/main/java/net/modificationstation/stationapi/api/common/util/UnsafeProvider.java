package net.modificationstation.stationapi.api.common.util;

import sun.misc.Unsafe;

public interface UnsafeProvider extends HasHandler<UnsafeProvider> {

    UnsafeProvider INSTANCE = new UnsafeProvider() {

        private UnsafeProvider handler;

        @Override
        public void setHandler(UnsafeProvider handler) {
            this.handler = handler;
        }

        @Override
        public Unsafe getUnsafe() {
            checkAccess(handler);
            return handler.getUnsafe();
        }
    };

    Unsafe getUnsafe();
}
