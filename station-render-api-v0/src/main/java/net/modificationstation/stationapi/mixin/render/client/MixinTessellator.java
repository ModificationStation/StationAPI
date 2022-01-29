package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.impl.client.texture.FastTessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Tessellator.class)
public abstract class MixinTessellator implements FastTessellator {

    @Unique
    private final int[] fastVertexData = new int[32];

    @Shadow private int[] bufferArray;

    @Shadow private int field_2068;

    @Shadow private int vertexAmount;

    @Shadow private int vertexCount;

    @Shadow private boolean hasTexture;

    @Shadow private double xOffset;

    @Shadow private double yOffset;

    @Shadow private double zOffset;

    @Shadow private boolean hasColour;

    @Override
    @Unique
    public void quad(int[] vertexData, float x, float y, float z, int[] colour) {
        System.arraycopy(vertexData, 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + xOffset));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + yOffset));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + zOffset));
        fastVertexData[5] = colour[0];
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + xOffset));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + yOffset));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + zOffset));
        fastVertexData[13] = colour[1];
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + xOffset));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + yOffset));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + zOffset));
        fastVertexData[21] = colour[2];
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + xOffset));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + yOffset));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + zOffset));
        fastVertexData[29] = colour[3];
        hasColour = true;
        hasTexture = true;
        System.arraycopy(fastVertexData, 0, bufferArray, field_2068, 24);
        System.arraycopy(fastVertexData, 0, bufferArray, field_2068 + 24, 8);
        System.arraycopy(fastVertexData, 16, bufferArray, field_2068 + 32, 16);
        vertexAmount += 4;
        field_2068 += 48;
        vertexCount += 6;
    }
}
