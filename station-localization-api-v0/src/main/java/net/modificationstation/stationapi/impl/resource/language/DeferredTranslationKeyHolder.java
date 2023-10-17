package net.modificationstation.stationapi.impl.resource.language;

import java.util.function.Supplier;

public interface DeferredTranslationKeyHolder {
    void stationapi_initTranslationKey(Supplier<String> translator);
}
