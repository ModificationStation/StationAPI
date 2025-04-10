package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.mesh.QuadView;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * @deprecated functionality of this class will be replaced by {@link QuadView}
 */
@Environment(EnvType.CLIENT)
@Deprecated(forRemoval = true)
public record BakedQuad(int[] vertexData, int tintIndex, Direction face, Sprite sprite, boolean shade, float lightEmission) {
    public BakedQuad(int[] vertexData, int colorIndex, Direction face, Sprite sprite, boolean shade) {
        this(vertexData, colorIndex, face, sprite, shade, 0);
    }

    public boolean hasTint() {
        return this.tintIndex != -1;
    }
}
