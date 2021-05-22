package net.modificationstation.stationapi.impl.client.model;

import lombok.Data;

@Data
public class JsonCuboid {
    private double[] from;
    private double[] to;
    private double color;
    private JsonFaces faces;
}
