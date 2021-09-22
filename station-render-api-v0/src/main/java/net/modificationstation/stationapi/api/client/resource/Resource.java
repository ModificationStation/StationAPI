package net.modificationstation.stationapi.api.client.resource;

import java.io.*;
import java.util.*;

public interface Resource {

    InputStream getResource();

    default Optional<InputStream> getMeta() {
        return Optional.empty();
    }

    static Resource of(InputStream stream) {
        return stream instanceof Resource ? (Resource) stream : () -> stream;
    }
}
