package net.modificationstation.stationloader.impl.client.model;

import lombok.Data;

@Data
public class JsonCuboid {
    private double[] from;
    private double[] to;
    private double color;
    private JsonFaces faces;
}
