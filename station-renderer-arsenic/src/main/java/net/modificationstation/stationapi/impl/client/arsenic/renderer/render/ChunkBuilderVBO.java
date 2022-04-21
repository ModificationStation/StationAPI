package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.RenderLayer;

import java.util.Map;

public interface ChunkBuilderVBO {

    Map<RenderLayer, VertexBuffer> getBuffers();
}
