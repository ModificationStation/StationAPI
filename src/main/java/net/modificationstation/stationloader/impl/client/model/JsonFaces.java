package net.modificationstation.stationloader.impl.client.model;

import lombok.Data;

@Data
public class JsonFaces {
    private JsonFace north = new JsonFace();
    private JsonFace east = new JsonFace();
    private JsonFace south = new JsonFace();
    private JsonFace west = new JsonFace();
    private JsonFace up = new JsonFace();
    private JsonFace down = new JsonFace();
}
