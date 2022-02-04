package net.modificationstation.stationapi.api.client.texture;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationFrameResourceMetadata;
import net.modificationstation.stationapi.api.client.resource.metadata.AnimationResourceMetadata;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class Sprite
implements AutoCloseable {
    private final SpriteAtlasTexture atlas;
    private final Info info;
    private final AnimationResourceMetadata animationMetadata;
    protected final NativeImage[] images;
    private final int[] frameXs;
    private final int[] frameYs;
    @Nullable
    private final Interpolation interpolation;
    private final int x;
    private final int y;
    private final float uMin;
    private final float uMax;
    private final float vMin;
    private final float vMax;
    private int frameIndex;
    private int frameTicks;

    public Sprite(SpriteAtlasTexture spriteAtlasTexture, Info info, int maxLevel, int atlasWidth, int atlasHeight, int x, int y, NativeImage nativeImage) {
        this.atlas = spriteAtlasTexture;
        AnimationResourceMetadata animationResourceMetadata = info.animationData;
        int i = info.width;
        int j = info.height;
        this.x = x;
        this.y = y;
        this.uMin = (float)x / (float)atlasWidth;
        this.uMax = (float)(x + i) / (float)atlasWidth;
        this.vMin = (float)y / (float)atlasHeight;
        this.vMax = (float)(y + j) / (float)atlasHeight;
        int k = nativeImage.getWidth() / animationResourceMetadata.getWidth(i);
        int l = nativeImage.getHeight() / animationResourceMetadata.getHeight(j);
        int n;
        int o;
        int p;
        if (animationResourceMetadata.getFrameCount() > 0) {
            int m = animationResourceMetadata.getFrameIndexSet().stream().max(Integer::compareTo).orElseThrow(NullPointerException::new) + 1;
            this.frameXs = new int[m];
            this.frameYs = new int[m];
            Arrays.fill(this.frameXs, -1);
            Arrays.fill(this.frameYs, -1);

            for (Integer integer : animationResourceMetadata.getFrameIndexSet()) {
                n = integer;
                if (n >= k * l) {
                    throw new RuntimeException("invalid frameindex " + n);
                }

                o = n / k;
                p = n % k;
                this.frameXs[n] = p;
                this.frameYs[n] = o;
            }
        } else {
            List<AnimationFrameResourceMetadata> list = Lists.newArrayList();
            int q = k * l;
            this.frameXs = new int[q];
            this.frameYs = new int[q];

            for(n = 0; n < l; ++n) {
                for(o = 0; o < k; ++o) {
                    p = n * k + o;
                    this.frameXs[p] = o;
                    this.frameYs[p] = n;
                    list.add(new AnimationFrameResourceMetadata(p, -1));
                }
            }

            animationResourceMetadata = new AnimationResourceMetadata(list, i, j, animationResourceMetadata.getDefaultFrameTime(), animationResourceMetadata.shouldInterpolate());
        }
        this.info = new Info(info.id, i, j, animationResourceMetadata);
        this.animationMetadata = animationResourceMetadata;
        try {
            try {
                this.images = MipmapHelper.getMipmapLevelsImages(nativeImage, maxLevel);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.create(throwable, "Generating mipmaps for frame");
                CrashReportSection crashReportSection = crashReport.addElement("Frame being iterated");
                crashReportSection.add("First frame", () -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append(nativeImage.getWidth()).append("x").append(nativeImage.getHeight());
                    return stringBuilder.toString();
                });
                throw new CrashException(crashReport);
            }
        }
        catch (Throwable throwable2) {
            CrashReport crashReport2 = CrashReport.create(throwable2, "Applying mipmap");
            CrashReportSection crashReportSection2 = crashReport2.addElement("Sprite being mipmapped");
            crashReportSection2.add("Sprite name", () -> this.getId().toString());
            crashReportSection2.add("Sprite size", () -> this.getWidth() + " x " + this.getHeight());
            crashReportSection2.add("Sprite frames", () -> this.getFrameCount() + " frames");
            crashReportSection2.add("Mipmap levels", maxLevel);
            throw new CrashException(crashReport2);
        }
        this.interpolation = animationResourceMetadata.shouldInterpolate() ? new Interpolation(info, maxLevel) : null;
    }

    private void upload(int frame) {
        int i = this.frameXs[frame] * this.info.getWidth();
        int j = this.frameYs[frame] * this.info.getHeight();
        this.upload(i, j, this.images);
    }

    private void upload(int frameX, int frameY, NativeImage[] output) {
        for (int i = 0; i < this.images.length; ++i) {
            output[i].upload(i, this.x >> i, this.y >> i, frameX >> i, frameY >> i, this.info.getWidth() >> i, this.info.getHeight() >> i, this.images.length > 1, false);
        }
    }

    public int getWidth() {
        return this.info.getWidth();
    }

    public int getHeight() {
        return this.info.getHeight();
    }

    public float getMinU() {
        return this.uMin;
    }

    public float getMaxU() {
        return this.uMax;
    }

    public float getFrameU(double frame) {
        float f = this.uMax - this.uMin;
        return this.uMin + f * (float)frame / 16.0f;
    }

    public float getMinV() {
        return this.vMin;
    }

    public float getMaxV() {
        return this.vMax;
    }

    public float getFrameV(double frame) {
        float f = this.vMax - this.vMin;
        return this.vMin + f * (float)frame / 16.0f;
    }

    public Identifier getId() {
        return this.info.getId();
    }

    public SpriteAtlasTexture getAtlas() {
        return this.atlas;
    }

    public int getFrameCount() {
        return this.frameXs.length;
    }

    @Override
    public void close() {
        for (NativeImage nativeImage : this.images) {
            if (nativeImage == null) continue;
            nativeImage.close();
        }
        if (this.interpolation != null) {
            this.interpolation.close();
        }
    }

    public String toString() {
        int i = this.frameXs.length;
        return "TextureAtlasSprite{name='" + this.info.getId() + '\'' + ", frameCount=" + i + ", x=" + this.x + ", y=" + this.y + ", height=" + this.info.getHeight() + ", width=" + this.info.getWidth() + ", u0=" + this.uMin + ", u1=" + this.uMax + ", v0=" + this.vMin + ", v1=" + this.vMax + '}';
    }

    public boolean isPixelTransparent(int frame, int x, int y) {
        return (this.images[0].getPixelColor(x + this.frameXs[frame] * this.info.getWidth(), y + this.frameYs[frame] * this.info.getHeight()) >> 24 & 0xFF) == 0;
    }

    public void upload() {
        this.upload(0);
    }

    private float getFrameDeltaFactor() {
        float f = (float)this.info.getWidth() / (this.uMax - this.uMin);
        float g = (float)this.info.getHeight() / (this.vMax - this.vMin);
        return Math.max(g, f);
    }

    public float getAnimationFrameDelta() {
        return 4.0f / this.getFrameDeltaFactor();
    }

    public void tickAnimation() {
        ++this.frameTicks;
        if (this.frameTicks >= this.animationMetadata.getFrameTime(this.frameIndex)) {
            int i = this.animationMetadata.getFrameIndex(this.frameIndex);
            int j = this.animationMetadata.getFrameCount() == 0 ? this.getFrameCount() : this.animationMetadata.getFrameCount();
            this.frameIndex = (this.frameIndex + 1) % j;
            this.frameTicks = 0;
            int k = this.animationMetadata.getFrameIndex(this.frameIndex);
            if (i != k && k >= 0 && k < this.getFrameCount()) {
                this.upload(k);
            }
        } else if (this.interpolation != null) {
            this.interpolation.apply();
        }
    }

    public boolean isAnimated() {
        return this.animationMetadata.getFrameCount() > 1;
    }

    @ApiStatus.Internal
    public NativeImage getBaseFrame() {
        return images[0];
    }

//    public VertexConsumer getTextureSpecificVertexConsumer(VertexConsumer vertexConsumer) {
//        return new SpriteTexturedVertexConsumer(vertexConsumer, this);
//    }

    @Environment(EnvType.CLIENT)
    final class Interpolation
    implements AutoCloseable {
        private final NativeImage[] images;

        private Interpolation(Info info, int mipmap) {
            this.images = new NativeImage[mipmap + 1];
            for (int i = 0; i < this.images.length; ++i) {
                int j = info.width >> i;
                int k = info.height >> i;
                if (this.images[i] != null) continue;
                this.images[i] = new NativeImage(j, k, false);
            }
        }

        private void apply() {
            double d = 1.0 - (double)frameTicks / (double)animationMetadata.getFrameTime(frameIndex);
            int i = animationMetadata.getFrameIndex(frameIndex);
            int j = animationMetadata.getFrameCount() == 0 ? getFrameCount() : animationMetadata.getFrameCount();
            int k = animationMetadata.getFrameIndex((frameIndex + 1) % j);
            if (i != k && k >= 0 && k < getFrameCount()) {
                for (int l = 0; l < this.images.length; ++l) {
                    int m = info.width >> l;
                    int n = info.height >> l;
                    for (int o = 0; o < n; ++o) {
                        for (int p = 0; p < m; ++p) {
                            int q = this.getPixelColor(i, l, p, o);
                            int r = this.getPixelColor(k, l, p, o);
                            int s = this.lerp(d, (q >> 16) & 0xFF, (r >> 16) & 0xFF);
                            int t = this.lerp(d, (q >> 8) & 0xFF, (r >> 8) & 0xFF);
                            int u = this.lerp(d, q & 0xFF, r & 0xFF);
                            this.images[l].setPixelColor(p, o, (q & 0xFF000000) | (s << 16) | (t << 8) | u);
                        }
                    }
                }
                upload(0, 0, this.images);
            }
        }

        private int getPixelColor(int frameIndex, int layer, int x, int y) {
            return Sprite.this.images[layer].getPixelColor(x + ((frameXs[frameIndex] * info.width) >> layer), y + ((frameYs[frameIndex] * info.height) >> layer));
        }

        private int lerp(double delta, int to, int from) {
            return (int)(delta * (double)to + (1.0 - delta) * (double)from);
        }

        public void close() {
            for (NativeImage nativeImage : this.images) {
                if (nativeImage == null) continue;
                nativeImage.close();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class Info {
        private final Identifier id;
        private final int width;
        private final int height;
        private final AnimationResourceMetadata animationData;

        public Info(Identifier id, int width, int height, AnimationResourceMetadata animationData) {
            this.id = id;
            this.width = width;
            this.height = height;
            this.animationData = animationData;
        }

        public Identifier getId() {
            return this.id;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
