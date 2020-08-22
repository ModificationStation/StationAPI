package net.modificationstation.stationloader.impl.common.item;

public abstract class CustomReach implements net.modificationstation.stationloader.api.common.item.CustomReach {

    public static void setDefaultBlockReach(Object defaultReach) {
        CustomReach.defaultBlockReach = (Float) defaultReach;
    }

    public static void setHandBlockReach(Object handReach) {
        CustomReach.handBlockReach = (Float) handReach;
    }

    public static Object getDefaultBlockReach() {
        return defaultBlockReach;
    }

    public static Object getHandBlockReach() {
        return handBlockReach;
    }

    private static Float defaultBlockReach;
    private static Float handBlockReach;
}
