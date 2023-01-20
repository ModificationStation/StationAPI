package net.modificationstation.stationapi.api.client.render.chunk;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.BufferBuilder;
import net.modificationstation.stationapi.api.client.render.RenderLayer;

import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class BlockBufferBuilderStorage {
    private final Map<RenderLayer, BufferBuilder> builders = RenderLayer.getBlockLayers().stream().collect(Collectors.toMap(renderLayer -> renderLayer, renderLayer -> new BufferBuilder(renderLayer.getExpectedBufferSize())));

    public BufferBuilder get(RenderLayer layer) {
        return this.builders.get(layer);
    }

    public void clear() {
        this.builders.values().forEach(BufferBuilder::clear);
    }

    public void reset() {
        this.builders.values().forEach(BufferBuilder::reset);
    }
}

