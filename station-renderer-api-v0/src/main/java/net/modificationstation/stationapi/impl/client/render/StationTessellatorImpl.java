package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.GlAllocationUtils;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.api.util.math.Vec4f;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class StationTessellatorImpl implements StationTessellator {

    // Hard ceiling on the int-buffer size so it can never grow large enough for
    // bufferSize * 4 (the byte-buffer allocation) to overflow a signed int.
    // 2^28 ints = 1 GiB direct buffer, far beyond anything vanilla rendering needs;
    // the buffer base size is 2^21 ints, so this still allows several doublings.
    private static final int MAX_BUFFER_SIZE = 1 << 28;

    private final Tessellator self;
    private final TessellatorAccessor access;
    private final int[] fastVertexData = new int[32];
    private final Vec4f damageUV = new Vec4f();

    public StationTessellatorImpl(Tessellator tessellator) {
        self = tessellator;
        access = (TessellatorAccessor) tessellator;
    }

    @Override
    public void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        byte by = (byte)(normalX * 128.0f);
        byte by2 = (byte)(normalY * 127.0f);
        byte by3 = (byte)(normalZ * 127.0f);
        int normal = by | by2 << 8 | by3 << 16;
        System.arraycopy(quad.getVertexData(), 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + access.getXOffset()));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + access.getYOffset()));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + access.getZOffset()));
        fastVertexData[6] = normal;
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + access.getXOffset()));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + access.getYOffset()));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + access.getZOffset()));
        fastVertexData[14] = normal;
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + access.getXOffset()));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + access.getYOffset()));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + access.getZOffset()));
        fastVertexData[22] = normal;
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + access.getXOffset()));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + access.getYOffset()));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + access.getZOffset()));
        fastVertexData[30] = normal;
        if (spreadUV) {
            Direction facing = quad.getFace();
            Matrix4f texture = Matrix4f.translateTmp((float) access.getXOffset(), (float) access.getYOffset(), (float) access.getZOffset());
            texture.invert();
            damageUV.set(Float.intBitsToFloat(fastVertexData[0]), Float.intBitsToFloat(fastVertexData[1]), Float.intBitsToFloat(fastVertexData[2]), 1.0F);
            damageUV.transform(texture);
            damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            damageUV.rotate(facing.getRotationQuaternion());
            fastVertexData[3] = Float.floatToRawIntBits(-damageUV.getX());
            fastVertexData[4] = Float.floatToRawIntBits(-damageUV.getY());
            damageUV.set(Float.intBitsToFloat(fastVertexData[8]), Float.intBitsToFloat(fastVertexData[9]), Float.intBitsToFloat(fastVertexData[10]), 1.0F);
            damageUV.transform(texture);
            damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            damageUV.rotate(facing.getRotationQuaternion());
            fastVertexData[11] = Float.floatToRawIntBits(-damageUV.getX());
            fastVertexData[12] = Float.floatToRawIntBits(-damageUV.getY());
            damageUV.set(Float.intBitsToFloat(fastVertexData[16]), Float.intBitsToFloat(fastVertexData[17]), Float.intBitsToFloat(fastVertexData[18]), 1.0F);
            damageUV.transform(texture);
            damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            damageUV.rotate(facing.getRotationQuaternion());
            fastVertexData[19] = Float.floatToRawIntBits(-damageUV.getX());
            fastVertexData[20] = Float.floatToRawIntBits(-damageUV.getY());
            damageUV.set(Float.intBitsToFloat(fastVertexData[24]), Float.intBitsToFloat(fastVertexData[25]), Float.intBitsToFloat(fastVertexData[26]), 1.0F);
            damageUV.transform(texture);
            damageUV.rotate(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            damageUV.rotate(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            damageUV.rotate(facing.getRotationQuaternion());
            fastVertexData[27] = Float.floatToRawIntBits(-damageUV.getX());
            fastVertexData[28] = Float.floatToRawIntBits(-damageUV.getY());
        }
        if (!access.getColorDisabled()) {
            fastVertexData[5] = colour0;
            fastVertexData[13] = colour1;
            fastVertexData[21] = colour2;
            fastVertexData[29] = colour3;
            access.setHasColor(true);
        }
        access.setHasTexture(true);
        access.setHasNormals(true);
        System.arraycopy(fastVertexData, 0, access.stationapi$getBuffer(), access.stationapi$getBufferPosition(), 24);
        System.arraycopy(fastVertexData, 0, access.stationapi$getBuffer(), access.stationapi$getBufferPosition() + 24, 8);
        System.arraycopy(fastVertexData, 16, access.stationapi$getBuffer(), access.stationapi$getBufferPosition() + 32, 16);
        access.stationapi$setAddedVertexCount(access.stationapi$getAddedVertexCount() + 4);
        access.stationapi$setBufferPosition(access.stationapi$getBufferPosition() + 48);
        access.stationapi$setVertexCount(access.stationapi$getVertexCount() + 6);
        ensureBufferCapacity(48);
    }

    @Override
    public void ensureBufferCapacity(int criticalCapacity) {
        int bufferSize = access.stationapi$getBufferSize();
        if (access.stationapi$getBufferPosition() >= bufferSize - criticalCapacity) {
            if (bufferSize >= MAX_BUFFER_SIZE) {
                // Already at the ceiling; doubling further would overflow bufferSize * 4.
                // Leave the buffer as-is rather than allocating an invalid (negative) capacity.
                return;
            }
            int newBufferSize = Math.min(bufferSize * 2, MAX_BUFFER_SIZE);
            LOGGER.info("Tessellator is nearing its maximum capacity. Increasing the buffer size from {} to {}", bufferSize, newBufferSize);
            access.stationapi$setBufferSize(newBufferSize);
            access.stationapi$setBuffer(Arrays.copyOf(access.stationapi$getBuffer(), newBufferSize));
            ByteBuffer newBuffer = GlAllocationUtils.allocateByteBuffer(newBufferSize * 4);
            access.stationapi$setByteBuffer(newBuffer);
            access.stationapi$setIntBuffer(newBuffer.asIntBuffer());
            access.stationapi$setFloatBuffer(newBuffer.asFloatBuffer());
        }
    }
}
