package net.modificationstation.stationapi.api.client.resource.metadata;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(value=EnvType.CLIENT)
public class AnimationResourceMetadata {
    public static final AnimationResourceMetadataReader READER = new AnimationResourceMetadataReader();
    public static final AnimationResourceMetadata EMPTY = new AnimationResourceMetadata(Lists.newArrayList(), -1, -1, 1, false){

        @Override
        public Pair<Integer, Integer> ensureImageSize(int x, int y) {
            return Pair.of(x, y);
        }
    };
    private final List<AnimationFrameResourceMetadata> frames;
    private final int width;
    private final int height;
    private final int defaultFrameTime;
    private final boolean interpolate;

    public AnimationResourceMetadata(List<AnimationFrameResourceMetadata> frames, int width, int height, int defaultFrameTime, boolean interpolate) {
        this.frames = frames;
        this.width = width;
        this.height = height;
        this.defaultFrameTime = defaultFrameTime;
        this.interpolate = interpolate;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isMultipleOf(int dividend, int divisor) {
        return dividend / divisor * divisor == dividend;
    }

    public Pair<Integer, Integer> ensureImageSize(int x, int y) {
        Pair<Integer, Integer> pair = this.getSize(x, y);
        int i = pair.getFirst();
        int j = pair.getSecond();
        if (!AnimationResourceMetadata.isMultipleOf(x, i) || !AnimationResourceMetadata.isMultipleOf(y, j)) {
            throw new IllegalArgumentException(String.format("Image size %s,%s is not multiply of frame size %s,%s", x, y, i, j));
        }
        return pair;
    }

    private Pair<Integer, Integer> getSize(int defaultWidth, int defaultHeight) {
        if (this.width != -1) {
            if (this.height != -1) {
                return Pair.of(this.width, this.height);
            }
            return Pair.of(this.width, defaultHeight);
        }
        if (this.height != -1) {
            return Pair.of(defaultWidth, this.height);
        }
        int i = Math.min(defaultWidth, defaultHeight);
        return Pair.of(i, i);
    }

    public int getHeight(int i) {
        return this.height == -1 ? i : this.height;
    }

    public int getWidth(int i) {
        return this.width == -1 ? i : this.width;
    }

    public int getDefaultFrameTime() {
        return this.defaultFrameTime;
    }

    public boolean shouldInterpolate() {
        return this.interpolate;
    }

    public void forEachFrame(FrameConsumer consumer) {
        for (AnimationFrameResourceMetadata animationFrameResourceMetadata : this.frames) {
            consumer.accept(animationFrameResourceMetadata.getIndex(), animationFrameResourceMetadata.getTime(this.defaultFrameTime));
        }
    }

    @FunctionalInterface
    @Environment(value=EnvType.CLIENT)
    public interface FrameConsumer {
        void accept(int var1, int var2);
    }
}
