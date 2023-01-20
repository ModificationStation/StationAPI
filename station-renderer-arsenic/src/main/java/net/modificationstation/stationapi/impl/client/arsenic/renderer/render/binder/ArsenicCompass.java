package net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.util.Vec3i;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StationTextureBinder;

public class ArsenicCompass extends StationTextureBinder {

    @SuppressWarnings("deprecation")
    private final Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
    private int[] compassTexture;
    private double currentRotation;
    private double rotationDelay;

    public ArsenicCompass(Atlas.Sprite staticReference) {
        super(staticReference);
    }

    @Override
    public void reloadFromTexturePack(TexturePack newTexturePack) {
        Atlas.Sprite staticReference = getStaticReference();
        int
                textureWidth = staticReference.getWidth(),
                textureHeight = staticReference.getHeight(),
                square = textureWidth * textureHeight;
        compassTexture = staticReference.getSprite().getBaseFrame().makePixelArray();
        grid = new byte[square * 4];
    }

    @Override
    public void update() {
        Atlas.Sprite staticReference = getStaticReference();
        int
                textureWidth = staticReference.getWidth(),
                textureHeight = staticReference.getHeight();
        for(int var1 = 0; var1 < textureWidth * textureHeight; ++var1) {
            int var2 = (this.compassTexture[var1] >> 24) & 255;
            int var3 = (this.compassTexture[var1] >> 16) & 255;
            int var4 = (this.compassTexture[var1] >> 8) & 255;
            int var5 = this.compassTexture[var1] & 255;
            if (this.render3d) {
                int var6 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
                int var7 = (var3 * 30 + var4 * 70) / 100;
                int var8 = (var3 * 30 + var5 * 70) / 100;
                var3 = var6;
                var4 = var7;
                var5 = var8;
            }

            this.grid[var1 * 4] = (byte)var3;
            this.grid[var1 * 4 + 1] = (byte)var4;
            this.grid[var1 * 4 + 2] = (byte)var5;
            this.grid[var1 * 4 + 3] = (byte)var2;
        }

        double var20 = 0.0D;
        if (this.minecraft.level != null && this.minecraft.player != null) {
            Vec3i var21 = this.minecraft.level.getSpawnPosition();
            double var23 = (double)var21.x - this.minecraft.player.x;
            double var25 = (double)var21.z - this.minecraft.player.z;
            var20 = (double)(this.minecraft.player.yaw - 90.0F) * Math.PI / 180.0D - Math.atan2(var25, var23);
            if (this.minecraft.level.dimension.blocksCompassAndClock) {
                var20 = Math.random() * (double)(float)Math.PI * 2.0D;
            }
        }

        double var22 = var20 - this.currentRotation;
        while (var22 < -Math.PI) {
            var22 += (Math.PI * 2D);
        }

        while(var22 >= Math.PI) {
            var22 -= (Math.PI * 2D);
        }

        if (var22 < -1.0D) {
            var22 = -1.0D;
        }

        if (var22 > 1.0D) {
            var22 = 1.0D;
        }

        this.rotationDelay += var22 * 0.1D;
        this.rotationDelay *= 0.8D;
        this.currentRotation += this.rotationDelay;
        double var24 = Math.sin(this.currentRotation);
        double var26 = Math.cos(this.currentRotation);

        for(int var9 = -textureHeight / 4; var9 <= textureHeight / 4; ++var9) {
            int var10 = (int)(0.53125D * textureWidth + var26 * (double)var9 * 0.3D);
            int var11 = (int)(0.46875D * textureHeight - var24 * (double)var9 * 0.3D * 0.5D);
            int var12 = var11 * textureWidth + var10;
            int var13 = 100;
            int var14 = 100;
            int var15 = 100;
            short var16 = 255;
            if (this.render3d) {
                int var17 = (var13 * 30 + var14 * 59 + var15 * 11) / 100;
                int var18 = (var13 * 30 + var14 * 70) / 100;
                int var19 = (var13 * 30 + var15 * 70) / 100;
                var13 = var17;
                var14 = var18;
                var15 = var19;
            }

            this.grid[var12 * 4] = (byte)var13;
            this.grid[var12 * 4 + 1] = (byte)var14;
            this.grid[var12 * 4 + 2] = (byte)var15;
            this.grid[var12 * 4 + 3] = (byte)var16;
        }

        for(int var27 = -textureWidth / 2; var27 <= textureWidth; ++var27) {
            int var28 = (int)(0.53125D * textureHeight + var24 * (double)var27 * 0.3D);
            int var29 = (int)(0.46875D * textureWidth + var26 * (double)var27 * 0.3D * 0.5D);
            int var30 = var29 * textureHeight + var28;
            int var31 = var27 >= 0 ? 255 : 100;
            int var32 = var27 >= 0 ? 20 : 100;
            int var33 = var27 >= 0 ? 20 : 100;
            short var34 = 255;
            if (this.render3d) {
                int var35 = (var31 * 30 + var32 * 59 + var33 * 11) / 100;
                int var36 = (var31 * 30 + var32 * 70) / 100;
                int var37 = (var31 * 30 + var33 * 70) / 100;
                var31 = var35;
                var32 = var36;
                var33 = var37;
            }

            this.grid[var30 * 4] = (byte)var31;
            this.grid[var30 * 4 + 1] = (byte)var32;
            this.grid[var30 * 4 + 2] = (byte)var33;
            this.grid[var30 * 4 + 3] = (byte)var34;
        }
    }
}
