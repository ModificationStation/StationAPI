package net.modificationstation.stationapi.api.client.texture;

import lombok.Getter;

public class ExtendableTextureSet extends TextureSet {

    @Getter
    private int texturesWidth, texturesHeight;

    public ExtendableTextureSet(int texturesWidth, int texturesHeight) {
        this.texturesWidth = texturesWidth;
        this.texturesHeight = texturesHeight;
    }
}
