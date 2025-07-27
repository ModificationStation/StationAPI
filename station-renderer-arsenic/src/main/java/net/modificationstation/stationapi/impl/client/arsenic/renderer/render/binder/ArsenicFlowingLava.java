package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder;

import net.minecraft.client.resource.pack.TexturePack;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class ArsenicFlowingLava extends StationTextureBinder {

    protected float[] current;
    protected float[] next;
    protected float[] heat;
    protected float[] heatDelta;
    int ticks = 0;

    public ArsenicFlowingLava(Atlas.Sprite staticReference) {
        super(staticReference);
        replicate = 2;
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int square = (getStaticReference().getWidth() / replicate) * (getStaticReference().getHeight() / replicate);
        current = new float[square];
        next = new float[square];
        heat = new float[square];
        heatDelta = new float[square];
        pixels = new byte[square * 4];
    }

    @Override
    public void tick() {
        int
                textureWidth = getStaticReference().getWidth() / replicate,
                textureHeight = getStaticReference().getHeight() / replicate;
        ++this.ticks;

        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight; ++var2) {
                float var3 = 0.0F;
                int var4 = (int)(MathHelper.sin((float)var2 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);
                int var5 = (int)(MathHelper.sin((float)var1 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);

                for(int var6 = var1 - 1; var6 <= var1 + 1; ++var6) {
                    for(int var7 = var2 - 1; var7 <= var2 + 1; ++var7) {
                        int var8 = var6 + var4 & (textureHeight - 1);
                        int var9 = var7 + var5 & (textureWidth - 1);
                        var3 += this.current[var8 + var9 * textureWidth];
                    }
                }

                this.next[var1 + var2 * textureWidth] = var3 / 10.0F + (
                        this.heat[(var1 & (textureWidth - 1)) + (var2 & (textureHeight - 1)) * textureWidth] +
                                this.heat[(var1 + 1 & (textureWidth - 1)) + (var2 & (textureHeight - 1)) * textureWidth] +
                                this.heat[(var1 + 1 & (textureWidth - 1)) + (var2 + 1 & (textureHeight - 1)) * textureWidth] +
                                this.heat[(var1 & (textureWidth - 1)) + (var2 + 1 & (textureHeight - 1)) * textureWidth]) / 4.0F * 0.8F;
                this.heat[var1 + var2 * textureWidth] += this.heatDelta[var1 + var2 * textureWidth] * 0.01F;
                if (this.heat[var1 + var2 * textureWidth] < 0.0F) {
                    this.heat[var1 + var2 * textureWidth] = 0.0F;
                }

                this.heatDelta[var1 + var2 * textureWidth] -= 0.06F;
                if (Math.random() < 0.005D) {
                    this.heatDelta[var1 + var2 * textureWidth] = 1.5F;
                }
            }
        }

        float[] var11 = this.next;
        this.next = this.current;
        this.current = var11;

        for(int var12 = 0; var12 < textureWidth * textureHeight; ++var12) {
            float var13 = this.current[var12 - this.ticks / 3 * textureWidth & (textureWidth * textureHeight - 1)] * 2.0F;
            if (var13 > 1.0F) {
                var13 = 1.0F;
            }

            if (var13 < 0.0F) {
                var13 = 0.0F;
            }

            int var14 = (int)(var13 * 100.0F + 155.0F);
            int var15 = (int)(var13 * var13 * 255.0F);
            int var16 = (int)(var13 * var13 * var13 * var13 * 128.0F);
            if (this.anaglyph) {
                int var17 = (var14 * 30 + var15 * 59 + var16 * 11) / 100;
                int var18 = (var14 * 30 + var15 * 70) / 100;
                int var10 = (var14 * 30 + var16 * 70) / 100;
                var14 = var17;
                var15 = var18;
                var16 = var10;
            }

            this.pixels[var12 * 4] = (byte)var14;
            this.pixels[var12 * 4 + 1] = (byte)var15;
            this.pixels[var12 * 4 + 2] = (byte)var16;
            this.pixels[var12 * 4 + 3] = -1;
        }
    }
}
