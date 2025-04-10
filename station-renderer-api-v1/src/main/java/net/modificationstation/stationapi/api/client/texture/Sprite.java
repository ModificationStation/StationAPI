package net.modificationstation.stationapi.api.client.texture;

import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class Sprite {
    private final Identifier atlasId;
    private final SpriteContents contents;
    final int x;
    final int y;
    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;

    protected Sprite(Identifier atlasId, SpriteContents contents, int atlasWidth, int atlasHeight, int x, int y) {
        this.atlasId = atlasId;
        this.contents = contents;
        this.x = x;
        this.y = y;
        this.minU = (float)x / (float)atlasWidth;
        this.maxU = (float)(x + contents.getWidth()) / (float)atlasWidth;
        this.minV = (float)y / (float)atlasHeight;
        this.maxV = (float)(y + contents.getHeight()) / (float)atlasHeight;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getMinU() {
        return this.minU;
    }

    public float getMaxU() {
        return this.maxU;
    }

    public SpriteContents getContents() {
        return this.contents;
    }

    @Nullable
    public TickableAnimation createAnimation() {
        final Animator animator = this.contents.createAnimator();
        if (animator != null) {
            return new TickableAnimation(){
                @Override
                public void tick() {
                    animator.tick(Sprite.this.x, Sprite.this.y);
                }

                @Override
                public void close() {
                    animator.close();
                }
            };
        }
        return null;
    }

    public float getFrameU(double frame) {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)frame / 16.0f;
    }

    public float getMinV() {
        return this.minV;
    }

    public float getMaxV() {
        return this.maxV;
    }

    public float getFrameV(double frame) {
        float f = this.maxV - this.minV;
        return this.minV + f * (float)frame / 16.0f;
    }

    public Identifier getAtlasId() {
        return this.atlasId;
    }

    public String toString() {
        return "TextureAtlasSprite{contents='" + this.contents + "', u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + "}";
    }

    public void upload() {
        this.contents.upload(this.x, this.y);
    }

    private float getFrameDeltaFactor() {
        float f = (float)this.contents.getWidth() / (this.maxU - this.minU);
        float g = (float)this.contents.getHeight() / (this.maxV - this.minV);
        return Math.max(g, f);
    }

    public float getAnimationFrameDelta() {
        return 4.0f / this.getFrameDeltaFactor();
    }

    public interface TickableAnimation extends AutoCloseable {
        void tick();

        @Override
        void close();
    }
}

