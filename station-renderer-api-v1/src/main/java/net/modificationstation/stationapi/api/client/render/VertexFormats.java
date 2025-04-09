package net.modificationstation.stationapi.api.client.render;

public class VertexFormats {
    public static final VertexFormat BLOCK = VertexFormat.builder()
            .add("Position", VertexFormatElement.POSITION)
            .add("UV0", VertexFormatElement.UV0)
            .add("Color", VertexFormatElement.COLOR)
            .add("Normal", VertexFormatElement.NORMAL)
            .padding(4)
            .build();
}
