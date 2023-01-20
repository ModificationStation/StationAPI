package net.modificationstation.stationapi.api.client.resource.metadata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class AnimationFrameResourceMetadata {
    public static final int UNDEFINED_TIME = -1;
    private final int index;
    private final int time;

    public AnimationFrameResourceMetadata(int index) {
        this(index, UNDEFINED_TIME);
    }

    public AnimationFrameResourceMetadata(int index, int time) {
        this.index = index;
        this.time = time;
    }

    public int getTime(int defaultTime) {
        return this.time == UNDEFINED_TIME ? defaultTime : this.time;
    }

    public int getIndex() {
        return this.index;
    }
}
