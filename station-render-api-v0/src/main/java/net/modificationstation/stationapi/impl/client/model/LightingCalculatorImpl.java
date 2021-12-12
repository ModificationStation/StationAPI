package net.modificationstation.stationapi.impl.client.model;

import com.google.common.primitives.Ints;
import net.minecraft.block.BlockBase;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.client.model.Quad;
import net.modificationstation.stationapi.api.client.model.Vertex;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.*;
import java.util.function.*;

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

    private final ObjIntConsumer<Vertex> vertexFast = this::vertexFast;

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

    public void calculateForQuad(Quad q) {
        if (ao)
            quadSmooth(q);
        else
            q.applyToVertexesWithIndex(vertexFast);
    }

    private void quadSmooth(Quad q) {
        int x0, y0, z0, x1, y1, z1;
        double x, y, z;
        float brightness;
        if (q.v00.shade) {
            shadeFace(q.v00.lightingFace);
            x0 = floor(this.x + q.v00.x - .5);
            y0 = floor(this.y + q.v00.y - .5);
            z0 = floor(this.z + q.v00.z - .5);
            x1 = x0 + 1;
            y1 = y0 + 1;
            z1 = z0 + 1;
            x = this.x + q.v00.x - x0;
            y = this.y + q.v00.y - y0;
            z = this.z + q.v00.z - z0;
            switch (q.v00.lightingFace) {
                case DOWN:
                    y0--;
                    y1--;
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, ALLOWS_GRASS_UNDER[id(x1, y0, z1)] || ALLOWS_GRASS_UNDER[id(x0, y0, z0)] ? z1 : z0), light(x1, y0, z1),
                            light(x0, y1, ALLOWS_GRASS_UNDER[id(x1, y1, z1)] || ALLOWS_GRASS_UNDER[id(x0, y1, z0)] ? z1 : z0), light(x1, y1, z1)
                    );
                    break;
                case UP:
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y0, z0)] ? z1 : z0),
                            light(x0, y1, z1), light(x1, y1, ALLOWS_GRASS_UNDER[id(x0, y1, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? z1 : z0)
                    );
                    break;
                case EAST:
                    z0--;
                    z1--;
                case WEST:
                    x = x < .5 ? x + .5 : x - .5;
                    y = y < .5 ? y + .5 : y - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? y1 : y0, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? y1 : y0, z1), light(x1, y1, z1)
                    );
                    break;
                case NORTH:
                    x0--;
                    x1--;
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0), light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x0, y1, z0)] ? y1 : y0, z1),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? y1 : y0, z1)
                    );
                    break;
                case SOUTH:
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0), light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x0, y1, z1)] ? y0 : y1, z1),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? y0 : y1, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + q.v00.lightingFace);
            }
            quadLight[0] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
        } else
            quadLight[0] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
        if (q.v01.shade) {
            shadeFace(q.v01.lightingFace);
            x0 = floor(this.x + q.v01.x - .5);
            y0 = floor(this.y + q.v01.y - .5);
            z0 = floor(this.z + q.v01.z - .5);
            x1 = x0 + 1;
            y1 = y0 + 1;
            z1 = z0 + 1;
            x = this.x + q.v01.x - x0;
            y = this.y + q.v01.y - y0;
            z = this.z + q.v01.z - z0;
            switch (q.v01.lightingFace) {
                case DOWN:
                    y0--;
                    y1--;
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, ALLOWS_GRASS_UNDER[id(x1, y0, z0)] || ALLOWS_GRASS_UNDER[id(x0, y0, z1)] ? z0 : z1), light(x1, y0, z0),
                            light(x0, y1, ALLOWS_GRASS_UNDER[id(x1, y1, z0)] || ALLOWS_GRASS_UNDER[id(x0, y1, z1)] ? z0 : z1), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case UP:
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y0, z1)] ? z0 : z1),
                            light(x0, y1, z0), light(x1, y1, ALLOWS_GRASS_UNDER[id(x0, y1, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? z0 : z1),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case EAST:
                    z0--;
                    z1--;
                case WEST:
                    x = x < .5 ? x + .5 : x - .5;
                    y = y < .5 ? y + .5 : y - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? y1 : y0, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? y1 : y0, z1), light(x1, y1, z1)
                    );
                    break;
                case NORTH:
                    x0--;
                    x1--;
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x0, y1, z1)] ? y1 : y0, z0),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? y1 : y0, z0),
                            light(x0, y0, z1), light(x1, y0, z1), light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case SOUTH:
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x0, y1, z0)] ? y0 : y1, z0),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? y0 : y1, z0),
                            light(x0, y1, z0), light(x1, y1, z0), light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + q.v01.lightingFace);
            }
            quadLight[1] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
        } else
            quadLight[1] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
        if (q.v11.shade) {
            shadeFace(q.v11.lightingFace);
            x0 = floor(this.x + q.v11.x - .5);
            y0 = floor(this.y + q.v11.y - .5);
            z0 = floor(this.z + q.v11.z - .5);
            x1 = x0 + 1;
            y1 = y0 + 1;
            z1 = z0 + 1;
            x = this.x + q.v11.x - x0;
            y = this.y + q.v11.y - y0;
            z = this.z + q.v11.z - z0;
            switch (q.v11.lightingFace) {
                case DOWN:
                    y0--;
                    y1--;
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, ALLOWS_GRASS_UNDER[id(x0, y0, z0)] || ALLOWS_GRASS_UNDER[id(x1, y0, z1)] ? z0 : z1),
                            light(x0, y1, z0), light(x1, y1, ALLOWS_GRASS_UNDER[id(x0, y1, z0)] || ALLOWS_GRASS_UNDER[id(x1, y1, z1)] ? z0 : z1),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case UP:
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, ALLOWS_GRASS_UNDER[id(x1, y0, z0)] || ALLOWS_GRASS_UNDER[id(x0, y0, z1)] ? z0 : z1), light(x1, y0, z0),
                            light(x0, y1, ALLOWS_GRASS_UNDER[id(x1, y1, z0)] || ALLOWS_GRASS_UNDER[id(x0, y1, z1)] ? z0 : z1), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case EAST:
                    z0--;
                    z1--;
                case WEST:
                    x = x < .5 ? x + .5 : x - .5;
                    y = y < .5 ? y + .5 : y - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z0)] || ALLOWS_GRASS_UNDER[id(x0, y0, z0)] ? y0 : y1, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z1)] || ALLOWS_GRASS_UNDER[id(x0, y0, z1)] ? y0 : y1, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case NORTH:
                    x0--;
                    x1--;
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z0)] || ALLOWS_GRASS_UNDER[id(x0, y0, z1)] ? y0 : y1, z0),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z0)] || ALLOWS_GRASS_UNDER[id(x1, y0, z1)] ? y0 : y1, z0),
                            light(x0, y1, z0), light(x1, y1, z0), light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case SOUTH:
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z1)] || ALLOWS_GRASS_UNDER[id(x0, y0, z0)] ? y1 : y0, z0),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z1)] || ALLOWS_GRASS_UNDER[id(x1, y0, z0)] ? y1 : y0, z0),
                            light(x0, y0, z1), light(x1, y0, z1), light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + q.v11.lightingFace);
            }
            quadLight[2] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
        } else
            quadLight[2] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
        if (q.v10.shade) {
            shadeFace(q.v10.lightingFace);
            x0 = floor(this.x + q.v10.x - .5);
            y0 = floor(this.y + q.v10.y - .5);
            z0 = floor(this.z + q.v10.z - .5);
            x1 = x0 + 1;
            y1 = y0 + 1;
            z1 = z0 + 1;
            x = this.x + q.v10.x - x0;
            y = this.y + q.v10.y - y0;
            z = this.z + q.v10.z - z0;
            switch (q.v10.lightingFace) {
                case DOWN:
                    y0--;
                    y1--;
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, z1), light(x1, y0, ALLOWS_GRASS_UNDER[id(x0, y0, z1)] || ALLOWS_GRASS_UNDER[id(x1, y0, z0)] ? z1 : z0),
                            light(x0, y1, z1), light(x1, y1, ALLOWS_GRASS_UNDER[id(x0, y1, z1)] || ALLOWS_GRASS_UNDER[id(x1, y1, z0)] ? z1 : z0)
                    );
                    break;
                case UP:
                    x = x < .5 ? x + .5 : x - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, y0, ALLOWS_GRASS_UNDER[id(x1, y0, z1)] || ALLOWS_GRASS_UNDER[id(x0, y0, z0)] ? z1 : z0), light(x1, y0, z1),
                            light(x0, y1, ALLOWS_GRASS_UNDER[id(x1, y1, z1)] || ALLOWS_GRASS_UNDER[id(x0, y1, z0)] ? z1 : z0), light(x1, y1, z1)
                    );
                    break;
                case EAST:
                    z0--;
                    z1--;
                    x = x < .5 ? x + .5 : x - .5;
                    y = y < .5 ? y + .5 : y - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z0)] || ALLOWS_GRASS_UNDER[id(x1, y0, z0)] ? y0 : y1, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z1)] || ALLOWS_GRASS_UNDER[id(x1, y0, z1)] ? y0 : y1, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case WEST:
                    x = x < .5 ? x + .5 : x - .5;
                    y = y < .5 ? y + .5 : y - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z0)] || ALLOWS_GRASS_UNDER[id(x0, y1, z0)] ? y1 : y0, z0),
                            light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, y1, z1), light(x1, ALLOWS_GRASS_UNDER[id(x1, y0, z1)] || ALLOWS_GRASS_UNDER[id(x0, y1, z1)] ? y1 : y0, z1)
                    );
                    break;
                case NORTH:
                    x0--;
                    x1--;
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0), light(x0, y1, z0), light(x1, y1, z0),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z1)] || ALLOWS_GRASS_UNDER[id(x0, y0, z0)] ? y0 : y1, z1),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z1)] || ALLOWS_GRASS_UNDER[id(x1, y0, z0)] ? y0 : y1, z1),
                            light(x0, y1, z1), light(x1, y1, z1)
                    );
                    break;
                case SOUTH:
                    y = y < .5 ? y + .5 : y - .5;
                    z = z < .5 ? z + .5 : z - .5;
                    brightness = MathHelper.interpolate3D(
                            x, y, z,
                            light(x0, y0, z0), light(x1, y0, z0),
                            light(x0, y1, z0), light(x1, y1, z0), light(x0, y0, z1), light(x1, y0, z1),
                            light(x0, ALLOWS_GRASS_UNDER[id(x0, y1, z0)] || ALLOWS_GRASS_UNDER[id(x0, y0, z1)] ? y1 : y0, z1),
                            light(x1, ALLOWS_GRASS_UNDER[id(x1, y1, z0)] || ALLOWS_GRASS_UNDER[id(x1, y0, z1)] ? y1 : y0, z1)
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + q.v10.lightingFace);
            }
            quadLight[3] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
        } else
            quadLight[3] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
    }

    private void vertexFast(Vertex v, int i) {
        if (v.shade) {
            shadeFace(v.lightingFace);
            int
                    x = floor(this.x + v.x),
                    y = floor(this.y + v.y),
                    z = floor(this.z + v.z);
            float brightness;
            switch (v.lightingFace) {
                case DOWN:
                case UP:
                    brightness = v.y % 1 == 0 ? light(x, y + v.lightingFace.vector.y, z) : light(x, y, z);
                    break;
                case EAST:
                case WEST:
                    brightness = v.z % 1 == 0 ? light(x, y, z + v.lightingFace.vector.z) : light(x, y, z);
                    break;
                case NORTH:
                case SOUTH:
                    brightness = v.x % 1 == 0 ? light(x + v.lightingFace.vector.x, y, z) : light(x, y, z);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.lightingFace);
            }
            quadLight[i] = colourFloatToInt(shaded[0] * brightness, shaded[1] * brightness, shaded[2] * brightness);
        } else
            quadLight[i] = colourFloatToInt(colourMultiplierRed, colourMultiplierGreen, colourMultiplierBlue);
    }

    public int get(int index) {
        return quadLight[index];
    }

    private void shadeFace(Direction face) {
        shaded[0] = colourMultiplierRed;
        shaded[1] = colourMultiplierGreen;
        shaded[2] = colourMultiplierBlue;
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
            default:
                throw new IllegalStateException("Unexpected value: " + face);
        }
    }

    private int colourFloatToInt(float r, float g, float b) {
        return (Ints.constrainToRange((int) (r * 255), 0, 255) << 16) +
                (Ints.constrainToRange((int) (g * 255), 0, 255) << 8) +
                (Ints.constrainToRange((int) (b * 255), 0, 255));
    }
}
