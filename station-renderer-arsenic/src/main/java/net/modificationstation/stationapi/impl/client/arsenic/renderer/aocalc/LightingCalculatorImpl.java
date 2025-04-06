package net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc;

import net.minecraft.block.Block;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Arrays;

import static net.minecraft.block.Block.BLOCKS_ALLOW_VISION;
import static net.minecraft.util.math.MathHelper.floor;

public final class LightingCalculatorImpl {
    private static final float[] FULL_BRIGHTNESS = new float[] { 1, 1, 1, 1 };

    private final int
            cacheRadius,
            cacheDiameter,
            cacheSelf;

    private Block block;
    private BlockView blockView;
    private int x, y, z;
    private boolean ao;

    private static final int UNCACHED_ID = -1;

    private final int[] emptyIdCache;
    private final int[] idCache;

    private final float[] emptyLightCache;
    private final float[] lightCache;

    public final float[] light = new float[4];

    public LightingCalculatorImpl(int radius) {
        cacheRadius = radius;
        cacheDiameter = cacheRadius * 2 + 1;
        int cacheSize = (int) Math.pow(cacheDiameter, 3);
        cacheSelf = cacheSize / 2;
        emptyIdCache = new int[cacheSize];
        idCache = new int[cacheSize];
        emptyLightCache = new float[cacheSize];
        lightCache = new float[cacheSize];
        Arrays.fill(emptyIdCache, UNCACHED_ID);
        Arrays.fill(emptyLightCache, Float.NaN);
    }

    public void initialize(
            Block block,
            BlockView blockView, int x, int y, int z,
            boolean ao
    ) {
        this.block = block;
        this.blockView = blockView;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ao = ao;
        System.arraycopy(emptyIdCache, 0, idCache, 0, idCache.length);
        System.arraycopy(emptyLightCache, 0, lightCache, 0, lightCache.length);
    }

    private int id(int x, int y, int z) {
        int index = toIndex(x - this.x, y - this.y, z - this.z);
        int id = idCache[index];
        return id == UNCACHED_ID ? idCache[index] = index == cacheSelf ? block.id : blockView.getBlockId(x, y, z) : id;
    }

    private float light(int x, int y, int z) {
        int index = toIndex(x - this.x, y - this.y, z - this.z);
        float brightness = lightCache[index];
        return Float.isNaN(brightness) ? lightCache[index] = block.getLuminance(blockView, x, y, z) : brightness;
    }

    private int toIndex(int x, int y, int z) {
        return ((x + cacheRadius) * cacheDiameter + y + cacheRadius) * cacheDiameter + z + cacheRadius;
    }

    public void calculateForQuad(BakedQuad q) {
        float emission = q.lightEmission();
        
        if (emission == 1) {
            System.arraycopy(FULL_BRIGHTNESS, 0, light, 0, light.length);
            return;
        }
        
        Direction face = q.face();
        calculateForQuad(
                face,
                x + Float.intBitsToFloat(q.vertexData()[0]),
                y + Float.intBitsToFloat(q.vertexData()[1]),
                z + Float.intBitsToFloat(q.vertexData()[2]),
                x + Float.intBitsToFloat(q.vertexData()[8]),
                y + Float.intBitsToFloat(q.vertexData()[9]),
                z + Float.intBitsToFloat(q.vertexData()[10]),
                x + Float.intBitsToFloat(q.vertexData()[16]),
                y + Float.intBitsToFloat(q.vertexData()[17]),
                z + Float.intBitsToFloat(q.vertexData()[18]),
                x + Float.intBitsToFloat(q.vertexData()[24]),
                y + Float.intBitsToFloat(q.vertexData()[25]),
                z + Float.intBitsToFloat(q.vertexData()[26]),
                q.shade()
        );
    
        if (emission == 0) return;

        light[0] = MathHelper.lerp(emission, light[0], 1F);
        light[1] = MathHelper.lerp(emission, light[1], 1F);
        light[2] = MathHelper.lerp(emission, light[2], 1F);
        light[3] = MathHelper.lerp(emission, light[3], 1F);
    }

    private void calculateForQuad(
            Direction face,
            double v00x, double v00y, double v00z,
            double v01x, double v01y, double v01z,
            double v11x, double v11y, double v11z,
            double v10x, double v10y, double v10z,
            boolean shade
    ) {
        if (ao)
            quadSmooth(
                    face,
                    v00x, v00y, v00z,
                    v01x, v01y, v01z,
                    v11x, v11y, v11z,
                    v10x, v10y, v10z
            );
        else
            quadFast(
                    face,
                    v00x, v00y, v00z,
                    v01x, v01y, v01z,
                    v11x, v11y, v11z,
                    v10x, v10y, v10z
            );
        if (shade)
            shadeFace(face);
    }

    @SuppressWarnings("DuplicateExpressions")
    private void quadSmooth(
            Direction face,
            double v00x, double v00y, double v00z,
            double v01x, double v01y, double v01z,
            double v11x, double v11y, double v11z,
            double v10x, double v10y, double v10z
    ) {
        int
                v00x0 = floor(v00x - .5),
                v00y0 = floor(v00y - .5),
                v00z0 = floor(v00z - .5),
                v01x0 = floor(v01x - .5),
                v01y0 = floor(v01y - .5),
                v01z0 = floor(v01z - .5),
                v11x0 = floor(v11x - .5),
                v11y0 = floor(v11y - .5),
                v11z0 = floor(v11z - .5),
                v10x0 = floor(v10x - .5),
                v10y0 = floor(v10y - .5),
                v10z0 = floor(v10z - .5),
                v00x1 = v00x0 + 1,
                v00y1 = v00y0 + 1,
                v00z1 = v00z0 + 1,
                v01x1 = v01x0 + 1,
                v01y1 = v01y0 + 1,
                v01z1 = v01z0 + 1,
                v11x1 = v11x0 + 1,
                v11y1 = v11y0 + 1,
                v11z1 = v11z0 + 1,
                v10x1 = v10x0 + 1,
                v10y1 = v10y0 + 1,
                v10z1 = v10z0 + 1;
        double
                v00dx = v00x - v00x0,
                v00dy = v00y - v00y0,
                v00dz = v00z - v00z0,
                v01dx = v01x - v01x0,
                v01dy = v01y - v01y0,
                v01dz = v01z - v01z0,
                v11dx = v11x - v11x0,
                v11dy = v11y - v11y0,
                v11dz = v11z - v11z0,
                v10dx = v10x - v10x0,
                v10dy = v10y - v10y0,
                v10dz = v10z - v10z0;
        switch (face.getAxis()) {
            case X -> {
                v00dy = v00dy < .5 ? v00dy + .5 : v00dy - .5;
                v00dz = v00dz < .5 ? v00dz + .5 : v00dz - .5;
                v01dy = v01dy < .5 ? v01dy + .5 : v01dy - .5;
                v01dz = v01dz < .5 ? v01dz + .5 : v01dz - .5;
                v11dy = v11dy < .5 ? v11dy + .5 : v11dy - .5;
                v11dz = v11dz < .5 ? v11dz + .5 : v11dz - .5;
                v10dy = v10dy < .5 ? v10dy + .5 : v10dy - .5;
                v10dz = v10dz < .5 ? v10dz + .5 : v10dz - .5;
                switch (face.getDirection()) {
                    case POSITIVE -> {
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0), light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z0)] || BLOCKS_ALLOW_VISION[id(v00x0, v00y1, v00z1)] ? v00y0 : v00y1, v00z1),
                                light(v00x1, BLOCKS_ALLOW_VISION[id(v00x1, v00y0, v00z0)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z1)] ? v00y0 : v00y1, v00z1),
                                light(v00x0, v00y1, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z1)] || BLOCKS_ALLOW_VISION[id(v01x0, v01y1, v01z0)] ? v01y0 : v01y1, v01z0),
                                light(v01x1, BLOCKS_ALLOW_VISION[id(v01x1, v01y0, v01z1)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z0)] ? v01y0 : v01y1, v01z0),
                                light(v01x0, v01y1, v01z0), light(v01x1, v01y1, v01z0), light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, v11y0, v11z0),
                                light(v11x0, BLOCKS_ALLOW_VISION[id(v11x0, v11y1, v11z1)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z0)] ? v11y1 : v11y0, v11z0),
                                light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z1)] || BLOCKS_ALLOW_VISION[id(v11x1, v11y0, v11z0)] ? v11y1 : v11y0, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1), light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0), light(v10x0, v10y0, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z0)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y0, v10z1)] ? v10y1 : v10y0, v10z1),
                                light(v10x1, BLOCKS_ALLOW_VISION[id(v10x1, v10y1, v10z0)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z1)] ? v10y1 : v10y0, v10z1)
                        );
                    }
                    case NEGATIVE -> {
                        v00x0--;
                        v00x1--;
                        v01x0--;
                        v01x1--;
                        v11x0--;
                        v11x1--;
                        v10x0--;
                        v10x1--;
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0), light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x0, v00y1, v00z0)] ? v00y1 : v00y0, v00z1),
                                light(v00x1, BLOCKS_ALLOW_VISION[id(v00x1, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z1)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x0, v01y1, v01z1)] ? v01y1 : v01y0, v01z0),
                                light(v01x1, BLOCKS_ALLOW_VISION[id(v01x1, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1), light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, BLOCKS_ALLOW_VISION[id(v11x0, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z0),
                                light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x1, v11y0, v11z1)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0), light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0), light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y0, v10z0)] ? v10y0 : v10y1, v10z1),
                                light(v10x1, BLOCKS_ALLOW_VISION[id(v10x1, v10y1, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z0)] ? v10y0 : v10y1, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, v10z1)
                        );
                    }
                }
            }
            case Y -> {
                v00dx = v00dx < .5 ? v00dx + .5 : v00dx - .5;
                v00dz = v00dz < .5 ? v00dz + .5 : v00dz - .5;
                v01dx = v01dx < .5 ? v01dx + .5 : v01dx - .5;
                v01dz = v01dz < .5 ? v01dz + .5 : v01dz - .5;
                v11dx = v11dx < .5 ? v11dx + .5 : v11dx - .5;
                v11dz = v11dz < .5 ? v11dz + .5 : v11dz - .5;
                v10dx = v10dx < .5 ? v10dx + .5 : v10dx - .5;
                v10dz = v10dz < .5 ? v10dz + .5 : v10dz - .5;
                switch (face.getDirection()) {
                    case POSITIVE -> {
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y0, v00z0)] ? v00z1 : v00z0),
                                light(v00x0, v00y1, v00z1), light(v00x1, v00y1, BLOCKS_ALLOW_VISION[id(v00x0, v00y1, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z0)] ? v00z1 : v00z0)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y0, v01z1)] ? v01z0 : v01z1),
                                light(v01x0, v01y1, v01z0), light(v01x1, v01y1, BLOCKS_ALLOW_VISION[id(v01x0, v01y1, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z1)] ? v01z0 : v01z1),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, BLOCKS_ALLOW_VISION[id(v11x1, v11y0, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z1)] ? v11z0 : v11z1), light(v11x1, v11y0, v11z0),
                                light(v11x0, v11y1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y1, v11z1)] ? v11z0 : v11z1), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, v10y0, BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y0, v10z0)] ? v10z1 : v10z0), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, BLOCKS_ALLOW_VISION[id(v10x1, v10y1, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z0)] ? v10z1 : v10z0), light(v10x1, v10y1, v10z1)
                        );
                    }
                    case NEGATIVE -> {
                        v00y0--;
                        v00y1--;
                        v01y0--;
                        v01y1--;
                        v11y0--;
                        v11y1--;
                        v10y0--;
                        v10y1--;
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, BLOCKS_ALLOW_VISION[id(v00x1, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z0)] ? v00z1 : v00z0), light(v00x1, v00y0, v00z1),
                                light(v00x0, v00y1, BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x0, v00y1, v00z0)] ? v00z1 : v00z0), light(v00x1, v00y1, v00z1)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, BLOCKS_ALLOW_VISION[id(v01x1, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z1)] ? v01z0 : v01z1), light(v01x1, v01y0, v01z0),
                                light(v01x0, v01y1, BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x0, v01y1, v01z1)] ? v01z0 : v01z1), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, v11y0, BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x1, v11y0, v11z1)] ? v11z0 : v11z1),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, BLOCKS_ALLOW_VISION[id(v11x0, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z1)] ? v11z0 : v11z1),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, v10y0, v10z1), light(v10x1, v10y0, BLOCKS_ALLOW_VISION[id(v10x0, v10y0, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z0)] ? v10z1 : v10z0),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y1, v10z0)] ? v10z1 : v10z0)
                        );
                    }
                }
            }
            case Z -> {
                v00dx = v00dx < .5 ? v00dx + .5 : v00dx - .5;
                v00dy = v00dy < .5 ? v00dy + .5 : v00dy - .5;
                v01dx = v01dx < .5 ? v01dx + .5 : v01dx - .5;
                v01dy = v01dy < .5 ? v01dy + .5 : v01dy - .5;
                v11dx = v11dx < .5 ? v11dx + .5 : v11dx - .5;
                v11dy = v11dy < .5 ? v11dy + .5 : v11dy - .5;
                v10dx = v10dx < .5 ? v10dx + .5 : v10dx - .5;
                v10dy = v10dy < .5 ? v10dy + .5 : v10dy - .5;
                switch (face.getDirection()) {
                    case POSITIVE -> {
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z0)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z1)] ? v00y1 : v00y0, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z0)] ? v01y1 : v01y0, v01z0), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z1)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z0)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z1)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z0)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z0)] ? v10y1 : v10y0, v10z0),
                                light(v10x0, v10y0, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z1)] ? v10y1 : v10y0, v10z1)
                        );
                    }
                    case NEGATIVE -> {
                        v00z0--;
                        v00z1--;
                        v01z0--;
                        v01z1--;
                        v11z0--;
                        v11z1--;
                        v10z0--;
                        v10z1--;
                        light[0] = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z0)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, BLOCKS_ALLOW_VISION[id(v00x0, v00y0, v00z1)] || BLOCKS_ALLOW_VISION[id(v00x1, v00y1, v00z1)] ? v00y1 : v00y0, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        light[1] = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z0)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z0)] ? v01y1 : v01y0, v01z0), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, BLOCKS_ALLOW_VISION[id(v01x0, v01y0, v01z1)] || BLOCKS_ALLOW_VISION[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        light[2] = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z0)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z0)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, BLOCKS_ALLOW_VISION[id(v11x1, v11y1, v11z1)] || BLOCKS_ALLOW_VISION[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        light[3] = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z0)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z0)] ? v10y0 : v10y1, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, BLOCKS_ALLOW_VISION[id(v10x0, v10y1, v10z1)] || BLOCKS_ALLOW_VISION[id(v10x1, v10y0, v10z1)] ? v10y0 : v10y1, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, v10z1)
                        );
                    }
                }
            }
        }
    }

    private void quadFast(
            Direction face,
            double v00x, double v00y, double v00z,
            double v01x, double v01y, double v01z,
            double v11x, double v11y, double v11z,
            double v10x, double v10y, double v10z
    ) {
        double mX = (v00x + v01x + v11x + v10x) / 4;
        double mY = (v00y + v01y + v11y + v10y) / 4;
        double mZ = (v00z + v01z + v11z + v10z) / 4;
        this.light[0] = this.light[1] = this.light[2] = this.light[3] =
                switch (face.getAxis()) {
                    case X -> Math.abs(mX - x);
                    case Y -> Math.abs(mY - y);
                    case Z -> Math.abs(mZ - z);
                } < 0.00001f ?
                        light(floor(mX) + face.getOffsetX(), floor(mY) + face.getOffsetY(), floor(mZ) + face.getOffsetZ()) :
                        light(floor(mX), floor(mY), floor(mZ));
    }

    private void shadeFace(Direction face) {
        if (face != Direction.UP) {
            float shade = shadeMultiplier(face);
            light[0] *= shade;
            light[1] *= shade;
            light[2] *= shade;
            light[3] *= shade;
        }
    }

    public float shadeMultiplier(Direction face) {
        return switch (face) {
            case DOWN -> 0.5F;
            case UP -> 1.0F;
            case EAST, WEST -> 0.8F;
            case NORTH, SOUTH -> 0.6F;
        };
    }
}
