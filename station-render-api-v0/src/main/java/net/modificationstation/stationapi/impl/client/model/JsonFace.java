package net.modificationstation.stationapi.impl.client.model;

import lombok.Data;

@Data
public class JsonFace {
    private double[] uv = new double[]{0, 0, 0, 0};
    private String texture = "#missing";
}
