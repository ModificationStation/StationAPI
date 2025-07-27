package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.util.math.MathHelper;

public class FogRendererImpl {
    private static final float[] FOG_COLOR = new float[3];

    public static void setupFog(Minecraft minecraft, float delta) {
        BiomeSource biomeSource = minecraft.world.method_1781();
        double x = minecraft.camera.x;
        double z = minecraft.camera.z;
        int color = BiomeColorsImpl.FOG_INTERPOLATOR.getColor(biomeSource, x, z);

        FOG_COLOR[0] = ((color >> 16) & 255) / 255F;
        FOG_COLOR[1] = ((color >> 8) & 255) / 255F;
        FOG_COLOR[2] = (color & 255) / 255F;

        if (minecraft.world.dimension.hasCeiling) return;

        delta = minecraft.world.getTime(delta);
        delta = net.minecraft.util.math.MathHelper.cos(delta * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        delta = MathHelper.clamp(delta, 0.0F, 1.0F);

        FOG_COLOR[0] = MathHelper.lerp(delta, 0.06F, FOG_COLOR[0]);
        FOG_COLOR[1] = MathHelper.lerp(delta, 0.06F, FOG_COLOR[1]);
        FOG_COLOR[2] = MathHelper.lerp(delta, 0.09F, FOG_COLOR[2]);
    }

    public static float getR() {
        return FOG_COLOR[0];
    }

    public static float getG() {
        return FOG_COLOR[1];
    }

    public static float getB() {
        return FOG_COLOR[2];
    }
}
