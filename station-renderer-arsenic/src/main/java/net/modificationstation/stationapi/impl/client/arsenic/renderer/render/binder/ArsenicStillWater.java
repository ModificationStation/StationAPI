package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder;

import net.minecraft.client.resource.pack.TexturePack;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class ArsenicStillWater extends StationTextureBinder {

    protected float[] current;
    protected float[] next;
    protected float[] heat;
    protected float[] heatDelta;

    public ArsenicStillWater(Atlas.Sprite staticReference) {
        super(staticReference);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int square = getStaticReference().getWidth() * getStaticReference().getHeight();
        current = new float[square];
        next = new float[square];
        heat = new float[square];
        heatDelta = new float[square];
        pixels = new byte[square * 4];
    }

    @Override
    public void tick() {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight; ++var2) {
                float var3 = 0.0F;

                for(int var4 = var1 - 1; var4 <= var1 + 1; ++var4) {
                    int var5 = var4 & (textureWidth - 1);
                    int var6 = var2 & (textureHeight - 1);
                    var3 += this.current[var5 + var6 * textureWidth];
                }

                this.next[var1 + var2 * textureWidth] = var3 / 3.3F + this.heat[var1 + var2 * textureWidth] * 0.8F;
            }
        }

        for(int var12 = 0; var12 < textureWidth; ++var12) {
            for(int var14 = 0; var14 < textureHeight; ++var14) {
                this.heat[var12 + var14 * textureWidth] += this.heatDelta[var12 + var14 * textureWidth] * 0.05F;
                if (this.heat[var12 + var14 * textureWidth] < 0.0F) {
                    this.heat[var12 + var14 * textureWidth] = 0.0F;
                }

                this.heatDelta[var12 + var14 * textureWidth] -= 0.1F;
                if (Math.random() < 0.05D) {
                    this.heatDelta[var12 + var14 * textureWidth] = 0.5F;
                }
            }
        }

        float[] var13 = this.next;
        this.next = this.current;
        this.current = var13;

        for(int var15 = 0; var15 < textureWidth * textureHeight; ++var15) {
            float var16 = this.current[var15];
            if (var16 > 1.0F) {
                var16 = 1.0F;
            }

            if (var16 < 0.0F) {
                var16 = 0.0F;
            }

            float var17 = var16 * var16;
            int var18 = (int)(32.0F + var17 * 32.0F);
            int var19 = (int)(50.0F + var17 * 64.0F);
            int var7 = 255;
            int var8 = (int)(146.0F + var17 * 50.0F);
            if (this.anaglyph) {
                int var9 = (var18 * 30 + var19 * 59 + var7 * 11) / 100;
                int var10 = (var18 * 30 + var19 * 70) / 100;
                int var11 = (var18 * 30 + var7 * 70) / 100;
                var18 = var9;
                var19 = var10;
                var7 = var11;
            }

            this.pixels[var15 * 4] = (byte)var18;
            this.pixels[var15 * 4 + 1] = (byte)var19;
            this.pixels[var15 * 4 + 2] = (byte)var7;
            this.pixels[var15 * 4 + 3] = (byte)var8;
        }
    }
}
