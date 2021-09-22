package net.modificationstation.stationapi.impl.client.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.awt.image.*;
import java.util.stream.*;

@RequiredArgsConstructor
public class JsonFaceData {

    @SerializedName("uv")
    double[] localUVs = new double[] {0, 0, 16, 16};
    @Getter
    private transient double[] uv;
    @Getter
    int rotation = 0;
    @SerializedName("texture")
    public final String textureId;
    @Getter
    private transient Atlas.Sprite texture;
    public final Direction cullface = Null.get();

    public void postprocess(Atlas.Sprite texture) {
        this.texture = texture;
        IntStream.range(0, localUVs.length).forEach(i -> localUVs[i] /= 16);
    }

    public void updateUVs() {
        if (texture != null) {
            BufferedImage atlasImage = texture.getAtlas().getImage();
            int
                    textureX = texture.getX(),
                    textureY = texture.getY(),
                    textureWidth = texture.getWidth(),
                    textureHeight = texture.getHeight(),
                    atlasWidth = atlasImage.getWidth(),
                    atlasHeight = atlasImage.getHeight();
            double[] uv = new double[localUVs.length];
            for (int i = 0; i < localUVs.length; i += 2) {
                uv[i] = (textureX + localUVs[i] * textureWidth) / atlasWidth;
                uv[i + 1] = (textureY + localUVs[i + 1] * textureHeight) / atlasHeight;
            }
            this.uv = uv;
        }
    }
}
