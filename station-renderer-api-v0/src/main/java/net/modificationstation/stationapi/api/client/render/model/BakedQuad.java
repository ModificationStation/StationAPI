package net.modificationstation.stationapi.api.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.util.math.Direction;

@Environment(EnvType.CLIENT)
public class BakedQuad {
    protected final int[] vertexData;
    protected final int colorIndex;
    protected final Direction face;
    protected final Sprite sprite;
    private final boolean shade;
    private final float emission;

    public BakedQuad(int[] vertexData, int colorIndex, Direction face, Sprite sprite, boolean shade) {
        this(vertexData, colorIndex, face, sprite, shade, 0);
    }

    public BakedQuad(int[] vertexData, int colorIndex, Direction face, Sprite sprite, boolean shade, float emission) {
        this.vertexData = vertexData;
        this.colorIndex = colorIndex;
        this.face = face;
        this.sprite = sprite;
        this.shade = shade;
        this.emission = emission;
    }

    public int[] getVertexData() {
        return this.vertexData;
    }

    public boolean hasColor() {
        return this.colorIndex != -1;
    }

    public int getColorIndex() {
        return this.colorIndex;
    }

    public Direction getFace() {
        return this.face;
    }

    public boolean hasShade() {
        return this.shade;
    }

    public float getEmission() {
        return emission;
    }
}
