package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.resource.ResourceType;

public enum ResourcePackCompatibility {
    TOO_OLD("old"),
    TOO_NEW("new"),
    COMPATIBLE("compatible");

    private final String notification;
    private final String confirmMessage;

    ResourcePackCompatibility(String translationSuffix) {
        this.notification = "pack.incompatible." + translationSuffix;
        this.confirmMessage = "pack.incompatible.confirm." + translationSuffix;
    }

    public boolean isCompatible() {
        return this == COMPATIBLE;
    }

    public static ResourcePackCompatibility from(int packVersion, ResourceType type) {
        int i = switch (type) {
            case CLIENT_RESOURCES -> 13;
            case SERVER_DATA -> 12;
        };
        if (packVersion < i) return TOO_OLD;
        if (packVersion > i) return TOO_NEW;
        return COMPATIBLE;
    }

    public String getNotification() {
        return this.notification;
    }

    public String getConfirmMessage() {
        return this.confirmMessage;
    }
}

