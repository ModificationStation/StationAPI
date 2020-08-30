package net.modificationstation.stationloader.impl.client.model;

import lombok.Data;

@Data
public class JsonFace {
    private double[] uv = new double[]{0,0,0,0};
    private String texture;
}
