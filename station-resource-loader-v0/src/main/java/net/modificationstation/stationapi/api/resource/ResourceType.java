package net.modificationstation.stationapi.api.resource;

public enum ResourceType {
    CLIENT_RESOURCES("assets"),
    SERVER_DATA("data");

    private final String directory;

    ResourceType(String name) {
        this.directory = name;
    }

    public String getDirectory() {
        return this.directory;
    }
}

