package net.modificationstation.stationapi.api.client.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.awt.image.*;
import java.util.*;

@Environment(value=EnvType.CLIENT)
public class Sprite
/*implements AutoCloseable*/ {
    private final ExpandableAtlas atlas;
    private final Atlas.Sprite info;
    private final AnimationResourceMetadata animationMetadata;
    protected final BufferedImage[] images;
    private final int[] frameXs;
    private final int[] frameYs;
//    @Nullable
//    private final Interpolation interpolation;
    private final int x;
    private final int y;
    private final float uMin;
    private final float uMax;
    private final float vMin;
    private final float vMax;
//    private int frameIndex;
//    private int frameTicks;

    public Sprite(ExpandableAtlas spriteAtlasTexture, Atlas.Sprite info, int maxLevel, int atlasWidth, int atlasHeight, int x, int y, BufferedImage nativeImage) {
        this.atlas = spriteAtlasTexture;
        AnimationResourceMetadata animationResourceMetadata = info.animationData;
        int i = info.getWidth();
        int j = info.getHeight();
        this.x = x;
        this.y = y;
        this.uMin = (float)x / (float)atlasWidth;
        this.uMax = (float)(x + i) / (float)atlasWidth;
        this.vMin = (float)y / (float)atlasHeight;
        this.vMax = (float)(y + j) / (float)atlasHeight;
        int k = nativeImage.getWidth() / animationResourceMetadata.getWidth(i);
        int l = nativeImage.getHeight() / animationResourceMetadata.getHeight(j);
        if (animationResourceMetadata.getFrameCount() > 0) {
            int m = animationResourceMetadata.getFrameIndexSet().stream().max(Integer::compareTo).orElseThrow(NullPointerException::new) + 1;
            this.frameXs = new int[m];
            this.frameYs = new int[m];
            Arrays.fill(this.frameXs, -1);
            Arrays.fill(this.frameYs, -1);
            for (Integer integer : animationResourceMetadata.getFrameIndexSet()) {
                int n = integer;
                if (n >= k * l) {
                    throw new RuntimeException("invalid frameindex " + n);
                }
                int o = n / k;
                this.frameXs[n] = n % k;
                this.frameYs[n] = o;
            }
        } else {
            List<AnimationFrameResourceMetadata> list = new ArrayList<>();
            int q = k * l;
            this.frameXs = new int[q];
            this.frameYs = new int[q];
            for (int r = 0; r < l; ++r) {
                int s = 0;
                while (s < k) {
                    int t = r * k + s;
                    this.frameXs[t] = s++;
                    this.frameYs[t] = r;
                    list.add(new AnimationFrameResourceMetadata(t, -1));
                }
            }
            animationResourceMetadata = new AnimationResourceMetadata(list, i, j, animationResourceMetadata.getDefaultFrameTime(), animationResourceMetadata.shouldInterpolate());
        }
//        this.info = new Info(info.id, i, j, animationResourceMetadata);
        this.info = info;
        this.animationMetadata = animationResourceMetadata;
        try {
            try {
                this.images = MipmapHelper.getMipmapLevelsImages(nativeImage, maxLevel);
            }
            catch (Throwable throwable) {
//                CrashReport crashReport = CrashReport.create(throwable, "Generating mipmaps for frame");
//                CrashReportSection crashReportSection = crashReport.addElement("Frame being iterated");
//                crashReportSection.add("First frame", () -> {
//                    StringBuilder stringBuilder = new StringBuilder();
//                    if (stringBuilder.length() > 0) {
//                        stringBuilder.append(", ");
//                    }
//                    stringBuilder.append(nativeImage.getWidth()).append("x").append(nativeImage.getHeight());
//                    return stringBuilder.toString();
//                });
//                throw new CrashException(crashReport);
                throw new RuntimeException(throwable);
            }
        }
        catch (Throwable throwable2) {
//            CrashReport crashReport2 = CrashReport.create(throwable2, "Applying mipmap");
//            CrashReportSection crashReportSection2 = crashReport2.addElement("Sprite being mipmapped");
//            crashReportSection2.add("Sprite name", () -> this.getId().toString());
//            crashReportSection2.add("Sprite size", () -> this.getWidth() + " x " + this.getHeight());
//            crashReportSection2.add("Sprite frames", () -> this.getFrameCount() + " frames");
//            crashReportSection2.add("Mipmap levels", (Object)Integer.valueOf(maxLevel));
//            throw new CrashException(crashReport2);
            throw new RuntimeException(throwable2);
        }
//        this.interpolation = animationResourceMetadata.shouldInterpolate() ? new Interpolation(info, maxLevel) : null;
    }

//    private void upload(int frame) {
//        int i = this.frameXs[frame] * this.info.getWidth();
//        int j = this.frameYs[frame] * this.info.getHeight();
//        this.upload(i, j, this.images);
//    }
//
//    private void upload(int frameX, int frameY, BufferedImage[] output) {
//        for (int i = 0; i < this.images.length; ++i) {
//            output[i].upload(i, this.x >> i, this.y >> i, frameX >> i, frameY >> i, this.info.getWidth() >> i, this.info.getHeight() >> i, this.images.length > 1, false);
//        }
//    }

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

    public ExpandableAtlas getAtlas() {
        return this.atlas;
    }

    public int getFrameCount() {
        return this.frameXs.length;
    }

//    public void close() {
//        for (BufferedImage nativeImage : this.images) {
//            if (nativeImage == null) continue;
//            nativeImage.close();
//        }
//        if (this.interpolation != null) {
//            this.interpolation.close();
//        }
//    }

    public String toString() {
        int i = this.frameXs.length;
        return "TextureAtlasSprite{name='" + this.info.getId() + '\'' + ", frameCount=" + i + ", x=" + this.x + ", y=" + this.y + ", height=" + this.info.getHeight() + ", width=" + this.info.getWidth() + ", u0=" + this.uMin + ", u1=" + this.uMax + ", v0=" + this.vMin + ", v1=" + this.vMax + '}';
    }

    public boolean isPixelTransparent(int frame, int x, int y) {
        return (this.images[0].getRGB(x + this.frameXs[frame] * this.info.getWidth(), y + this.frameYs[frame] * this.info.getHeight()) >> 24 & 0xFF) == 0;
    }

//    public void upload() {
//        this.upload(0);
//    }

    private float getFrameDeltaFactor() {
        float f = (float)this.info.getWidth() / (this.uMax - this.uMin);
        float g = (float)this.info.getHeight() / (this.vMax - this.vMin);
        return Math.max(g, f);
    }

    public float getAnimationFrameDelta() {
        return 4.0f / this.getFrameDeltaFactor();
    }

//    public void tickAnimation() {
//        ++this.frameTicks;
//        if (this.frameTicks >= this.animationMetadata.getFrameTime(this.frameIndex)) {
//            int i = this.animationMetadata.getFrameIndex(this.frameIndex);
//            int j = this.animationMetadata.getFrameCount() == 0 ? this.getFrameCount() : this.animationMetadata.getFrameCount();
//            this.frameIndex = (this.frameIndex + 1) % j;
//            this.frameTicks = 0;
//            int k = this.animationMetadata.getFrameIndex(this.frameIndex);
//            if (i != k && k >= 0 && k < this.getFrameCount()) {
//                this.upload(k);
//            }
//        } else if (this.interpolation != null) {
//            if (!RenderSystem.isOnRenderThread()) {
//                RenderSystem.recordRenderCall(() -> this.interpolation.apply());
//            } else {
//                this.interpolation.apply();
//            }
//        }
//    }

    public boolean isAnimated() {
        return this.animationMetadata.getFrameCount() > 1;
    }

//    public VertexConsumer getTextureSpecificVertexConsumer(VertexConsumer vertexConsumer) {
//        return new SpriteTexturedVertexConsumer(vertexConsumer, this);
//    }
//
//    @Environment(value=EnvType.CLIENT)
//    final class Interpolation
//    implements AutoCloseable {
//        private final NativeImage[] images;
//
//        private Interpolation(Info info, int mipmap) {
//            this.images = new NativeImage[mipmap + 1];
//            for (int i = 0; i < this.images.length; ++i) {
//                int j = info.width >> i;
//                int k = info.height >> i;
//                if (this.images[i] != null) continue;
//                this.images[i] = new NativeImage(j, k, false);
//            }
//        }
//
//        private void apply() {
//            double d = 1.0 - (double)Sprite.this.frameTicks / (double)Sprite.this.animationMetadata.getFrameTime(Sprite.this.frameIndex);
//            int i = Sprite.this.animationMetadata.getFrameIndex(Sprite.this.frameIndex);
//            int j = Sprite.this.animationMetadata.getFrameCount() == 0 ? Sprite.this.getFrameCount() : Sprite.this.animationMetadata.getFrameCount();
//            int k = Sprite.this.animationMetadata.getFrameIndex((Sprite.this.frameIndex + 1) % j);
//            if (i != k && k >= 0 && k < Sprite.this.getFrameCount()) {
//                for (int l = 0; l < this.images.length; ++l) {
//                    int m = Sprite.this.info.width >> l;
//                    int n = Sprite.this.info.height >> l;
//                    for (int o = 0; o < n; ++o) {
//                        for (int p = 0; p < m; ++p) {
//                            int q = this.getPixelColor(i, l, p, o);
//                            int r = this.getPixelColor(k, l, p, o);
//                            int s = this.lerp(d, q >> 16 & 0xFF, r >> 16 & 0xFF);
//                            int t = this.lerp(d, q >> 8 & 0xFF, r >> 8 & 0xFF);
//                            int u = this.lerp(d, q & 0xFF, r & 0xFF);
//                            this.images[l].setPixelColor(p, o, q & 0xFF000000 | s << 16 | t << 8 | u);
//                        }
//                    }
//                }
//                Sprite.this.upload(0, 0, this.images);
//            }
//        }
//
//        private int getPixelColor(int frameIndex, int layer, int x, int y) {
//            return Sprite.this.images[layer].getPixelColor(x + (Sprite.this.frameXs[frameIndex] * Sprite.this.info.width >> layer), y + (Sprite.this.frameYs[frameIndex] * Sprite.this.info.height >> layer));
//        }
//
//        private int lerp(double delta, int to, int from) {
//            return (int)(delta * (double)to + (1.0 - delta) * (double)from);
//        }
//
//        public void close() {
//            for (NativeImage nativeImage : this.images) {
//                if (nativeImage == null) continue;
//                nativeImage.close();
//            }
//        }
//    }
}
