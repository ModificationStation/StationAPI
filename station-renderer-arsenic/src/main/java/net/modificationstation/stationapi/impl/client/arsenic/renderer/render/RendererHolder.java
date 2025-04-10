package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;

import java.util.Objects;

final class RendererHolder {
    private RendererHolder() { throw new RuntimeException("No"); }

    final static BakedModelRenderer RENDERER = Objects.requireNonNull(Renderer.get()).bakedModelRenderer();
}
