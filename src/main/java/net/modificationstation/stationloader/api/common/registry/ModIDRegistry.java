package net.modificationstation.stationloader.api.common.registry;

import net.modificationstation.stationloader.impl.common.packet.CustomData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public interface ModIDRegistry {

    Map<String, Map<String, Integer>> item = new HashMap<>();
    Map<String, Map<String, Consumer<CustomData>>> packet = new HashMap<>();
}
