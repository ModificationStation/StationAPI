package net.modificationstation.stationapi.api.client.texture;

import lombok.Getter;

public class Atlas {

    private final int id;
    private boolean[] override;
    @Getter
    private int texturesWidth, texturesHeight;

    public Atlas(int id, int textureWidth, int textureHeight) {
        this.id = id;
        override = new boolean[textureWidth * textureHeight];
        this.texturesWidth = textureWidth;
        this.texturesHeight = textureHeight;
    }

    public boolean hasOverrideSpace() {
        for(boolean b : override) if(!b) return false;
        return true;
    }

    public int getNextOverrideIndex() {
        int i = 0;
        while (override[i]) i++;
        return i;
    }
}
