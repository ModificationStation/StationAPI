package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import lombok.Getter;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class SpriteContents implements TextureStitcher.Stitchable, AutoCloseable {
    private final Identifier id;
    final int width;
    final int height;
    private final NativeImage image;
    @Getter
    @Nullable
    private final Animation animation;

    public SpriteContents(Identifier id, SpriteDimensions dimensions, NativeImage image, AnimationResourceMetadata metadata) {
        this.id = id;
        this.width = dimensions.width();
        this.height = dimensions.height();
        this.animation = createAnimation(dimensions, image.getWidth(), image.getHeight(), metadata);
        this.image = image;
    }

    private int getFrameCount() {
        return animation != null ? animation.frames.size() : 1;
    }

    @Nullable
    private Animation createAnimation(SpriteDimensions dimensions, int imageWidth, int imageHeight, AnimationResourceMetadata metadata) {
        int i2 = imageWidth / dimensions.width();
        int j = imageHeight / dimensions.height();
        int k = i2 * j;
        List<AnimationFrame> list = new ArrayList<>();
        metadata.forEachFrame((index, frameTime) -> list.add(new AnimationFrame(index, frameTime)));
        if (list.isEmpty()) for (int l = 0; l < k; ++l) list.add(new AnimationFrame(l, metadata.getDefaultFrameTime()));
        else {
            int l = 0;
            IntOpenHashSet intSet = new IntOpenHashSet();
            Iterator<AnimationFrame> iterator = list.iterator();
            while (iterator.hasNext()) {
                AnimationFrame animationFrame = iterator.next();
                boolean bl = true;
                if (animationFrame.time <= 0) {
                    LOGGER.warn("Invalid frame duration on spriteId {} frame {}: {}", id, l, animationFrame.time);
                    bl = false;
                }
                if (animationFrame.index < 0 || animationFrame.index >= k) {
                    LOGGER.warn("Invalid frame index on spriteId {} frame {}: {}", id, l, animationFrame.index);
                    bl = false;
                }
                if (bl) intSet.add(animationFrame.index);
                else iterator.remove();
                ++l;
            }
            int[] is = IntStream.range(0, k).filter(i -> !intSet.contains(i)).toArray();
            if (is.length > 0)
                LOGGER.warn("Unused frames in spriteId {}: {}", id, Arrays.toString(is));
        }
        if (list.size() <= 1) return null;
        return new Animation(ImmutableList.copyOf(list), i2, metadata.shouldInterpolate());
    }

    void upload(int x, int y, int unpackSkipPixels, int unpackSkipRows, NativeImage image) {
        image.upload(0, x, y, unpackSkipPixels, unpackSkipRows, width, height, false, false);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public IntStream getDistinctFrameCount() {
        return animation != null ? animation.getDistinctFrameCount() : IntStream.of(1);
    }

    @Nullable
    public Animator createAnimator() {
        return animation != null ? animation.createAnimator() : null;
    }

    @Override
    public void close() {
        image.close();
    }

    public String toString() {
        return "SpriteContents{name=" + id + ", frameCount=" + getFrameCount() + ", height=" + height + ", width=" + width + "}";
    }

    public boolean isPixelTransparent(int frame, int x, int y) {
        int i = x;
        int j = y;
        if (animation != null) {
            i += animation.getFrameX(frame) * width;
            j += animation.getFrameY(frame) * height;
        }
        return (image.getColor(i, j) >> 24 & 0xFF) == 0;
    }

    public void upload(int x, int y) {
        if (this.animation != null) this.animation.upload(x, y);
        else upload(x, y, 0, 0, image);
    }

    @ApiStatus.Internal
    public NativeImage getBaseFrame() {
        return image;
    }

    class Animation {
        final List<AnimationFrame> frames;
        private final int frameCount;
        private final boolean interpolation;

        Animation(List<AnimationFrame> frames, int frameCount, boolean interpolation) {
            this.frames = frames;
            this.frameCount = frameCount;
            this.interpolation = interpolation;
        }

        int getFrameX(int frame) {
            return frame % frameCount;
        }

        int getFrameY(int frame) {
            return frame / frameCount;
        }

        void upload(int x, int y, int frame) {
            int i = getFrameX(frame) * width;
            int j = getFrameY(frame) * height;
            SpriteContents.this.upload(x, y, i, j, image);
        }

        public Animator createAnimator() {
            return new AnimatorImpl(this, interpolation ? new Interpolation() : null);
        }

        public void upload(int x, int y) {
            upload(x, y, frames.get(0).index);
        }

        public IntStream getDistinctFrameCount() {
            return frames.stream().mapToInt(frame -> frame.index).distinct();
        }
    }

    record AnimationFrame(int index, int time) {}

    class AnimatorImpl
    implements Animator {
        int frame;
        int currentTime;
        final Animation animation;
        @Nullable
        private final Interpolation interpolation;

        AnimatorImpl(Animation animation, @Nullable Interpolation interpolation) {
            this.animation = animation;
            this.interpolation = interpolation;
        }

        @Override
        public void tick(int x, int y) {
            ++currentTime;
            AnimationFrame animationFrame = animation.frames.get(frame);
            if (currentTime >= animationFrame.time) {
                int i = animationFrame.index;
                frame = (frame + 1) % animation.frames.size();
                currentTime = 0;
                int j = animation.frames.get(frame).index;
                if (i != j) animation.upload(x, y, j);
            } else if (interpolation != null) interpolation.apply(x, y, this);
        }

        @Override
        public void close() {
            if (interpolation != null) interpolation.close();
        }
    }

    final class Interpolation implements AutoCloseable {
        private final NativeImage interpImage = new NativeImage(width, height, false);

        void apply(int x, int y, AnimatorImpl animator) {
            Animation animation = animator.animation;
            List<AnimationFrame> list = animation.frames;
            AnimationFrame animationFrame = list.get(animator.frame);
            double d = 1.0 - (double)animator.currentTime / (double)animationFrame.time;
            int i = animationFrame.index;
            int j = list.get((animator.frame + 1) % list.size()).index;
            if (i != j) {
                for (int n = 0; n < height; ++n)
                    for (int o = 0; o < width; ++o) {
                        int p = getPixelColor(animation, i, o, n);
                        int q = getPixelColor(animation, j, o, n);
                        interpImage.setColor(o, n,
                                p & 0xFF000000 |
                                        lerp(d, p >> 16 & 0xFF, q >> 16 & 0xFF) << 16 |
                                        lerp(d, p >> 8 & 0xFF, q >> 8 & 0xFF) << 8 |
                                        lerp(d, p & 0xFF, q & 0xFF)
                        );
                    }
                upload(x, y, 0, 0, interpImage);
            }
        }

        private int getPixelColor(Animation animation, int frameIndex, int x, int y) {
            return image.getColor(x + (animation.getFrameX(frameIndex) * width), y + (animation.getFrameY(frameIndex) * height));
        }

        private int lerp(double delta, int to, int from) {
            return (int)(delta * (double)to + (1.0 - delta) * (double)from);
        }

        @Override
        public void close() {
            interpImage.close();
        }
    }
}

