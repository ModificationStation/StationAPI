package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class StationStillWaterTextureBinder extends StationTextureBinder {

    protected float[] field_2566;
    protected float[] field_2567;
    protected float[] field_2568;
    protected float[] field_2569;

    public StationStillWaterTextureBinder(Atlas.Sprite staticReference) {
        super(staticReference);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int square = getStaticReference().getWidth() * getStaticReference().getHeight();
        field_2566 = new float[square];
        field_2567 = new float[square];
        field_2568 = new float[square];
        field_2569 = new float[square];
        grid = new byte[square * 4];
    }

    @Override
    public void update() {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight; ++var2) {
                float var3 = 0.0F;

                for(int var4 = var1 - 1; var4 <= var1 + 1; ++var4) {
                    int var5 = var4 & (textureWidth - 1);
                    int var6 = var2 & (textureHeight - 1);
                    var3 += this.field_2566[var5 + var6 * textureWidth];
                }

                this.field_2567[var1 + var2 * textureWidth] = var3 / 3.3F + this.field_2568[var1 + var2 * textureWidth] * 0.8F;
            }
        }

        for(int var12 = 0; var12 < textureWidth; ++var12) {
            for(int var14 = 0; var14 < textureHeight; ++var14) {
                this.field_2568[var12 + var14 * textureWidth] += this.field_2569[var12 + var14 * textureWidth] * 0.05F;
                if (this.field_2568[var12 + var14 * textureWidth] < 0.0F) {
                    this.field_2568[var12 + var14 * textureWidth] = 0.0F;
                }

                this.field_2569[var12 + var14 * textureWidth] -= 0.1F;
                if (Math.random() < 0.05D) {
                    this.field_2569[var12 + var14 * textureWidth] = 0.5F;
                }
            }
        }

        float[] var13 = this.field_2567;
        this.field_2567 = this.field_2566;
        this.field_2566 = var13;

        for(int var15 = 0; var15 < textureWidth * textureHeight; ++var15) {
            float var16 = this.field_2566[var15];
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
