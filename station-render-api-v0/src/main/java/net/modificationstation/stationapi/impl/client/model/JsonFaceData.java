package net.modificationstation.stationapi.impl.client.model;

import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

import java.awt.image.*;

@RequiredArgsConstructor
public class JsonFaceData {

    public final String texture;
    double[] uv;
    private transient double[] adjustedUVs;

    public void updateUVs(Atlas.Texture texture) {
        BufferedImage atlasImage = texture.getAtlas().getImage();
        int
                textureX = texture.getX(),
                textureY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double[] adjustedUVs = new double[4];
        adjustedUVs[0] = (textureX + (uv[0] / 16) * textureWidth) / atlasWidth;
        adjustedUVs[1] = (textureY + (uv[1] / 16) * textureHeight) / atlasHeight;
        adjustedUVs[2] = (textureX + (uv[2] / 16) * textureWidth) / atlasWidth;
        adjustedUVs[3] = (textureY + (uv[3] / 16) * textureHeight) / atlasHeight;
        this.adjustedUVs = adjustedUVs;
    }

    public double[] getUv() {
        return adjustedUVs;
    }
}
