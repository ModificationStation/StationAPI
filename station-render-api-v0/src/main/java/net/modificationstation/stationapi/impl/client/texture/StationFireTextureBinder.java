package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.texture.atlas.SquareAtlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;
import net.modificationstation.stationapi.api.client.texture.binder.TexturePackDependent;

public class StationFireTextureBinder extends StationTextureBinder implements TexturePackDependent {
    protected float[] currentFireFrame;
    protected float[] lastFireFrame;

    public StationFireTextureBinder(int line) {
        super(SquareAtlas.TERRAIN.getTexture(BlockBase.FIRE.texture + 16 * line));
        refreshTextures();
    }

    @Override
    public void refreshTextures() {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        currentFireFrame = new float[textureWidth * ((int) (textureHeight * 1.25))];
        lastFireFrame = new float[textureWidth * ((int) (textureHeight * 1.25))];
        grid = new byte[textureWidth * textureHeight * 4];
    }

    @Override
    public void update() {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        for(int var1 = 0; var1 < textureWidth; ++var1) {
            for(int var2 = 0; var2 < textureHeight * 1.25; ++var2) {
                int var3 = (int) (textureHeight * 1.125);
                float var4 = this.currentFireFrame[(var1 + (var2 + 1) % ((int) (textureHeight * 1.25)) * textureWidth)] * (float)var3;

                for(int var5 = var1 - 1; var5 <= var1 + 1; ++var5) {
                    for(int var6 = var2; var6 <= var2 + 1; ++var6) {
                        if (var5 >= 0 && var6 >= 0 && var5 < textureWidth && var6 < textureHeight * 1.25) {
                            var4 += this.currentFireFrame[var5 + var6 * textureWidth];
                        }

                        ++var3;
                    }
                }

                this.lastFireFrame[var1 + var2 * textureWidth] = var4 / ((float)var3 * (1 + 0.96F / textureHeight));
                if (var2 >= textureHeight * 1.1875) {
                    this.lastFireFrame[var1 + var2 * textureWidth] = (float)(Math.random() * Math.random() * Math.random() * 4.0D + Math.random() * (double)0.1F + (double)0.2F);
                }
            }
        }

        float[] var12 = this.lastFireFrame;
        this.lastFireFrame = this.currentFireFrame;
        this.currentFireFrame = var12;

        for(int var13 = 0; var13 < textureWidth * textureHeight; ++var13) {
            float var14 = this.currentFireFrame[var13] * 1.8F;
            if (var14 > 1.0F) {
                var14 = 1.0F;
            }

            if (var14 < 0.0F) {
                var14 = 0.0F;
            }

            int var16 = (int)(var14 * 155.0F + 100.0F);
            int var17 = (int)(var14 * var14 * 255.0F);
            int var7 = (int)(var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * var14 * 255.0F);
            short var8 = 255;
            if (var14 < 0.5F) {
                var8 = 0;
            }

            if (this.render3d) {
                int var9 = (var16 * 30 + var17 * 59 + var7 * 11) / 100;
                int var10 = (var16 * 30 + var17 * 70) / 100;
                int var11 = (var16 * 30 + var7 * 70) / 100;
                var16 = var9;
                var17 = var10;
                var7 = var11;
            }

            this.grid[var13 * 4] = (byte)var16;
            this.grid[var13 * 4 + 1] = (byte)var17;
            this.grid[var13 * 4 + 2] = (byte)var7;
            this.grid[var13 * 4 + 3] = (byte)var8;
        }

    }
}
