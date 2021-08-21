package net.modificationstation.stationapi.impl.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JsonCuboidData {

    private final double[] from;
    private final double[] to;
    private final JsonFacesData faces;
}
