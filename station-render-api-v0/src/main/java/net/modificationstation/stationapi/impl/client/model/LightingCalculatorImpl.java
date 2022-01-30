package net.modificationstation.stationapi.impl.client.model;

import com.google.common.primitives.Ints;
import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;

import static net.minecraft.block.BlockBase.ALLOWS_GRASS_UNDER;
import static net.minecraft.util.maths.MathHelper.floor;

public class LightingCalculatorImpl {

    private final int
            cacheRadius,
            cacheDiameter,
            cacheSelf;

    private BlockBase block;
    private BlockView blockView;
    private int x, y, z;
    private float colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue;
    private boolean ao;

    private static final int UNCACHED_ID = -1;

    private final float[] shaded = new float[3];

    private final int[] emptyIdCache;
    private final int[] idCache;

    private final float[] emptyLightCache;
    private final float[] lightCache;

    private final int[] quadLight = new int[4];

    LightingCalculatorImpl(int radius) {
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
            BlockBase block,
            BlockView blockView, int x, int y, int z,
            float colourMultiplierRed, float colourMultiplierGreen, float colourMultiplierBlue,
            boolean ao
    ) {
        this.block = block;
        this.blockView = blockView;
        this.x = x;
        this.y = y;
        this.z = z;
        this.colourMultiplierRed = colourMultiplierRed;
        this.colourMultiplierGreen = colourMultiplierGreen;
        this.colourMultiplierBlue = colourMultiplierBlue;
        this.ao = ao;
        System.arraycopy(emptyIdCache, 0, idCache, 0, idCache.length);
        System.arraycopy(emptyLightCache, 0, lightCache, 0, lightCache.length);
    }

    private int id(int x, int y, int z) {
        int index = toIndex(x - this.x, y - this.y, z - this.z);
        int id = idCache[index];
        return id == UNCACHED_ID ? idCache[index] = index == cacheSelf ? block.id : blockView.getTileId(x, y, z) : id;
    }

    private float light(int x, int y, int z) {
        int index = toIndex(x - this.x, y - this.y, z - this.z);
        float brightness = lightCache[index];
        return Float.isNaN(brightness) ? lightCache[index] = block.getBrightness(blockView, x, y, z) : brightness;
    }

    private int toIndex(int x, int y, int z) {
        return ((x + cacheRadius) * cacheDiameter + y + cacheRadius) * cacheDiameter + z + cacheRadius;
    }

    public int[] calculateForQuad(BakedQuad q) {
        if (ao)
            quadSmooth(q);
//        else
//            q.applyToVertexesWithIndex(vertexFast);

        return quadLight;
    }

    @SuppressWarnings("DuplicateExpressions")
    private void quadSmooth(BakedQuad q) {
        shaded[0] = colourMultiplierRed;
        shaded[1] = colourMultiplierGreen;
        shaded[2] = colourMultiplierBlue;
        Direction face = q.getFace();
        if (q.hasShade())
            shadeFace(face);
        float brightness;
        double
                v00x = Float.intBitsToFloat(q.getVertexData()[0]),
                v00y = Float.intBitsToFloat(q.getVertexData()[1]),
                v00z = Float.intBitsToFloat(q.getVertexData()[2]),
                v01x = Float.intBitsToFloat(q.getVertexData()[8]),
                v01y = Float.intBitsToFloat(q.getVertexData()[9]),
                v01z = Float.intBitsToFloat(q.getVertexData()[10]),
                v11x = Float.intBitsToFloat(q.getVertexData()[16]),
                v11y = Float.intBitsToFloat(q.getVertexData()[17]),
                v11z = Float.intBitsToFloat(q.getVertexData()[18]),
                v10x = Float.intBitsToFloat(q.getVertexData()[24]),
                v10y = Float.intBitsToFloat(q.getVertexData()[25]),
                v10z = Float.intBitsToFloat(q.getVertexData()[26]);
        int
                v00x0 = floor(this.x + v00x - .5),
                v00y0 = floor(this.y + v00y - .5),
                v00z0 = floor(this.z + v00z - .5),
                v01x0 = floor(this.x + v01x - .5),
                v01y0 = floor(this.y + v01y - .5),
                v01z0 = floor(this.z + v01z - .5),
                v11x0 = floor(this.x + v11x - .5),
                v11y0 = floor(this.y + v11y - .5),
                v11z0 = floor(this.z + v11z - .5),
                v10x0 = floor(this.x + v10x - .5),
                v10y0 = floor(this.y + v10y - .5),
                v10z0 = floor(this.z + v10z - .5),
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
                v00dx = this.x + v00x - v00x0,
                v00dy = this.y + v00y - v00y0,
                v00dz = this.z + v00z - v00z0,
                v01dx = this.x + v01x - v01x0,
                v01dy = this.y + v01y - v01y0,
                v01dz = this.z + v01z - v01z0,
                v11dx = this.x + v11x - v11x0,
                v11dy = this.y + v11y - v11y0,
                v11dz = this.z + v11z - v11z0,
                v10dx = this.x + v10x - v10x0,
                v10dy = this.y + v10y - v10y0,
                v10dz = this.z + v10z - v10z0;
        switch (face.axis) {
            case X:
                v00dy = v00dy < .5 ? v00dy + .5 : v00dy - .5;
                v00dz = v00dz < .5 ? v00dz + .5 : v00dz - .5;
                v01dy = v01dy < .5 ? v01dy + .5 : v01dy - .5;
                v01dz = v01dz < .5 ? v01dz + .5 : v01dz - .5;
                v11dy = v11dy < .5 ? v11dy + .5 : v11dy - .5;
                v11dz = v11dz < .5 ? v11dz + .5 : v11dz - .5;
                v10dy = v10dy < .5 ? v10dy + .5 : v10dy - .5;
                v10dz = v10dz < .5 ? v10dz + .5 : v10dz - .5;
                switch (face.direction) {
                    case POSITIVE:
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0), light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z0)] || ALLOWS_GRASS_UNDER[id(v00x0, v00y1, v00z1)] ? v00y0 : v00y1, v00z1),
                                light(v00x1, ALLOWS_GRASS_UNDER[id(v00x1, v00y0, v00z0)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z1)] ? v00y0 : v00y1, v00z1),
                                light(v00x0, v00y1, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z1)] || ALLOWS_GRASS_UNDER[id(v01x0, v01y1, v01z0)] ? v01y0 : v01y1, v01z0),
                                light(v01x1, ALLOWS_GRASS_UNDER[id(v01x1, v01y0, v01z1)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z0)] ? v01y0 : v01y1, v01z0),
                                light(v01x0, v01y1, v01z0), light(v01x1, v01y1, v01z0), light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, v11y0, v11z0),
                                light(v11x0, ALLOWS_GRASS_UNDER[id(v11x0, v11y1, v11z1)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z0)] ? v11y1 : v11y0, v11z0),
                                light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z1)] || ALLOWS_GRASS_UNDER[id(v11x1, v11y0, v11z0)] ? v11y1 : v11y0, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1), light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0), light(v10x0, v10y0, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z0)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y0, v10z1)] ? v10y1 : v10y0, v10z1),
                                light(v10x1, ALLOWS_GRASS_UNDER[id(v10x1, v10y1, v10z0)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z1)] ? v10y1 : v10y0, v10z1)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                    case NEGATIVE:
                        v00x0--;
                        v00x1--;
                        v01x0--;
                        v01x1--;
                        v11x0--;
                        v11x1--;
                        v10x0--;
                        v10x1--;
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0), light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x0, v00y1, v00z0)] ? v00y1 : v00y0, v00z1),
                                light(v00x1, ALLOWS_GRASS_UNDER[id(v00x1, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z1)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x0, v01y1, v01z1)] ? v01y1 : v01y0, v01z0),
                                light(v01x1, ALLOWS_GRASS_UNDER[id(v01x1, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1), light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, ALLOWS_GRASS_UNDER[id(v11x0, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z0),
                                light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x1, v11y0, v11z1)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0), light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0), light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y0, v10z0)] ? v10y0 : v10y1, v10z1),
                                light(v10x1, ALLOWS_GRASS_UNDER[id(v10x1, v10y1, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z0)] ? v10y0 : v10y1, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, v10z1)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                }
                break;
            case Y:
                v00dx = v00dx < .5 ? v00dx + .5 : v00dx - .5;
                v00dz = v00dz < .5 ? v00dz + .5 : v00dz - .5;
                v01dx = v01dx < .5 ? v01dx + .5 : v01dx - .5;
                v01dz = v01dz < .5 ? v01dz + .5 : v01dz - .5;
                v11dx = v11dx < .5 ? v11dx + .5 : v11dx - .5;
                v11dz = v11dz < .5 ? v11dz + .5 : v11dz - .5;
                v10dx = v10dx < .5 ? v10dx + .5 : v10dx - .5;
                v10dz = v10dz < .5 ? v10dz + .5 : v10dz - .5;
                switch (face.direction) {
                    case POSITIVE:
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y0, v00z0)] ? v00z1 : v00z0),
                                light(v00x0, v00y1, v00z1), light(v00x1, v00y1, ALLOWS_GRASS_UNDER[id(v00x0, v00y1, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z0)] ? v00z1 : v00z0)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y0, v01z1)] ? v01z0 : v01z1),
                                light(v01x0, v01y1, v01z0), light(v01x1, v01y1, ALLOWS_GRASS_UNDER[id(v01x0, v01y1, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z1)] ? v01z0 : v01z1),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, ALLOWS_GRASS_UNDER[id(v11x1, v11y0, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z1)] ? v11z0 : v11z1), light(v11x1, v11y0, v11z0),
                                light(v11x0, v11y1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y1, v11z1)] ? v11z0 : v11z1), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, v10y0, ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y0, v10z0)] ? v10z1 : v10z0), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, ALLOWS_GRASS_UNDER[id(v10x1, v10y1, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z0)] ? v10z1 : v10z0), light(v10x1, v10y1, v10z1)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                    case NEGATIVE:
                        v00y0--;
                        v00y1--;
                        v01y0--;
                        v01y1--;
                        v11y0--;
                        v11y1--;
                        v10y0--;
                        v10y1--;
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, v00y1, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, ALLOWS_GRASS_UNDER[id(v00x1, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z0)] ? v00z1 : v00z0), light(v00x1, v00y0, v00z1),
                                light(v00x0, v00y1, ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x0, v00y1, v00z0)] ? v00z1 : v00z0), light(v00x1, v00y1, v00z1)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, ALLOWS_GRASS_UNDER[id(v01x1, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z1)] ? v01z0 : v01z1), light(v01x1, v01y0, v01z0),
                                light(v01x0, v01y1, ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x0, v01y1, v01z1)] ? v01z0 : v01z1), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, v01y1, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, v11y0, ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x1, v11y0, v11z1)] ? v11z0 : v11z1),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, ALLOWS_GRASS_UNDER[id(v11x0, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z1)] ? v11z0 : v11z1),
                                light(v11x0, v11y0, v11z1), light(v11x1, v11y0, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, v10y0, v10z1), light(v10x1, v10y0, ALLOWS_GRASS_UNDER[id(v10x0, v10y0, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z0)] ? v10z1 : v10z0),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y1, v10z0)] ? v10z1 : v10z0)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                }
                break;
            case Z:
                v00dx = v00dx < .5 ? v00dx + .5 : v00dx - .5;
                v00dy = v00dy < .5 ? v00dy + .5 : v00dy - .5;
                v01dx = v01dx < .5 ? v01dx + .5 : v01dx - .5;
                v01dy = v01dy < .5 ? v01dy + .5 : v01dy - .5;
                v11dx = v11dx < .5 ? v11dx + .5 : v11dx - .5;
                v11dy = v11dy < .5 ? v11dy + .5 : v11dy - .5;
                v10dx = v10dx < .5 ? v10dx + .5 : v10dx - .5;
                v10dy = v10dy < .5 ? v10dy + .5 : v10dy - .5;
                switch (face.direction) {
                    case POSITIVE:
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z0)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z1)] ? v00y1 : v00y0, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z0)] ? v01y1 : v01y0, v01z0), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z1)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z0)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z1)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, v10y0, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z0)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z0)] ? v10y1 : v10y0, v10z0),
                                light(v10x0, v10y0, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z1)] ? v10y1 : v10y0, v10z1)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                    case NEGATIVE:
                        v00z0--;
                        v00z1--;
                        v01z0--;
                        v01z1--;
                        v11z0--;
                        v11z1--;
                        v10z0--;
                        v10z1--;
                        brightness = MathHelper.interpolate3D(
                                v00dx, v00dy, v00dz,
                                light(v00x0, v00y0, v00z0), light(v00x1, v00y0, v00z0),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z0)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z0)] ? v00y1 : v00y0, v00z0), light(v00x1, v00y1, v00z0),
                                light(v00x0, v00y0, v00z1), light(v00x1, v00y0, v00z1),
                                light(v00x0, ALLOWS_GRASS_UNDER[id(v00x0, v00y0, v00z1)] || ALLOWS_GRASS_UNDER[id(v00x1, v00y1, v00z1)] ? v00y1 : v00y0, v00z1), light(v00x1, v00y1, v00z1)
                        );
                        quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v01dx, v01dy, v01dz,
                                light(v01x0, v01y0, v01z0), light(v01x1, v01y0, v01z0),
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z0)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z0)] ? v01y1 : v01y0, v01z0), light(v01x1, v01y1, v01z0),
                                light(v01x0, v01y0, v01z1), light(v01x1, v01y0, v01z1),
                                light(v01x0, ALLOWS_GRASS_UNDER[id(v01x0, v01y0, v01z1)] || ALLOWS_GRASS_UNDER[id(v01x1, v01y1, v01z1)] ? v01y1 : v01y0, v01z1), light(v01x1, v01y1, v01z1)
                        );
                        quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v11dx, v11dy, v11dz,
                                light(v11x0, v11y0, v11z0), light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z0)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z0)] ? v11y0 : v11y1, v11z0),
                                light(v11x0, v11y1, v11z0), light(v11x1, v11y1, v11z0),
                                light(v11x0, v11y0, v11z1), light(v11x1, ALLOWS_GRASS_UNDER[id(v11x1, v11y1, v11z1)] || ALLOWS_GRASS_UNDER[id(v11x0, v11y0, v11z1)] ? v11y0 : v11y1, v11z1),
                                light(v11x0, v11y1, v11z1), light(v11x1, v11y1, v11z1)
                        );
                        quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        brightness = MathHelper.interpolate3D(
                                v10dx, v10dy, v10dz,
                                light(v10x0, ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z0)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z0)] ? v10y0 : v10y1, v10z0), light(v10x1, v10y0, v10z0),
                                light(v10x0, v10y1, v10z0), light(v10x1, v10y1, v10z0),
                                light(v10x0, ALLOWS_GRASS_UNDER[id(v10x0, v10y1, v10z1)] || ALLOWS_GRASS_UNDER[id(v10x1, v10y0, v10z1)] ? v10y0 : v10y1, v10z1), light(v10x1, v10y0, v10z1),
                                light(v10x0, v10y1, v10z1), light(v10x1, v10y1, v10z1)
                        );
                        quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
                        break;
                }
                break;
        }
    }

//    private void vertexFast(Vertex v, int i) {
//        if (v.shade) {
//            shadeFace(v.lightingFace);
//            int
//                    x = floor(this.x + v.x),
//                    y = floor(this.y + v.y),
//                    z = floor(this.z + v.z);
//            float brightness;
//            switch (v.lightingFace) {
//                case DOWN:
//                case UP:
//                    brightness = v.y % 1 == 0 ? light(x, y + v.lightingFace.vector.y, z) : light(x, y, z);
//                    break;
//                case EAST:
//                case WEST:
//                    brightness = v.z % 1 == 0 ? light(x, y, z + v.lightingFace.vector.z) : light(x, y, z);
//                    break;
//                case NORTH:
//                case SOUTH:
//                    brightness = v.x % 1 == 0 ? light(x + v.lightingFace.vector.x, y, z) : light(x, y, z);
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + v.lightingFace);
//            }
//            quadLight[i] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
//        } else
//            quadLight[i] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
//    }

    private void shadeFace(Direction face) {
        switch (face) {
            case DOWN:
                shaded[0] *= 0.5F;
                shaded[1] *= 0.5F;
                shaded[2] *= 0.5F;
                break;
            case UP:
                break;
            case EAST:
            case WEST:
                shaded[0] *= 0.8F;
                shaded[1] *= 0.8F;
                shaded[2] *= 0.8F;
                break;
            case NORTH:
            case SOUTH:
                shaded[0] *= 0.6F;
                shaded[1] *= 0.6F;
                shaded[2] *= 0.6F;
                break;
        }
    }

    private int colourFloatToInt(float r, float g, float b) {
        return (255 << 24) |
                (Ints.constrainToRange((int) (r * 255), 0, 255) << 16) |
                (Ints.constrainToRange((int) (g * 255), 0, 255) << 8) |
                (Ints.constrainToRange((int) (b * 255), 0, 255));
    }
}
