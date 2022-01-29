package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class AnimationFrameResourceMetadata {
    private final int index;
    private final int time;

    public AnimationFrameResourceMetadata(int index) {
        this(index, -1);
    }

    public AnimationFrameResourceMetadata(int index, int time) {
        this.index = index;
        this.time = time;
    }

    public boolean usesDefaultFrameTime() {
        return this.time == -1;
    }

    public int getTime() {
        return this.time;
    }

    public int getIndex() {
        return this.index;
    }
}
