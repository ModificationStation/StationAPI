package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.util.math.Matrix3f;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.helper.ColorHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;

import java.util.function.Supplier;

public abstract class AbstractQuadRenderer {

    static final int FULL_BRIGHTNESS = 0xF000F0;

    protected final Supplier<Tessellator> bufferFunc;
    protected final BlockRenderInfo blockInfo;
    protected final LightingCalculatorImpl aoCalc;
    protected final RenderContext.QuadTransform transform;
    protected final Vec3f normalVec = new Vec3f();

    protected abstract Matrix4f matrix();

    protected abstract Matrix3f normalMatrix();

    protected abstract int overlay();

    AbstractQuadRenderer(BlockRenderInfo blockInfo, Supplier<Tessellator> bufferFunc, LightingCalculatorImpl aoCalc, RenderContext.QuadTransform transform) {
        this.blockInfo = blockInfo;
        this.bufferFunc = bufferFunc;
        this.aoCalc = aoCalc;
        this.transform = transform;
    }

    /** handles block colour and red-blue swizzle, common to all renders. */
    private void colorizeQuad(MutableQuadViewImpl q, int blockColourIndex) {
        if (blockColourIndex == -1) {
            for (int i = 0; i < 4; i++) {
                q.spriteColor(i, 0, ColorHelper.swapRedBlueIfNeeded(q.spriteColor(i, 0)));
            }
        } else {
            final int blockColour = blockInfo.blockColour(blockColourIndex);

            for (int i = 0; i < 4; i++) {
                q.spriteColor(i, 0, ColorHelper.swapRedBlueIfNeeded(ColorHelper.multiplyColour(blockColour, q.spriteColor(i, 0))));
            }
        }
    }

    private void bufferQuad(MutableQuadViewImpl q) {
        bufferQuad(bufferFunc.get(), q, matrix(), overlay(), normalMatrix(), normalVec);
    }

    // TODO: make this actually work lol
    public static void bufferQuad(Tessellator buff, MutableQuadViewImpl quad, Matrix4f matrix, int overlay, Matrix3f normalMatrix, Vec3f normalVec) {
//        final boolean useNormals = quad.hasVertexNormals();
//
//        if (useNormals) {
//            quad.populateMissingNormals();
//        } else {
//            final Vec3f faceNormal = quad.faceNormal();
//            normalVec.set(faceNormal.getX(), faceNormal.getY(), faceNormal.getZ());
//            normalVec.transform(normalMatrix);
//        }
//
//        for (int i = 0; i < 4; i++) {
//            buff.vertex(matrix, quad.x(i), quad.y(i), quad.z(i));
//            final int colour = quad.spriteColour(i, 0);
//            buff.color(colour & 0xFF, (colour >> 8) & 0xFF, (colour >> 16) & 0xFF, (colour >> 24) & 0xFF);
//            buff.texture(quad.spriteU(i, 0), quad.spriteV(i, 0));
//            buff.overlay(overlay);
//            buff.light(quad.lightmap(i));
//
//            if (useNormals) {
//                normalVec.set(quad.normalX(i), quad.normalY(i), quad.normalZ(i));
//                normalVec.transform(normalMatrix);
//            }
//
//            buff.normal(normalVec.getX(), normalVec.getY(), normalVec.getZ());
//            buff.next();
//        }
    }

    // routines below have a bit of copy-paste code reuse to avoid conditional execution inside a hot loop

    /** for non-emissive mesh quads and all fallback quads with smooth lighting. */
    protected void tessellateSmooth(MutableQuadViewImpl q, int blockColorIndex) {
        colorizeQuad(q, blockColorIndex);

        for (int i = 0; i < 4; i++) {
            q.spriteColor(i, 0, ColorHelper.multiplyRGB(q.spriteColor(i, 0), aoCalc.light[i]));
//            q.spriteColor(i, 0, ColourHelper.multiplyRGB(q.spriteColour(i, 0), aoCalc.ao[i]));
//            q.lightmap(i, ColourHelper.maxBrightness(q.lightmap(i), aoCalc.light[i]));
        }

        bufferQuad(q);
    }

    /** for emissive mesh quads with smooth lighting. */
    protected void tessellateSmoothEmissive(MutableQuadViewImpl q, int blockColorIndex) {
        colorizeQuad(q, blockColorIndex);

//        for (int i = 0; i < 4; i++) {
//            q.spriteColour(i, 0, ColourHelper.multiplyRGB(q.spriteColour(i, 0), aoCalc.ao[i]));
//            q.lightmap(i, FULL_BRIGHTNESS);
//        }

        bufferQuad(q);
    }

    /** for non-emissive mesh quads and all fallback quads with flat lighting. */
    protected void tessellateFlat(MutableQuadViewImpl quad, int blockColorIndex) {
        colorizeQuad(quad, blockColorIndex);

        // TODO: implement
//        final int brightness = flatBrightness(quad, blockInfo.blockState, blockInfo.blockPos);

        for (int i = 0; i < 4; i++) {
            quad.spriteColor(i, 0, ColorHelper.multiplyRGB(quad.spriteColor(i, 0), aoCalc.light[i]));
//            quad.lightmap(i, ColourHelper.maxBrightness(quad.lightmap(i), brightness));
        }

        bufferQuad(quad);
    }

    /** for emissive mesh quads with flat lighting. */
    protected void tessellateFlatEmissive(MutableQuadViewImpl quad, int blockColorIndex) {
        colorizeQuad(quad, blockColorIndex);
        shadeFlatQuad(quad);

//        for (int i = 0; i < 4; i++) {
//            quad.lightmap(i, FULL_BRIGHTNESS);
//        }

        bufferQuad(quad);
    }

    private void shadeFlatQuad(MutableQuadViewImpl quad) {
        float shade = aoCalc.shadeMultiplier(quad.lightFace());
        for (int i = 0; i < 4; i++) {
            quad.spriteColor(i, 0, ColorHelper.multiplyRGB(quad.spriteColor(i, 0), shade));
        }
    }
}
