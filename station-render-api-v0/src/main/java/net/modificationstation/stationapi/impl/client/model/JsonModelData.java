package net.modificationstation.stationapi.impl.client.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class JsonModelData {

    @SuppressWarnings("FieldMayBeFinal") // had to make it non-final with a getter because javac is retarded
    @Getter
    private boolean ambientocclusion = true;
    public final Map<String, String> textures;
    public final List<JsonCuboidData> elements;
}
