package net.modificationstation.stationloader.impl.client.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class JsonModel {
    private HashMap<String, String> textures;
    private JsonCuboid[] elements;
}
