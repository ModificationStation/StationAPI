package net.modificationstation.stationloader.api.common.registry;

import java.net.URL;

public class Resource {

    Resource(Identifier identifier, URL url) {
        this.identifier = identifier;
        this.url = url;
    }

    private final Identifier identifier;
    private final URL url;
}
