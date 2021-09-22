package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextureAnimationData {

    public final int frametime;
    public final ImmutableList<Frame> frames;
    public final boolean interpolate;

    @RequiredArgsConstructor
    public static class Frame {

        public final int index;
        public final int time;
    }
}
