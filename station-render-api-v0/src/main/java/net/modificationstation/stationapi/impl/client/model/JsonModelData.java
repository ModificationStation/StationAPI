package net.modificationstation.stationapi.impl.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Getter
public class JsonModelData {

    private final Map<String, String> textures;
    private final List<JsonCuboidData> elements;
}
