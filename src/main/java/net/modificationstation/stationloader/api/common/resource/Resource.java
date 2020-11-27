package net.modificationstation.stationloader.api.common.resource;

import net.modificationstation.stationloader.api.common.registry.Identifier;

import java.net.URL;

public class Resource {

    Resource(Identifier identifier, URL url) {
        this.identifier = identifier;
        this.url = url;
    }

    private final Identifier identifier;
    private final URL url;
}
