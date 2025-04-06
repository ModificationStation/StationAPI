package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;

@Environment(EnvType.CLIENT)
public record BakedQuad(int[] vertexData, int tintIndex, Direction face, Sprite sprite, boolean shade, float lightEmission) {
    public BakedQuad(int[] vertexData, int colorIndex, Direction face, Sprite sprite, boolean shade) {
        this(vertexData, colorIndex, face, sprite, shade, 0);
    }

    public boolean hasTint() {
        return this.tintIndex != -1;
    }
}
