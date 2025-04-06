package net.modificationstation.stationapi.api.client.render;

public class VertexFormats {
    public static final VertexFormat BLOCK = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("Color", VertexFormatElement.COLOR)
            .add("UV0", VertexFormatElement.UV0)
            .add("Normal", VertexFormatElement.NORMAL)
            .build();
}
