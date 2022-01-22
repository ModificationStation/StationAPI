package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.block.BlockBase;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class StationFlowingWaterTextureBinder extends StationTextureBinder {

    protected float[] field_2118;
    protected float[] field_2119;
    protected float[] field_2120;
    protected float[] field_2121;
    private int field_2122 = 0;

    public StationFlowingWaterTextureBinder() {
        super(Atlases.getTerrain().getTexture(BlockBase.FLOWING_WATER.texture + 1));
        textureSize = 2;
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int square = (getStaticReference().getWidth() / textureSize) * (getStaticReference().getHeight() / textureSize);
        field_2118 = new float[square];
        field_2119 = new float[square];
        field_2120 = new float[square];
        field_2121 = new float[square];
        grid = new byte[square * 4];
    }

    public void update() {
        int
                textureWidth = getStaticReference().getWidth() / textureSize,
                textureHeight = getStaticReference().getHeight() / textureSize;
        ++this.field_2122;

        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight; ++var2) {
                float var3 = 0.0F;

                for(int var4 = var2 - 2; var4 <= var2; ++var4) {
                    int var5 = var1 & (textureWidth - 1);
                    int var6 = var4 & (textureHeight - 1);
                    var3 += this.field_2118[var5 + var6 * textureWidth];
                }

                this.field_2119[var1 + var2 * textureWidth] = var3 / 3.2F + this.field_2120[var1 + var2 * textureWidth] * 0.8F;
            }
        }

        for(int var12 = 0; var12 < textureWidth; ++var12) {
            for(int var14 = 0; var14 < textureHeight; ++var14) {
                this.field_2120[var12 + var14 * textureWidth] += this.field_2121[var12 + var14 * textureWidth] * 0.05F;
                if (this.field_2120[var12 + var14 * textureWidth] < 0.0F) {
                    this.field_2120[var12 + var14 * textureWidth] = 0.0F;
                }

                this.field_2121[var12 + var14 * textureWidth] -= 0.3F;
                if (Math.random() < 0.2D) {
                    this.field_2121[var12 + var14 * textureWidth] = 0.5F;
                }
            }
        }

        float[] var13 = this.field_2119;
        this.field_2119 = this.field_2118;
        this.field_2118 = var13;

        for(int var15 = 0; var15 < textureWidth * textureHeight; ++var15) {
            float var16 = this.field_2118[var15 - this.field_2122 * textureWidth & (textureWidth * textureHeight - 1)];
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
            if (this.render3d) {
                int var9 = (var18 * 30 + var19 * 59 + var7 * 11) / 100;
                int var10 = (var18 * 30 + var19 * 70) / 100;
                int var11 = (var18 * 30 + var7 * 70) / 100;
                var18 = var9;
                var19 = var10;
                var7 = var11;
            }

            this.grid[var15 * 4] = (byte)var18;
            this.grid[var15 * 4 + 1] = (byte)var19;
            this.grid[var15 * 4 + 2] = (byte)var7;
            this.grid[var15 * 4 + 3] = (byte)var8;
        }
    }
}
