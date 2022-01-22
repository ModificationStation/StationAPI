package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.block.Bed;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.sortme.MagicBedNumbers;
import net.modificationstation.stationapi.api.client.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.plugin.BlockRendererPlugin;
import net.modificationstation.stationapi.impl.client.model.BakedModelRendererImpl;
import net.modificationstation.stationapi.mixin.render.client.BlockRendererAccessor;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.*;
import java.util.*;

import static net.minecraft.client.render.block.BlockRenderer.fancyGraphics;

public class StationBlockRenderer extends BlockRendererPlugin {

    private boolean renderingMesh;
    public final Set<Atlas> activeAtlases = new HashSet<>();
    public final BlockRendererAccessor blockRendererAccessor;
    public final BakedModelRenderer bakedModelRenderer;

    public StationBlockRenderer(BlockRenderer blockRenderer) {
        super(blockRenderer);
        blockRendererAccessor = (BlockRendererAccessor) blockRenderer;
        bakedModelRenderer = new BakedModelRendererImpl(blockRenderer, this);
    }

    public void startMeshRender() {
        renderingMesh = true;
    }

    public void endMeshRender() {
        renderingMesh = false;
        if (!activeAtlases.isEmpty()) {
            activeAtlases.forEach(atlas -> {
                atlas.bindAtlas();
                Tessellator tessellator = atlas.getTessellator();
                Objects.requireNonNull(tessellator).draw();
                tessellator.setOffset(0, 0, 0);
            });
            activeAtlases.clear();
        }
        Atlases.getTerrain().bindAtlas();
    }

    public Tessellator prepareTessellator(Atlas atlas) {
        Tessellator tessellator;
        if (renderingMesh) {
            tessellator = atlas.getTessellator();
            TessellatorAccessor originalAccessor = (TessellatorAccessor) Tessellator.INSTANCE;
            if (!((TessellatorAccessor) Objects.requireNonNull(tessellator)).getDrawing()) {
                activeAtlases.add(atlas);
                tessellator.start();
                tessellator.setOffset(originalAccessor.getXOffset(), originalAccessor.getYOffset(), originalAccessor.getZOffset());
            }
        } else {
            tessellator = Tessellator.INSTANCE;
            TessellatorAccessor accessor = (TessellatorAccessor) tessellator;
            boolean drawing = accessor.getDrawing();
            boolean hasColour = accessor.getHasColour();
            int colour = accessor.getColour();
            boolean hasNormals = accessor.getHasNormals();
            int normal = accessor.getNormal();
            boolean hasTexture = accessor.getHasTexture();
            double textureX = accessor.getTextureX(), textureY = accessor.getTextureY();
            if (drawing) {
                tessellator.draw();
            }
            atlas.bindAtlas();
            tessellator.start();
            if (drawing) {
                if (hasColour)
                    tessellator.colour(colour);
                if (hasNormals)
                    tessellator.setNormal((normal & 255) / 128F, ((normal >> 8) & 255) / 127F, ((normal >> 16) & 255) / 127F);
                if (hasTexture)
                    tessellator.setTextureXY(textureX, textureY);
            }
        }
        return tessellator;
    }

    @Override
    public void renderWorld(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof BlockWithWorldRenderer) {
            block.updateBoundingBox(blockRendererAccessor.getBlockView(), x, y, z);
            cir.setReturnValue(((BlockWithWorldRenderer) block).renderWorld(blockRenderer, blockRendererAccessor.getBlockView(), x, y, z));
        }
    }

    @Override
    public void renderBed(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        Atlas atlas = ((CustomAtlasProvider) block).getAtlas();
        Tessellator var5 = prepareTessellator(atlas);
        Atlas.Sprite texture;
        int
                var6 = blockRendererAccessor.getBlockView().getTileMeta(blockX, blockY, blockZ),
                var7 = Bed.orientationOnly(var6);
        boolean var8 = Bed.isFoot(var6);
        float
                var9 = 0.5F,
                var10 = 1.0F,
                var11 = 0.8F,
                var12 = 0.6F,
                var25 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ);
        var5.colour(var9 * var25, var9 * var25, var9 * var25);
        int var26 = block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 0);
        texture = atlas.getTexture(var26);
        double
                var37 = (double)blockX + block.minX,
                var39 = (double)blockX + block.maxX,
                var41 = (double)blockY + block.minY + 0.1875D,
                var43 = (double)blockZ + block.minZ,
                var45 = (double)blockZ + block.maxZ;
        var5.vertex(var37, var41, var45, texture.getStartU(), texture.getEndV());
        var5.vertex(var37, var41, var43, texture.getStartU(), texture.getStartV());
        var5.vertex(var39, var41, var43, texture.getEndU(), texture.getStartV());
        var5.vertex(var39, var41, var45, texture.getEndU(), texture.getEndV());
        float var64 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX, blockY + 1, blockZ);
        var5.colour(var10 * var64, var10 * var64, var10 * var64);
        var26 = block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 1);
        texture = atlas.getTexture(var26);
        double
                var30 = texture.getStartU(),
                var32 = texture.getEndU(),
                var34 = texture.getStartV(),
                var36 = texture.getEndV(),
                var38 = var30,
                var40 = var32,
                var42 = var34,
                var44 = var34,
                var46 = var30,
                var48 = var32,
                var50 = var36,
                var52 = var36;
        if (var7 == 0) {
            var40 = var30;
            var42 = var36;
            var46 = var32;
            var52 = var34;
        } else if (var7 == 2) {
            var38 = var32;
            var44 = var36;
            var48 = var30;
            var50 = var34;
        } else if (var7 == 3) {
            var38 = var32;
            var44 = var36;
            var48 = var30;
            var50 = var34;
            var40 = var30;
            var42 = var36;
            var46 = var32;
            var52 = var34;
        }

        double
                var54 = (double)blockX + block.minX,
                var56 = (double)blockX + block.maxX,
                var58 = (double)blockY + block.maxY,
                var60 = (double)blockZ + block.minZ,
                var62 = (double)blockZ + block.maxZ;
        var5.vertex(var56, var58, var62, var46, var50);
        var5.vertex(var56, var58, var60, var38, var42);
        var5.vertex(var54, var58, var60, var40, var44);
        var5.vertex(var54, var58, var62, var48, var52);
        int var65 = MagicBedNumbers.field_792[var7];
        if (var8)
            var65 = MagicBedNumbers.field_792[MagicBedNumbers.field_793[var7]];

        int rotation;
        switch(var7) {
            case 0:
                rotation = 5;
                break;
            case 1:
                rotation = 3;
                break;
            case 2:
            default:
                rotation = 4;
                break;
            case 3:
                rotation = 2;
                break;
        }

        if (var65 != 2 && (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ - 1, 2))) {
            float var69 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ - 1);
            if (block.minZ > 0.0D)
                var69 = var25;

            var5.colour(var11 * var69, var11 * var69, var11 * var69);
            blockRendererAccessor.setMirrorTexture(rotation == 2);
            this.renderEastFace(block, blockX, blockY, blockZ, block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 2));
        }

        if (var65 != 3 && (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ + 1, 3))) {
            float var70 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ + 1);
            if (block.maxZ < 1.0D)
                var70 = var25;

            var5.colour(var11 * var70, var11 * var70, var11 * var70);
            blockRendererAccessor.setMirrorTexture(rotation == 3);
            this.renderWestFace(block, blockX, blockY, blockZ, block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 3));
        }

        if (var65 != 4 && (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), blockX - 1, blockY, blockZ, 4))) {
            float var71 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX - 1, blockY, blockZ);
            if (block.minX > 0.0D)
                var71 = var25;

            var5.colour(var12 * var71, var12 * var71, var12 * var71);
            blockRendererAccessor.setMirrorTexture(rotation == 4);
            this.renderNorthFace(block, blockX, blockY, blockZ, block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 4));
        }

        if (var65 != 5 && (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), blockX + 1, blockY, blockZ, 5))) {
            float var72 = block.getBrightness(blockRendererAccessor.getBlockView(), blockX + 1, blockY, blockZ);
            if (block.maxX < 1.0D)
                var72 = var25;

            var5.colour(var12 * var72, var12 * var72, var12 * var72);
            blockRendererAccessor.setMirrorTexture(rotation == 5);
            this.renderSouthFace(block, blockX, blockY, blockZ, block.getTextureForSide(blockRendererAccessor.getBlockView(), blockX, blockY, blockZ, 5));
        }

        blockRendererAccessor.setMirrorTexture(false);
        cir.setReturnValue(true);
    }

    @Override
    public void renderPlant(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        float var6 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        int var7 = block.getColourMultiplier(blockRendererAccessor.getBlockView(), x, y, z);
        int meta = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        Tessellator var5 = prepareTessellator(((CustomAtlasProvider) block).getAtlas().of(block.getTextureForSide(0, blockRendererAccessor.getBlockView().getTileMeta(x, y, z))));
        float var8 = (float)((var7 >> 16) & 255) / 255.0F;
        float var9 = (float)((var7 >> 8) & 255) / 255.0F;
        float var10 = (float)(var7 & 255) / 255.0F;
        if (GameRenderer.anaglyph3d) {
            float var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
            float var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
            float var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
            var8 = var11;
            var9 = var12;
            var10 = var13;
        }

        var5.colour(var6 * var8, var6 * var9, var6 * var10);
        double var19 = x;
        double var20 = y;
        double var15 = z;
        if (block == BlockBase.TALLGRASS) {
            long var17 = (x * 3129871L) ^ (long)z * 116129781L ^ (long)y;
            var17 = var17 * var17 * 42317861L + var17 * 11L;
            var19 += ((double)((float)((var17 >> 16) & 15L) / 15.0F) - 0.5D) * 0.5D;
            var20 += ((double)((float)((var17 >> 20) & 15L) / 15.0F) - 1.0D) * 0.2D;
            var15 += ((double)((float)((var17 >> 24) & 15L) / 15.0F) - 0.5D) * 0.5D;
        }

        renderCrossed(block, meta, var19, var20, var15);
        cir.setReturnValue(true);
    }

    @Override
    public void renderCrops(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        float var6 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        int meta = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        Tessellator var5 = prepareTessellator(((CustomAtlasProvider) block).getAtlas().of(block.getTextureForSide(0, blockRendererAccessor.getBlockView().getTileMeta(x, y, z))));
        var5.colour(var6, var6, var6);
        renderShiftedColumn(block, meta, x, (float)y - 0.0625F, z);
        cir.setReturnValue(true);
    }

    @Override
    public void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length, CallbackInfo ci) {
        renderTorchTilted(block, renderX, renderY, renderZ, width, length);
        ci.cancel();
    }

    public void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length) {
        Atlas atlas;
        Atlas.Sprite texture;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            atlas = Atlases.getTerrain();
            texture = atlas.getTexture(blockRendererAccessor.getTextureOverride());
        } else {
            int textureIndex = block.getTextureForSide(0);
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
            texture = atlas.getTexture(textureIndex);
        }
        Tessellator t = prepareTessellator(atlas);

        float var16 = (float) texture.getStartU();
        float var17 = (float) texture.getEndU();
        float var18 = (float) texture.getStartV();
        float var19 = (float) texture.getEndV();
        BufferedImage atlasImage = atlas.getImage();
        int
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double var20 = (double)var16 + texture.getWidth() * 0.4375 / atlasWidth;
        double var22 = (double)var18 + texture.getHeight() * 0.375 / atlasHeight;
        double var24 = (double)var16 + texture.getWidth() * 0.5625 / atlasWidth;
        double var26 = (double)var18 + texture.getHeight() * 0.5 / atlasHeight;
        renderX += 0.5D;
        renderZ += 0.5D;
        double var28 = renderX - 0.5D;
        double var30 = renderX + 0.5D;
        double var32 = renderZ - 0.5D;
        double var34 = renderZ + 0.5D;
        double var36 = 0.0625D;
        double var38 = 0.625D;
        double d = renderX + width * (1.0D - var38) - var36;
        double d2 = renderZ + length * (1.0D - var38) - var36;
        double d21 = renderZ + length * (1.0D - var38) + var36;
        double d1 = renderX + width * (1.0D - var38) + var36;
        t.vertex(d, renderY + var38, d2, var20, var22);
        t.vertex(d, renderY + var38, d21, var20, var26);
        t.vertex(d1, renderY + var38, d21, var24, var26);
        t.vertex(d1, renderY + var38, d2, var24, var22);
        t.vertex(renderX - var36, renderY + 1.0D, var32, var16, var18);
        t.vertex(renderX - var36 + width, renderY + 0.0D, var32 + length, var16, var19);
        t.vertex(renderX - var36 + width, renderY + 0.0D, var34 + length, var17, var19);
        t.vertex(renderX - var36, renderY + 1.0D, var34, var17, var18);
        t.vertex(renderX + var36, renderY + 1.0D, var34, var16, var18);
        t.vertex(renderX + width + var36, renderY + 0.0D, var34 + length, var16, var19);
        t.vertex(renderX + width + var36, renderY + 0.0D, var32 + length, var17, var19);
        t.vertex(renderX + var36, renderY + 1.0D, var32, var17, var18);
        t.vertex(var28, renderY + 1.0D, renderZ + var36, var16, var18);
        t.vertex(var28 + width, renderY + 0.0D, renderZ + var36 + length, var16, var19);
        t.vertex(var30 + width, renderY + 0.0D, renderZ + var36 + length, var17, var19);
        t.vertex(var30, renderY + 1.0D, renderZ + var36, var17, var18);
        t.vertex(var30, renderY + 1.0D, renderZ - var36, var16, var18);
        t.vertex(var30 + width, renderY + 0.0D, renderZ - var36 + length, var16, var19);
        t.vertex(var28 + width, renderY + 0.0D, renderZ - var36 + length, var17, var19);
        t.vertex(var28, renderY + 1.0D, renderZ - var36, var17, var18);
    }

    @Override
    public void renderCrossed(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {
        renderCrossed(block, meta, x, y, z);
        ci.cancel();
    }

    public void renderCrossed(BlockBase block, int meta, double x, double y, double z) {
        Atlas atlas;
        Atlas.Sprite texture;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            atlas = Atlases.getTerrain();
            texture = atlas.getTexture(blockRendererAccessor.getTextureOverride());
        } else {
            int textureIndex = block.getTextureForSide(0, meta);
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
            texture = atlas.getTexture(textureIndex);
        }
        Tessellator t = prepareTessellator(atlas);
        double
                var13 = texture.getStartU(),
                var15 = texture.getEndU(),
                var17 = texture.getStartV(),
                var19 = texture.getEndV(),
                var21 = x + 0.5D - (double)0.45F,
                var23 = x + 0.5D + (double)0.45F,
                var25 = z + 0.5D - (double)0.45F,
                var27 = z + 0.5D + (double)0.45F;
        t.vertex(var21, y + 1.0D, var25, var13, var17);
        t.vertex(var21, y + 0.0D, var25, var13, var19);
        t.vertex(var23, y + 0.0D, var27, var15, var19);
        t.vertex(var23, y + 1.0D, var27, var15, var17);
        t.vertex(var23, y + 1.0D, var27, var13, var17);
        t.vertex(var23, y + 0.0D, var27, var13, var19);
        t.vertex(var21, y + 0.0D, var25, var15, var19);
        t.vertex(var21, y + 1.0D, var25, var15, var17);
        t.vertex(var21, y + 1.0D, var27, var13, var17);
        t.vertex(var21, y + 0.0D, var27, var13, var19);
        t.vertex(var23, y + 0.0D, var25, var15, var19);
        t.vertex(var23, y + 1.0D, var25, var15, var17);
        t.vertex(var23, y + 1.0D, var25, var13, var17);
        t.vertex(var23, y + 0.0D, var25, var13, var19);
        t.vertex(var21, y + 0.0D, var27, var15, var19);
        t.vertex(var21, y + 1.0D, var27, var15, var17);
    }

    @Override
    public void renderShiftedColumn(BlockBase block, int meta, double x, double y, double z, CallbackInfo ci) {
        renderShiftedColumn(block, meta, x, y, z);
        ci.cancel();
    }

    public void renderShiftedColumn(BlockBase block, int meta, double x, double y, double z) {
        Atlas atlas;
        Atlas.Sprite texture;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            atlas = Atlases.getTerrain();
            texture = atlas.getTexture(blockRendererAccessor.getTextureOverride());
        } else {
            int textureIndex = block.getTextureForSide(0, meta);
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
            texture = atlas.getTexture(textureIndex);
        }
        Tessellator t = prepareTessellator(atlas);
        double var13 = texture.getStartU();
        double var15 = texture.getEndU();
        double var17 = texture.getStartV();
        double var19 = texture.getEndV();
        double var21 = x + 0.5D - 0.25D;
        double var23 = x + 0.5D + 0.25D;
        double var25 = z + 0.5D - 0.5D;
        double var27 = z + 0.5D + 0.5D;
        t.vertex(var21, y + 1.0D, var25, var13, var17);
        t.vertex(var21, y + 0.0D, var25, var13, var19);
        t.vertex(var21, y + 0.0D, var27, var15, var19);
        t.vertex(var21, y + 1.0D, var27, var15, var17);
        t.vertex(var21, y + 1.0D, var27, var13, var17);
        t.vertex(var21, y + 0.0D, var27, var13, var19);
        t.vertex(var21, y + 0.0D, var25, var15, var19);
        t.vertex(var21, y + 1.0D, var25, var15, var17);
        t.vertex(var23, y + 1.0D, var27, var13, var17);
        t.vertex(var23, y + 0.0D, var27, var13, var19);
        t.vertex(var23, y + 0.0D, var25, var15, var19);
        t.vertex(var23, y + 1.0D, var25, var15, var17);
        t.vertex(var23, y + 1.0D, var25, var13, var17);
        t.vertex(var23, y + 0.0D, var25, var13, var19);
        t.vertex(var23, y + 0.0D, var27, var15, var19);
        t.vertex(var23, y + 1.0D, var27, var15, var17);
        var21 = x + 0.5D - 0.5D;
        var23 = x + 0.5D + 0.5D;
        var25 = z + 0.5D - 0.25D;
        var27 = z + 0.5D + 0.25D;
        t.vertex(var21, y + 1.0D, var25, var13, var17);
        t.vertex(var21, y + 0.0D, var25, var13, var19);
        t.vertex(var23, y + 0.0D, var25, var15, var19);
        t.vertex(var23, y + 1.0D, var25, var15, var17);
        t.vertex(var23, y + 1.0D, var25, var13, var17);
        t.vertex(var23, y + 0.0D, var25, var13, var19);
        t.vertex(var21, y + 0.0D, var25, var15, var19);
        t.vertex(var21, y + 1.0D, var25, var15, var17);
        t.vertex(var23, y + 1.0D, var27, var13, var17);
        t.vertex(var23, y + 0.0D, var27, var13, var19);
        t.vertex(var21, y + 0.0D, var27, var15, var19);
        t.vertex(var21, y + 1.0D, var27, var15, var17);
        t.vertex(var21, y + 1.0D, var27, var13, var17);
        t.vertex(var21, y + 0.0D, var27, var13, var19);
        t.vertex(var23, y + 0.0D, var27, var15, var19);
        t.vertex(var23, y + 1.0D, var27, var15, var17);
    }

    @Override
    public void renderFast(BlockBase block, int x, int y, int z, float r, float g, float b, CallbackInfoReturnable<Boolean> cir) {
        blockRendererAccessor.setShadeTopFace(false);
        Atlas topAtlas = ((CustomAtlasProvider) block).getAtlas();
        Tessellator tessellator;
        boolean rendered = false;
        float initialShadeBottom = 0.5F;
        float initialShadeTop = 1.0F;
        float initialShadeEastWest = 0.8F;
        float initialShadeNorthSouth = 0.6F;
        float shadeTopRed = initialShadeTop * r;
        float shadeTopGreen = initialShadeTop * g;
        float shadeTopBlue = initialShadeTop * b;
        float shadeBottomRed = initialShadeBottom;
        float shadeEastWestRed = initialShadeEastWest;
        float shadeNorthSouthRed = initialShadeNorthSouth;
        float shadeBottomGreen = initialShadeBottom;
        float shadeEastWestGreen = initialShadeEastWest;
        float shadeNorthSouthGreen = initialShadeNorthSouth;
        float shadeBottomBlue = initialShadeBottom;
        float shadeEastWestBlue = initialShadeEastWest;
        float shadeNorthSouthBlue = initialShadeNorthSouth;
        if (block != BlockBase.GRASS) {
            shadeBottomRed = initialShadeBottom * r;
            shadeEastWestRed = initialShadeEastWest * r;
            shadeNorthSouthRed = initialShadeNorthSouth * r;
            shadeBottomGreen = initialShadeBottom * g;
            shadeEastWestGreen = initialShadeEastWest * g;
            shadeNorthSouthGreen = initialShadeNorthSouth * g;
            shadeBottomBlue = initialShadeBottom * b;
            shadeEastWestBlue = initialShadeEastWest * b;
            shadeNorthSouthBlue = initialShadeNorthSouth * b;
        }

        float brightness = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x, y - 1, z, 0)) {
            float brightnessBottom = block.getBrightness(blockRendererAccessor.getBlockView(), x, y - 1, z);
            int textureBottom = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 0);
            tessellator = prepareTessellator(topAtlas.of(textureBottom));
            tessellator.colour(shadeBottomRed * brightnessBottom, shadeBottomGreen * brightnessBottom, shadeBottomBlue * brightnessBottom);
            this.renderBottomFace(block, x, y, z, textureBottom);
            rendered = true;
        }

        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x, y + 1, z, 1)) {
            float brightnessTop = block.getBrightness(blockRendererAccessor.getBlockView(), x, y + 1, z);
            if (block.maxY != 1.0D && !block.material.isLiquid()) {
                brightnessTop = brightness;
            }
            int textureTop = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 1);
            tessellator = prepareTessellator(topAtlas.of(textureTop));
            tessellator.colour(shadeTopRed * brightnessTop, shadeTopGreen * brightnessTop, shadeTopBlue * brightnessTop);
            this.renderTopFace(block, x, y, z, textureTop);
            rendered = true;
        }

        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x, y, z - 1, 2)) {
            float brightnessEast = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z - 1);
            if (block.minZ > 0.0D) {
                brightnessEast = brightness;
            }
            int textureEast = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 2);
            tessellator = prepareTessellator(topAtlas.of(textureEast));
            tessellator.colour(shadeEastWestRed * brightnessEast, shadeEastWestGreen * brightnessEast, shadeEastWestBlue * brightnessEast);
            this.renderEastFace(block, x, y, z, textureEast);
            if (fancyGraphics && topAtlas == Atlases.getTerrain() && textureEast == 3 && blockRendererAccessor.getTextureOverride() < 0) {
                tessellator = prepareTessellator(Atlases.getTerrain());
                tessellator.colour(shadeEastWestRed * brightnessEast * r, shadeEastWestGreen * brightnessEast * g, shadeEastWestBlue * brightnessEast * b);
                this.renderEastFace(block, x, y, z, 38);
            }

            rendered = true;
        }

        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x, y, z + 1, 3)) {
            float brightnessWest = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z + 1);
            if (block.maxZ < 1.0D) {
                brightnessWest = brightness;
            }
            int textureWest = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 3);
            tessellator = prepareTessellator(topAtlas.of(textureWest));
            tessellator.colour(shadeEastWestRed * brightnessWest, shadeEastWestGreen * brightnessWest, shadeEastWestBlue * brightnessWest);
            this.renderWestFace(block, x, y, z, textureWest);
            if (fancyGraphics && topAtlas == Atlases.getTerrain() && textureWest == 3 && blockRendererAccessor.getTextureOverride() < 0) {
                tessellator = prepareTessellator(Atlases.getTerrain());
                tessellator.colour(shadeEastWestRed * brightnessWest * r, shadeEastWestGreen * brightnessWest * g, shadeEastWestBlue * brightnessWest * b);
                this.renderWestFace(block, x, y, z, 38);
            }

            rendered = true;
        }

        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x - 1, y, z, 4)) {
            float brightnessNorth = block.getBrightness(blockRendererAccessor.getBlockView(), x - 1, y, z);
            if (block.minX > 0.0D) {
                brightnessNorth = brightness;
            }
            int textureNorth = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 4);
            tessellator = prepareTessellator(topAtlas.of(textureNorth));
            tessellator.colour(shadeNorthSouthRed * brightnessNorth, shadeNorthSouthGreen * brightnessNorth, shadeNorthSouthBlue * brightnessNorth);
            this.renderNorthFace(block, x, y, z, textureNorth);
            if (fancyGraphics && topAtlas == Atlases.getTerrain() && textureNorth == 3 && blockRendererAccessor.getTextureOverride() < 0) {
                tessellator = prepareTessellator(Atlases.getTerrain());
                tessellator.colour(shadeNorthSouthRed * brightnessNorth * r, shadeNorthSouthGreen * brightnessNorth * g, shadeNorthSouthBlue * brightnessNorth * b);
                this.renderNorthFace(block, x, y, z, 38);
            }

            rendered = true;
        }

        if (blockRendererAccessor.getRenderAllSides() || block.isSideRendered(blockRendererAccessor.getBlockView(), x + 1, y, z, 5)) {
            float brightnessSouth = block.getBrightness(blockRendererAccessor.getBlockView(), x + 1, y, z);
            if (block.maxX < 1.0D) {
                brightnessSouth = brightness;
            }
            int textureSouth = block.getTextureForSide(blockRendererAccessor.getBlockView(), x, y, z, 5);
            tessellator = prepareTessellator(topAtlas.of(textureSouth));
            tessellator.colour(shadeNorthSouthRed * brightnessSouth, shadeNorthSouthGreen * brightnessSouth, shadeNorthSouthBlue * brightnessSouth);
            this.renderSouthFace(block, x, y, z, textureSouth);
            if (fancyGraphics && topAtlas == Atlases.getTerrain() && textureSouth == 3 && blockRendererAccessor.getTextureOverride() < 0) {
                tessellator = prepareTessellator(Atlases.getTerrain());
                tessellator.colour(shadeNorthSouthRed * brightnessSouth * r, shadeNorthSouthGreen * brightnessSouth * g, shadeNorthSouthBlue * brightnessSouth * b);
                this.renderSouthFace(block, x, y, z, 38);
            }
            rendered = true;
        }
        cir.setReturnValue(rendered);
    }

    @Override
    public void renderBottomFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderBottomFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderBottomFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getBottomFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                adjustedRenderY = renderY + block.minY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
            t.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            t.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
        }
        t.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
    }

    @Override
    public void renderTopFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderTopFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderTopFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getTopFaceRotation()) {
            case 1:
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                adjustedRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(endRenderX, adjustedRenderY, endRenderZ, endU1, endV1);
            t.vertex(endRenderX, adjustedRenderY, startRenderZ, endU2, startV2);
            t.vertex(startRenderX, adjustedRenderY, startRenderZ, startU1, startV1);
        }
        t.vertex(startRenderX, adjustedRenderY, endRenderZ, startU2, endV2);
    }

    @Override
    public void renderEastFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderEastFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderEastFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getEastFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                adjustedRenderZ = renderZ + block.minZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(startRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(endRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(endRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(startRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
            t.vertex(endRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            t.vertex(endRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
        }
        t.vertex(startRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
    }

    @Override
    public void renderWestFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderWestFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderWestFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getWestFaceRotation()) {
            case 1:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxX * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.minX * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxX * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                startRenderX = renderX + block.minX,
                endRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                adjustedRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(startRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(startRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(endRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(startRenderX, endRenderY, adjustedRenderZ, startU1, startV1);
            t.vertex(startRenderX, startRenderY, adjustedRenderZ, startU2, endV2);
            t.vertex(endRenderX, startRenderY, adjustedRenderZ, endU1, endV1);
        }
        t.vertex(endRenderX, endRenderY, adjustedRenderZ, endU2, startV2);
    }

    @Override
    public void renderNorthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderNorthFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderNorthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getNorthFaceRotation()) {
            case 1:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 2:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                adjustedRenderX = renderX + block.minX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(adjustedRenderX, endRenderY, endRenderZ, endU2, startV2);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(adjustedRenderX, endRenderY, startRenderZ, startU1, startV1);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(adjustedRenderX, startRenderY, startRenderZ, startU2, endV2);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(adjustedRenderX, endRenderY, endRenderZ, endU2, startV2);
            t.vertex(adjustedRenderX, endRenderY, startRenderZ, startU1, startV1);
            t.vertex(adjustedRenderX, startRenderY, startRenderZ, startU2, endV2);
        }
        t.vertex(adjustedRenderX, startRenderY, endRenderZ, endU1, endV1);
    }

    @Override
    public void renderSouthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex, CallbackInfo ci) {
        renderSouthFace(block, renderX, renderY, renderZ, textureIndex);
        ci.cancel();
    }

    public void renderSouthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        Atlas atlas;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            textureIndex = blockRendererAccessor.getTextureOverride();
            atlas = Atlases.getTerrain();
        } else
            atlas = ((CustomAtlasProvider) block).getAtlas().of(textureIndex);
        Atlas.Sprite texture = atlas.getTexture(textureIndex);
        BufferedImage atlasImage = atlas.getImage();
        Tessellator t = prepareTessellator(atlas);
        int
                texX = texture.getX(),
                texY = texture.getY(),
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                atlasWidth = atlasImage.getWidth(),
                atlasHeight = atlasImage.getHeight();
        double
                startU1 = (texX + block.minZ * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxZ * textureWidth) / atlasWidth,
                startV1 = (texY + textureHeight - block.maxY * textureHeight) / atlasHeight,
                endV1 = (texY + textureHeight - block.minY * textureHeight) / atlasHeight;
        if (blockRendererAccessor.getMirrorTexture()) {
            double temp = startU1;
            startU1 = endU1;
            endU1 = temp;
        }
        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startU1 = texture.getStartU();
            endU1 = texture.getEndU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getStartV();
            endV1 = texture.getEndV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getSouthFaceRotation()) {
            case 1:
                startU1 = (texX + textureWidth - block.maxY * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
                endU1 = (texX + textureWidth - block.minY * textureWidth) / atlasWidth;
                endV1 = (texY + block.minZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startU1 = endU1;
                endU1 = startU2;
                startV2 = endV1;
                endV2 = startV1;
                break;
            case 2:
                startU1 = (texX + block.minY * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endU1 = (texX + block.maxY * textureWidth) / atlasWidth;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                startV2 = startV1;
                endV2 = endV1;
                endU2 = startU1;
                startU2 = endU1;
                startV1 = endV1;
                endV1 = startV2;
                break;
            case 3:
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
                break;
        }
        double
                adjustedRenderX = renderX + block.maxX,
                startRenderY = renderY + block.minY,
                endRenderY = renderY + block.maxY,
                startRenderZ = renderZ + block.minZ,
                endRenderZ = renderZ + block.maxZ;
        if (blockRendererAccessor.getShadeTopFace()) {
            t.colour(
                    blockRendererAccessor.getColourRed00(), blockRendererAccessor.getColourGreen00(), blockRendererAccessor.getColourBlue00()
            );
            t.vertex(adjustedRenderX, startRenderY, endRenderZ, startU2, endV2);
            t.colour(
                    blockRendererAccessor.getColourRed01(), blockRendererAccessor.getColourGreen01(), blockRendererAccessor.getColourBlue01()
            );
            t.vertex(adjustedRenderX, startRenderY, startRenderZ, endU1, endV1);
            t.colour(
                    blockRendererAccessor.getColurRed11(), blockRendererAccessor.getColourGreen11(), blockRendererAccessor.getColourBlue11()
            );
            t.vertex(adjustedRenderX, endRenderY, startRenderZ, endU2, startV2);
            t.colour(
                    blockRendererAccessor.getColourRed10(), blockRendererAccessor.getColourGreen10(), blockRendererAccessor.getColourBlue10()
            );
        } else {
            t.vertex(adjustedRenderX, startRenderY, endRenderZ, startU2, endV2);
            t.vertex(adjustedRenderX, startRenderY, startRenderZ, endU1, endV1);
            t.vertex(adjustedRenderX, endRenderY, startRenderZ, endU2, startV2);
        }
        t.vertex(adjustedRenderX, endRenderY, endRenderZ, startU1, startV1);
    }

    @Override
    public void renderInventory(BlockBase block, int meta, float brightness, CallbackInfo ci) {
        if (block instanceof BlockWithInventoryRenderer) {
            if (blockRenderer.itemColourEnabled) {
                int var5 = block.getBaseColour(meta);
                float var6 = (float)((var5 >> 16) & 255) / 255.0F;
                float var7 = (float)((var5 >> 8) & 255) / 255.0F;
                float var8 = (float)(var5 & 255) / 255.0F;
                GL11.glColor4f(var6 * brightness, var7 * brightness, var8 * brightness, 1.0F);
            }
            ((BlockWithInventoryRenderer) block).renderInventory(blockRenderer, meta);
            ci.cancel();
        }
    }
}
