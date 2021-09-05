package net.modificationstation.stationapi.impl.client.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;

import java.awt.image.*;
import java.util.stream.*;

@RequiredArgsConstructor
public class JsonFaceData {

    @SerializedName("uv")
    double[] localUVs;
    @Getter
    private transient double[] uv;
    @Getter
    int rotation = 0;
    @SerializedName("texture")
    public final String textureId;
    @Getter
    private transient Atlas.Texture texture;

    public void postprocess(Atlas.Texture texture) {
        this.texture = texture;
        IntStream.range(0, localUVs.length).forEach(i -> localUVs[i] /= 16);
    }

    public void updateUVs() {
        BufferedImage atlasImage = texture.getAtlas().getImage();
        int
                textureX = texture.getX(),
                textureY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double[] uv = new double[localUVs.length];
        for (int i = 0; i < localUVs.length; i+=2) {
            uv[i] = (textureX + localUVs[i] * textureWidth) / atlasWidth;
            uv[i + 1] = (textureY + localUVs[i + 1] * textureHeight) / atlasHeight;
        }
        this.uv = uv;
    }
}
