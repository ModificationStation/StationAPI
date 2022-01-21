package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.block.BlockBase;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class StationFlowingLavaTextureBinder extends StationTextureBinder {

    protected float[] field_1166;
    protected float[] field_1167;
    protected float[] field_1168;
    protected float[] field_1169;
    int field_1170 = 0;

    public StationFlowingLavaTextureBinder() {
        super(Atlases.getTerrain().getTexture(BlockBase.FLOWING_LAVA.texture + 1));
        textureSize = 2;
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int square = getStaticReference().getWidth() * getStaticReference().getHeight();
        field_1166 = new float[square];
        field_1167 = new float[square];
        field_1168 = new float[square];
        field_1169 = new float[square];
        grid = new byte[square * 4];
    }

    @Override
    public void update() {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        ++this.field_1170;

        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight; ++var2) {
                float var3 = 0.0F;
                int var4 = (int)(MathHelper.sin((float)var2 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);
                int var5 = (int)(MathHelper.sin((float)var1 * (float)Math.PI * 2.0F / 16.0F) * 1.2F);

                for(int var6 = var1 - 1; var6 <= var1 + 1; ++var6) {
                    for(int var7 = var2 - 1; var7 <= var2 + 1; ++var7) {
                        int var8 = var6 + var4 & (textureHeight - 1);
                        int var9 = var7 + var5 & (textureWidth - 1);
                        var3 += this.field_1166[var8 + var9 * textureWidth];
                    }
                }

                this.field_1167[var1 + var2 * textureWidth] = var3 / 10.0F + (
                        this.field_1168[(var1 & (textureWidth - 1)) + (var2 & (textureHeight - 1)) * textureWidth] +
                                this.field_1168[(var1 + 1 & (textureWidth - 1)) + (var2 & (textureHeight - 1)) * textureWidth] +
                                this.field_1168[(var1 + 1 & (textureWidth - 1)) + (var2 + 1 & (textureHeight - 1)) * textureWidth] +
                                this.field_1168[(var1 & (textureWidth - 1)) + (var2 + 1 & (textureHeight - 1)) * textureWidth]) / 4.0F * 0.8F;
                this.field_1168[var1 + var2 * textureWidth] += this.field_1169[var1 + var2 * textureWidth] * 0.01F;
                if (this.field_1168[var1 + var2 * textureWidth] < 0.0F) {
                    this.field_1168[var1 + var2 * textureWidth] = 0.0F;
                }

                this.field_1169[var1 + var2 * textureWidth] -= 0.06F;
                if (Math.random() < 0.005D) {
                    this.field_1169[var1 + var2 * textureWidth] = 1.5F;
                }
            }
        }

        float[] var11 = this.field_1167;
        this.field_1167 = this.field_1166;
        this.field_1166 = var11;

        for(int var12 = 0; var12 < textureWidth * textureHeight; ++var12) {
            float var13 = this.field_1166[var12 - this.field_1170 / 3 * textureWidth & (textureWidth * textureHeight - 1)] * 2.0F;
            if (var13 > 1.0F) {
                var13 = 1.0F;
            }

            if (var13 < 0.0F) {
                var13 = 0.0F;
            }

            int var14 = (int)(var13 * 100.0F + 155.0F);
            int var15 = (int)(var13 * var13 * 255.0F);
            int var16 = (int)(var13 * var13 * var13 * var13 * 128.0F);
            if (this.render3d) {
                int var17 = (var14 * 30 + var15 * 59 + var16 * 11) / 100;
                int var18 = (var14 * 30 + var15 * 70) / 100;
                int var10 = (var14 * 30 + var16 * 70) / 100;
                var14 = var17;
                var15 = var18;
                var16 = var10;
            }

            this.grid[var12 * 4] = (byte)var14;
            this.grid[var12 * 4 + 1] = (byte)var15;
            this.grid[var12 * 4 + 2] = (byte)var16;
            this.grid[var12 * 4 + 3] = -1;
        }
    }
}
