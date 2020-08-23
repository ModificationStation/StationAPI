package net.modificationstation.stationloader.api.common.item;

import net.minecraft.item.ItemInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CustomReach {

    float getCustomBlockReach(ItemInstance itemInstance, float defaultReach);

    double getCustomEntityReach(ItemInstance itemInstance, double defaultReach);

    static void setDefaultBlockReach(Float defaultBlockReach) {
        CONSUMERS.get("setDefaultBlockReach").accept(defaultBlockReach);
    }

    static void setHandBlockReach(Float handBlockReach) {
        CONSUMERS.get("setHandBlockReach").accept(handBlockReach);
    }

    static Float getDefaultBlockReach() {
        return (Float) SUPPLIERS.get("getDefaultBlockReach").get();
    }

    static Float getHandBlockReach() {
        return (Float) SUPPLIERS.get("getHandBlockReach").get();
    }

    static void setDefaultEntityReach(Double defaultEntityReach) {
        CONSUMERS.get("setDefaultEntityReach").accept(defaultEntityReach);
    }

    static void setHandEntityReach(Double handEntityReach) {
        CONSUMERS.get("setHandEntityReach").accept(handEntityReach);
    }

    static Double getDefaultEntityReach() {
        return (Double) SUPPLIERS.get("getDefaultEntityReach").get();
    }

    static Double getHandEntityReach() {
        return (Double) SUPPLIERS.get("getHandEntityReach").get();
    }

    Map<String, Consumer<Object>> CONSUMERS = new HashMap<>();

    Map<String, Supplier<Object>> SUPPLIERS = new HashMap<>();
}
