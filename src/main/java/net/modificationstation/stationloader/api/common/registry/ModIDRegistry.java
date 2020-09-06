package net.modificationstation.stationloader.api.common.registry;

import java.util.HashMap;
import java.util.Map;

public interface ModIDRegistry {

    Map<Class<?>, Map<String, Map<String, Integer>>> registries = new HashMap<>();
}
