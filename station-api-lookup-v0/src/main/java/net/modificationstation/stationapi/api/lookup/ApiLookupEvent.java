package net.modificationstation.stationapi.api.lookup;

import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.exception.LookupException;

import java.util.Optional;

public abstract class ApiLookupEvent extends Event {
    public final Class<?> apiClass;
    private Object apiInstance;

    public ApiLookupEvent(Class<?> apiClass) {
        this.apiClass = apiClass;
    }

    public void found(Object apiInstance) {
        if (!apiClass.isInstance(apiInstance)) {
            throw new LookupException(apiClass, apiInstance.getClass());
        }
        this.apiInstance = apiInstance;
        if (this.isCancelable()) {
            this.cancel();
        }
    }

    public Optional<?> getApi() {
        if (apiClass.isInstance(this.apiInstance)) {
            return Optional.of(apiInstance);
        } else {
            return Optional.empty();
        }
    }

}
