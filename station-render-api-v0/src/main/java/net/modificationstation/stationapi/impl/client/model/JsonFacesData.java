package net.modificationstation.stationapi.impl.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JsonFacesData {

    private final JsonFaceData
            north,
            east,
            south,
            west,
            up,
            down;
}
