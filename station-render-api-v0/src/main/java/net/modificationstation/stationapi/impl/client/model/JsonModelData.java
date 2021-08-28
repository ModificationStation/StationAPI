package net.modificationstation.stationapi.impl.client.model;

import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class JsonModelData {

    public final Map<String, String> textures;
    public final List<JsonCuboidData> elements;
}
