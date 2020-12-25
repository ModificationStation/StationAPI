package net.modificationstation.stationapi.api.common.resource;

import net.modificationstation.stationapi.api.common.registry.Identifier;

import java.net.URL;

public class Resource {

    private final Identifier identifier;
    private final URL url;
    Resource(Identifier identifier, URL url) {
        this.identifier = identifier;
        this.url = url;
    }
}
