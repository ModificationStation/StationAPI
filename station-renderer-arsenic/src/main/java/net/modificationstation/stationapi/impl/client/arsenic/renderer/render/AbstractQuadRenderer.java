package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.RenderContext;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.helper.ColourHelper;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.mesh.MutableQuadViewImpl;

public abstract class AbstractQuadRenderer {

    protected final BlockRenderInfo blockInfo;
    protected final LightingCalculatorImpl aoCalc;
    protected final RenderContext.QuadTransform transform;

    AbstractQuadRenderer(BlockRenderInfo blockInfo, LightingCalculatorImpl aoCalc, RenderContext.QuadTransform transform) {
        this.blockInfo = blockInfo;
        this.aoCalc = aoCalc;
        this.transform = transform;
    }

    /** handles block color and red-blue swizzle, common to all renders. */
    private void colorizeQuad(MutableQuadViewImpl q, int blockColourIndex) {
        if (blockColourIndex == -1) {
            for (int i = 0; i < 4; i++) {
                q.spriteColour(i, 0, ColourHelper.swapRedBlueIfNeeded(q.spriteColour(i, 0)));
            }
        } else {
            final int blockColour = blockInfo.blockColour(blockColourIndex);

            for (int i = 0; i < 4; i++) {
                q.spriteColour(i, 0, ColourHelper.swapRedBlueIfNeeded(ColourHelper.multiplyColour(blockColour, q.spriteColour(i, 0))));
            }
        }
    }

    private void tessellateQuad(MutableQuadViewImpl q) {
        StationTessellator.get(Tessellator.INSTANCE).quad(
                q.data(),
                0, 0, 0,
                q.spriteColour(0, 0),
                q.spriteColour(1, 0),
                q.spriteColour(2, 0),
                q.spriteColour(3, 0)
        );
    }

    // routines below have a bit of copy-paste code reuse to avoid conditional execution inside a hot loop

    /** for non-emissive mesh quads and all fallback quads with smooth lighting. */
    protected void tessellateSmooth(MutableQuadViewImpl q, int blockColorIndex) {
        colorizeQuad(q, blockColorIndex);

        for (int i = 0; i < 4; i++) {
            q.spriteColour(i, 0, ColourHelper.multiplyRGB(q.spriteColour(i, 0), aoCalc.light[i]));
//            q.spriteColor(i, 0, ColourHelper.multiplyRGB(q.spriteColour(i, 0), aoCalc.ao[i]));
//            q.lightmap(i, ColourHelper.maxBrightness(q.lightmap(i), aoCalc.light[i]));
        }

        tessellateQuad(q);
    }

    /** for emissive mesh quads with smooth lighting. */
    protected void tessellateSmoothEmissive(MutableQuadViewImpl q, int blockColorIndex) {
        colorizeQuad(q, blockColorIndex);

//        for (int i = 0; i < 4; i++) {
//            q.spriteColour(i, 0, ColourHelper.multiplyRGB(q.spriteColour(i, 0), aoCalc.ao[i]));
//            q.lightmap(i, FULL_BRIGHTNESS);
//        }

        tessellateQuad(q);
    }

    /** for non-emissive mesh quads and all fallback quads with flat lighting. */
    protected void tessellateFlat(MutableQuadViewImpl quad, int blockColorIndex) {
        colorizeQuad(quad, blockColorIndex);

        // TODO: implement
//        final int brightness = flatBrightness(quad, blockInfo.blockState, blockInfo.blockPos);

        for (int i = 0; i < 4; i++) {
            quad.spriteColour(i, 0, ColourHelper.multiplyRGB(quad.spriteColour(i, 0), aoCalc.light[i]));
//            quad.lightmap(i, ColourHelper.maxBrightness(quad.lightmap(i), brightness));
        }

        tessellateQuad(quad);
    }

    /** for emissive mesh quads with flat lighting. */
    protected void tessellateFlatEmissive(MutableQuadViewImpl quad, int blockColorIndex) {
        colorizeQuad(quad, blockColorIndex);
        shadeFlatQuad(quad);

//        for (int i = 0; i < 4; i++) {
//            quad.lightmap(i, FULL_BRIGHTNESS);
//        }

        tessellateQuad(quad);
    }

    private void shadeFlatQuad(MutableQuadViewImpl quad) {
        float shade = aoCalc.shadeMultiplier(quad.lightFace());
        for (int i = 0; i < 4; i++) {
            quad.spriteColour(i, 0, ColourHelper.multiplyRGB(quad.spriteColour(i, 0), shade));
        }
    }
}
