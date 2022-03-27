package net.modificationstation.stationapi.api.client.model.block;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.client.render.RendererAccess;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.util.Lazy;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RendererHolder {

    static final Lazy<BakedModelRenderer> BAKED_MODEL_RENDERER = new Lazy<>(() -> RendererAccess.INSTANCE.hasRenderer() ? Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer() : null);
}
