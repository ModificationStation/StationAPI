package net.modificationstation.stationapi.impl.client.texture;

import net.minecraft.block.BlockBase;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

import java.util.*;

public class StationPortalTextureBinder extends StationTextureBinder {
    private int updatesRan = 0;
    private final byte[][] texture = new byte[32][];

    public StationPortalTextureBinder() {
        super(Atlases.getTerrain().getTexture(BlockBase.PORTAL.texture));
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        int
                textureWidth = getStaticReference().getWidth(),
                textureHeight = getStaticReference().getHeight();
        Random var1 = new Random(100L);

        grid = new byte[textureWidth * textureHeight * 4];

        for(int var2 = 0; var2 < 32; ++var2) {
            texture[var2] = new byte[textureWidth * textureHeight * 4];
            for(int var3 = 0; var3 < textureWidth; ++var3) {
                for(int var4 = 0; var4 < textureHeight; ++var4) {
                    float var5 = 0.0F;

                    for(int var6 = 0; var6 < 2; ++var6) {
                        float var7 = (float)(var6 * textureWidth / 2);
                        float var8 = (float)(var6 * textureHeight / 2);
                        float var9 = ((float)var3 - var7) / textureWidth * 2.0F;
                        float var10 = ((float)var4 - var8) / textureHeight * 2.0F;
                        if (var9 < -1.0F) {
                            var9 += 2.0F;
                        }

                        if (var9 >= 1.0F) {
                            var9 -= 2.0F;
                        }

                        if (var10 < -1.0F) {
                            var10 += 2.0F;
                        }

                        if (var10 >= 1.0F) {
                            var10 -= 2.0F;
                        }

                        float var11 = var9 * var9 + var10 * var10;
                        float var12 = (float)Math.atan2(var10, var9) + ((float)var2 / 32.0F * (float)Math.PI * 2.0F - var11 * 10.0F + (float)(var6 * 2)) * (float)(var6 * 2 - 1);
                        var12 = (MathHelper.sin(var12) + 1.0F) / 2.0F;
                        var12 = var12 / (var11 + 1.0F);
                        var5 += var12 * 0.5F;
                    }

                    var5 = var5 + var1.nextFloat() * 0.1F;
                    int var14 = (int)(var5 * 100.0F + 155.0F);
                    int var15 = (int)(var5 * var5 * 200.0F + 55.0F);
                    int var16 = (int)(var5 * var5 * var5 * var5 * 255.0F);
                    int var17 = (int)(var5 * 100.0F + 155.0F);
                    int var18 = var4 * textureWidth + var3;
                    this.texture[var2][var18 * 4] = (byte)var15;
                    this.texture[var2][var18 * 4 + 1] = (byte)var16;
                    this.texture[var2][var18 * 4 + 2] = (byte)var14;
                    this.texture[var2][var18 * 4 + 3] = (byte)var17;
                }
            }
        }
    }

    @Override
    public void update() {
        ++this.updatesRan;
        byte[] var1 = this.texture[this.updatesRan & 31];

        for(int var2 = 0; var2 < getStaticReference().getWidth() * getStaticReference().getHeight(); ++var2) {
            int var3 = var1[var2 * 4] & 255;
            int var4 = var1[var2 * 4 + 1] & 255;
            int var5 = var1[var2 * 4 + 2] & 255;
            int var6 = var1[var2 * 4 + 3] & 255;
            if (this.render3d) {
                int var7 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var8 = (var3 * 30 + var4 * 70) / 100;
                int var9 = (var3 * 30 + var5 * 70) / 100;
                var3 = var7;
                var4 = var8;
                var5 = var9;
            }

            this.grid[var2 * 4] = (byte)var3;
            this.grid[var2 * 4 + 1] = (byte)var4;
            this.grid[var2 * 4 + 2] = (byte)var5;
            this.grid[var2 * 4 + 3] = (byte)var6;
        }
    }
}
