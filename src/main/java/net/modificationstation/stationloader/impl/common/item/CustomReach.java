package net.modificationstation.stationloader.impl.common.item;

public abstract class CustomReach implements net.modificationstation.stationloader.api.common.item.CustomReach {

    public static void setDefaultBlockReach(Object defaultBlockReach) {
        CustomReach.defaultBlockReach = (Float) defaultBlockReach;
    }

    public static void setHandBlockReach(Object handBlockReach) {
        CustomReach.handBlockReach = (Float) handBlockReach;
    }

    public static Object getDefaultBlockReach() {
        return defaultBlockReach;
    }

    public static Object getHandBlockReach() {
        return handBlockReach;
    }

    public static void setDefaultEntityReach(Object defaultEntityReach) {
        CustomReach.defaultEntityReach = (Double) defaultEntityReach;
    }

    public static void setHandEntityReach(Object handEntityReach) {
        CustomReach.handEntityReach = (Double) handEntityReach;
    }

    public static Object getDefaultEntityReach() {
        return defaultEntityReach;
    }

    public static Object getHandEntityReach() {
        return handEntityReach;
    }

    private static Float defaultBlockReach;
    private static Float handBlockReach;
    private static Double defaultEntityReach;
    private static Double handEntityReach;
}
