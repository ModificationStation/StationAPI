package net.modificationstation.stationloader.api.common.item;

import net.minecraft.item.ItemInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CustomReach {

    float getCustomBlockReach(ItemInstance itemInstance, float defaultReach);

    static void setDefaultBlockReach(Float defaultReach) {
        CONSUMERS.get("setDefaultBlockReach").accept(defaultReach);
    }

    static void setHandBlockReach(Float handReach) {
        CONSUMERS.get("setHandBlockReach").accept(handReach);
    }

    static Float getDefaultBlockReach() {
        return (Float) SUPPLIERS.get("getDefaultBlockReach").get();
    }

    static Float getHandBlockReach() {
        return (Float) SUPPLIERS.get("getHandBlockReach").get();
    }

    Map<String, Consumer<Object>> CONSUMERS = new HashMap<>();

    Map<String, Supplier<Object>> SUPPLIERS = new HashMap<>();
}
