package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.block.Bed;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Fluid;
import net.minecraft.block.Rail;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.sortme.MagicBedNumbers;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.TilePos;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.model.block.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.block.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.CustomAtlasProvider;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.mixin.arsenic.client.BlockRendererAccessor;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class ArsenicBlockRenderer {

    private final BlockRenderer blockRenderer;
    private final BlockRendererAccessor blockRendererAccessor;

    public ArsenicBlockRenderer(BlockRenderer blockRenderer) {
        this.blockRenderer = blockRenderer;
        blockRendererAccessor = (BlockRendererAccessor) blockRenderer;
//        bakedModelRenderer = RendererAccess.INSTANCE.hasRenderer() ? Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer() : null;
    }

    public Tessellator prepareTessellator(Atlas atlas) {
        return Tessellator.INSTANCE;
    }

    public void renderWorld(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = ((BlockStateView) blockRendererAccessor.getBlockView()).getBlockState(x, y, z);
        BakedModel model = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state);
        if (!model.isBuiltin()) {
//            cir.setReturnValue(RendererHolder.RENDERER.renderWorld(blockRenderer, state, model, blockRendererAccessor.getBlockView(), x, y, z, blockRendererAccessor.getTextureOverride()));
        } else //noinspection deprecation
            if (block instanceof BlockWithWorldRenderer renderer) {
                block.updateBoundingBox(blockRendererAccessor.getBlockView(), x, y, z);
                //noinspection deprecation
                cir.setReturnValue(renderer.renderWorld(blockRenderer, blockRendererAccessor.getBlockView(), x, y, z));
        }
    }

    public boolean renderBed(BlockBase block, int blockX, int blockY, int blockZ) {
        ExpandableAtlas atlas = Atlases.getTerrain();
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

        int rotation = switch (var7) {
            case 0 -> 5;
            case 1 -> 3;
            default -> 4;
            case 3 -> 2;
        };

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
        return true;
    }

    public boolean renderLever(BlockBase block, int x, int y, int z) {
        int var5 = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        int var6 = var5 & 7;
        boolean var7 = (var5 & 8) > 0;
        Atlas atlas = ((CustomAtlasProvider) block).getAtlas();
        Tessellator var8 = prepareTessellator(atlas);
        boolean var9 = blockRendererAccessor.getTextureOverride() >= 0;
        if (!var9) {
            blockRendererAccessor.stationapi$setTextureOverride(BlockBase.COBBLESTONE.texture);
        }

        float var10 = 0.25F;
        float var11 = 0.1875F;
        float var12 = 0.1875F;
        if (var6 == 5) {
            block.setBoundingBox(0.5F - var11, 0.0F, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
        } else if (var6 == 6) {
            block.setBoundingBox(0.5F - var10, 0.0F, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
        } else if (var6 == 4) {
            block.setBoundingBox(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0F);
        } else if (var6 == 3) {
            block.setBoundingBox(0.5F - var11, 0.5F - var10, 0.0F, 0.5F + var11, 0.5F + var10, var12);
        } else if (var6 == 2) {
            block.setBoundingBox(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0F, 0.5F + var10, 0.5F + var11);
        } else if (var6 == 1) {
            block.setBoundingBox(0.0F, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
        }

        blockRenderer.renderStandardBlock(block, x, y, z);
        if (!var9) {
            blockRendererAccessor.stationapi$setTextureOverride(-1);
        }

        float var13 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        if (BlockBase.EMITTANCE[block.id] > 0) {
            var13 = 1.0F;
        }

        var8.colour(var13, var13, var13);
        int var14 = block.getTextureForSide(0);
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            var14 = blockRendererAccessor.getTextureOverride();
        }

        Sprite texture = atlas.getTexture(var14).getSprite();
        float var17 = texture.getMinU();
        float var18 = texture.getMaxU();
        float var19 = texture.getMinV();
        float var20 = texture.getMaxV();
        Vec3f[] var21 = new Vec3f[8];
        float var22 = 0.0625F;
        float var23 = 0.0625F;
        float var24 = 0.625F;
        var21[0] = Vec3f.from(-var22, 0.0D, -var23);
        var21[1] = Vec3f.from(var22, 0.0D, -var23);
        var21[2] = Vec3f.from(var22, 0.0D, var23);
        var21[3] = Vec3f.from(-var22, 0.0D, var23);
        var21[4] = Vec3f.from(-var22, var24, -var23);
        var21[5] = Vec3f.from(var22, var24, -var23);
        var21[6] = Vec3f.from(var22, var24, var23);
        var21[7] = Vec3f.from(-var22, var24, var23);

        for(int var25 = 0; var25 < 8; ++var25) {
            if (var7) {
                var21[var25].z -= 0.0625D;
                var21[var25].method_1306(0.69813174F);
            } else {
                var21[var25].z += 0.0625D;
                var21[var25].method_1306(-0.69813174F);
            }

            if (var6 == 6) {
                var21[var25].method_1308(((float)Math.PI / 2F));
            }

            if (var6 < 5) {
                var21[var25].y -= 0.375D;
                var21[var25].method_1306(((float)Math.PI / 2F));
                if (var6 == 4) {
                    var21[var25].method_1308(0.0F);
                }

                if (var6 == 3) {
                    var21[var25].method_1308((float)Math.PI);
                }

                if (var6 == 2) {
                    var21[var25].method_1308(((float)Math.PI / 2F));
                }

                if (var6 == 1) {
                    var21[var25].method_1308((-(float)Math.PI / 2F));
                }

                var21[var25].x += (double)x + 0.5D;
                var21[var25].y += (float)y + 0.5F;
                var21[var25].z += (double)z + 0.5D;
            } else {
                var21[var25].x += (double)x + 0.5D;
                var21[var25].y += (float)y + 0.125F;
                var21[var25].z += (double)z + 0.5D;
            }
        }

        Vec3f var30 = null;
        Vec3f var26 = null;
        Vec3f var27 = null;
        Vec3f var28 = null;

        for(int var29 = 0; var29 < 6; ++var29) {
            if (var29 == 0) {
                var17 = texture.getMinU() + (texture.getMaxU() - texture.getMinU()) * 0.4375F;
                var18 = texture.getMinU() + (texture.getMaxU() - texture.getMinU()) * 0.5625F;
                var19 = texture.getMinV() + (texture.getMaxV() - texture.getMinV()) * 0.375F;
                var20 = (texture.getMinV() + texture.getMaxV()) * 0.5F;
            } else if (var29 == 2) {
                var17 = texture.getMinU() + (texture.getMaxU() - texture.getMinU()) * 0.4375F;
                var18 = texture.getMinU() + (texture.getMaxU() - texture.getMinU()) * 0.5625F;
                var19 = texture.getMinV() + (texture.getMaxV() - texture.getMinV()) * 0.375F;
                var20 = texture.getMaxV();
            }

            switch (var29) {
                case 0 -> {
                    var30 = var21[0];
                    var26 = var21[1];
                    var27 = var21[2];
                    var28 = var21[3];
                }
                case 1 -> {
                    var30 = var21[7];
                    var26 = var21[6];
                    var27 = var21[5];
                    var28 = var21[4];
                }
                case 2 -> {
                    var30 = var21[1];
                    var26 = var21[0];
                    var27 = var21[4];
                    var28 = var21[5];
                }
                case 3 -> {
                    var30 = var21[2];
                    var26 = var21[1];
                    var27 = var21[5];
                    var28 = var21[6];
                }
                case 4 -> {
                    var30 = var21[3];
                    var26 = var21[2];
                    var27 = var21[6];
                    var28 = var21[7];
                }
                case 5 -> {
                    var30 = var21[0];
                    var26 = var21[3];
                    var27 = var21[7];
                    var28 = var21[4];
                }
            }

            var8.vertex(var30.x, var30.y, var30.z, var17, var20);
            var8.vertex(var26.x, var26.y, var26.z, var18, var20);
            var8.vertex(var27.x, var27.y, var27.z, var18, var19);
            var8.vertex(var28.x, var28.y, var28.z, var17, var19);
        }

        return true;
    }

    public boolean renderFire(BlockBase block, int x, int y, int z) {
        Atlas atlas = ((CustomAtlasProvider) block).getAtlas();
        Tessellator var5 = prepareTessellator(atlas);
        int var6 = block.getTextureForSide(0);
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            var6 = blockRendererAccessor.getTextureOverride();
        }

        float var7 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        var5.colour(var7, var7, var7);
        Atlas.Sprite texture = atlas.getTexture(var6);
        double var10 = texture.getStartU();
        double var12 = texture.getEndU();
        double var14 = texture.getStartV();
        double var16 = texture.getEndV();
        float var18 = 1.4F;
        Atlas.Sprite nextTex = atlas.getTexture(var6 + 16);
        if (!blockRendererAccessor.getBlockView().canSuffocate(x, y - 1, z) && !BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x, y - 1, z)) {
            float var60 = 0.2F;
            float var20 = 0.0625F;
            if ((x + y + z & 1) == 1) {
                var10 = nextTex.getStartU();
                var12 = nextTex.getEndU();
                var14 = nextTex.getStartV();
                var16 = nextTex.getEndV();
            }

            if ((x / 2 + y / 2 + z / 2 & 1) == 1) {
                double var62 = var12;
                var12 = var10;
                var10 = var62;
            }

            if (BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x - 1, y, z)) {
                var5.vertex((float) x + var60, (float) y + var18 + var20, z + 1, var12, var14);
                var5.vertex(x, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex(x, (float) (y) + var20, z, var10, var16);
                var5.vertex((float) x + var60, (float) y + var18 + var20, z, var10, var14);
                var5.vertex((float) x + var60, (float) y + var18 + var20, z, var10, var14);
                var5.vertex(x, (float) (y) + var20, z, var10, var16);
                var5.vertex(x, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex((float) x + var60, (float) y + var18 + var20, z + 1, var12, var14);
            }

            if (BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x + 1, y, z)) {
                var5.vertex((float) (x + 1) - var60, (float) y + var18 + var20, z, var10, var14);
                var5.vertex(x + 1, (float) (y) + var20, z, var10, var16);
                var5.vertex(x + 1, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex((float) (x + 1) - var60, (float) y + var18 + var20, z + 1, var12, var14);
                var5.vertex((float) (x + 1) - var60, (float) y + var18 + var20, z + 1, var12, var14);
                var5.vertex(x + 1, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex(x + 1, (float) (y) + var20, z, var10, var16);
                var5.vertex((float) (x + 1) - var60, (float) y + var18 + var20, z, var10, var14);
            }

            if (BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x, y, z - 1)) {
                var5.vertex(x, (float) y + var18 + var20, (float) z + var60, var12, var14);
                var5.vertex(x, (float) (y) + var20, z, var12, var16);
                var5.vertex(x + 1, (float) (y) + var20, z, var10, var16);
                var5.vertex(x + 1, (float) y + var18 + var20, (float) z + var60, var10, var14);
                var5.vertex(x + 1, (float) y + var18 + var20, (float) z + var60, var10, var14);
                var5.vertex(x + 1, (float) (y) + var20, z, var10, var16);
                var5.vertex(x, (float) (y) + var20, z, var12, var16);
                var5.vertex(x, (float) y + var18 + var20, (float) z + var60, var12, var14);
            }

            if (BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x, y, z + 1)) {
                var5.vertex(x + 1, (float) y + var18 + var20, (float) (z + 1) - var60, var10, var14);
                var5.vertex(x + 1, (float) (y) + var20, z + 1, var10, var16);
                var5.vertex(x, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex(x, (float) y + var18 + var20, (float) (z + 1) - var60, var12, var14);
                var5.vertex(x, (float) y + var18 + var20, (float) (z + 1) - var60, var12, var14);
                var5.vertex(x, (float) (y) + var20, z + 1, var12, var16);
                var5.vertex(x + 1, (float) (y) + var20, z + 1, var10, var16);
                var5.vertex(x + 1, (float) y + var18 + var20, (float) (z + 1) - var60, var10, var14);
            }

            if (BlockBase.FIRE.method_1824(blockRendererAccessor.getBlockView(), x, y + 1, z)) {
                double var63 = (double) x + 0.5D + 0.5D;
                double var65 = (double) x + 0.5D - 0.5D;
                double var67 = (double) z + 0.5D + 0.5D;
                double var69 = (double) z + 0.5D - 0.5D;
                double var71 = (double) x + 0.5D - 0.5D;
                double var73 = (double) x + 0.5D + 0.5D;
                double var75 = (double) z + 0.5D - 0.5D;
                double var35 = (double) z + 0.5D + 0.5D;
                var10 = texture.getStartU();
                var12 = texture.getEndU();
                var14 = texture.getStartV();
                var16 = texture.getEndV();
                ++y;
                var18 = -0.2F;
                if ((x + y + z & 1) == 0) {
                    var5.vertex(var71, (float) y + var18, z, var12, var14);
                    var5.vertex(var63, y, z, var12, var16);
                    var5.vertex(var63, y, z + 1, var10, var16);
                    var5.vertex(var71, (float) y + var18, z + 1, var10, var14);
                    var10 = nextTex.getStartU();
                    var12 = nextTex.getEndU();
                    var14 = nextTex.getStartV();
                    var16 = nextTex.getEndV();
                    var5.vertex(var73, (float) y + var18, z + 1, var12, var14);
                    var5.vertex(var65, y, z + 1, var12, var16);
                    var5.vertex(var65, y, z, var10, var16);
                    var5.vertex(var73, (float) y + var18, z, var10, var14);
                } else {
                    var5.vertex(x, (float) y + var18, var35, var12, var14);
                    var5.vertex(x, y, var69, var12, var16);
                    var5.vertex(x + 1, y, var69, var10, var16);
                    var5.vertex(x + 1, (float) y + var18, var35, var10, var14);
                    var10 = nextTex.getStartU();
                    var12 = nextTex.getEndU();
                    var14 = nextTex.getStartV();
                    var16 = nextTex.getEndV();
                    var5.vertex(x + 1, (float) y + var18, var75, var12, var14);
                    var5.vertex(x + 1, y, var67, var12, var16);
                    var5.vertex(x, y, var67, var10, var16);
                    var5.vertex(x, (float) y + var18, var75, var10, var14);
                }
            }
        } else {
            double var19 = (double) x + 0.5D + 0.2D;
            double var21 = (double) x + 0.5D - 0.2D;
            double var23 = (double) z + 0.5D + 0.2D;
            double var25 = (double) z + 0.5D - 0.2D;
            double var27 = (double) x + 0.5D - 0.3D;
            double var29 = (double) x + 0.5D + 0.3D;
            double var31 = (double) z + 0.5D - 0.3D;
            double var33 = (double) z + 0.5D + 0.3D;
            var5.vertex(var27, (float) y + var18, z + 1, var12, var14);
            var5.vertex(var19, y, z + 1, var12, var16);
            var5.vertex(var19, y, z, var10, var16);
            var5.vertex(var27, (float) y + var18, z, var10, var14);
            var5.vertex(var29, (float) y + var18, z, var12, var14);
            var5.vertex(var21, y, z, var12, var16);
            var5.vertex(var21, y, z + 1, var10, var16);
            var5.vertex(var29, (float) y + var18, z + 1, var10, var14);
            var10 = nextTex.getStartU();
            var12 = nextTex.getEndU();
            var14 = nextTex.getStartV();
            var16 = nextTex.getEndV();
            var5.vertex(x + 1, (float) y + var18, var33, var12, var14);
            var5.vertex(x + 1, y, var25, var12, var16);
            var5.vertex(x, y, var25, var10, var16);
            var5.vertex(x, (float) y + var18, var33, var10, var14);
            var5.vertex(x, (float) y + var18, var31, var12, var14);
            var5.vertex(x, y, var23, var12, var16);
            var5.vertex(x + 1, y, var23, var10, var16);
            var5.vertex(x + 1, (float) y + var18, var31, var10, var14);
            var19 = (double) x + 0.5D - 0.5D;
            var21 = (double) x + 0.5D + 0.5D;
            var23 = (double) z + 0.5D - 0.5D;
            var25 = (double) z + 0.5D + 0.5D;
            var27 = (double) x + 0.5D - 0.4D;
            var29 = (double) x + 0.5D + 0.4D;
            var31 = (double) z + 0.5D - 0.4D;
            var33 = (double) z + 0.5D + 0.4D;
            var5.vertex(var27, (float) y + var18, z, var10, var14);
            var5.vertex(var19, y, z, var10, var16);
            var5.vertex(var19, y, z + 1, var12, var16);
            var5.vertex(var27, (float) y + var18, z + 1, var12, var14);
            var5.vertex(var29, (float) y + var18, z + 1, var10, var14);
            var5.vertex(var21, y, z + 1, var10, var16);
            var5.vertex(var21, y, z, var12, var16);
            var5.vertex(var29, (float) y + var18, z, var12, var14);
            var10 = texture.getStartU();
            var12 = texture.getEndU();
            var14 = texture.getStartV();
            var16 = texture.getEndV();
            var5.vertex(x, (float) y + var18, var33, var10, var14);
            var5.vertex(x, y, var25, var10, var16);
            var5.vertex(x + 1, y, var25, var12, var16);
            var5.vertex(x + 1, (float) y + var18, var33, var12, var14);
            var5.vertex(x + 1, (float) y + var18, var31, var10, var14);
            var5.vertex(x + 1, y, var23, var10, var16);
            var5.vertex(x, y, var23, var12, var16);
            var5.vertex(x, (float) y + var18, var31, var12, var14);
        }

        return true;
    }

//    public boolean renderRedstoneDust(BlockBase block, int x, int y, int z) {
//        Tessellator var5 = Tessellator.INSTANCE;
//        int var6 = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
//        int var7 = block.getTextureForSide(1, var6);
//        if (blockRendererAccessor.getTextureOverride() >= 0) {
//            var7 = blockRendererAccessor.getTextureOverride();
//        }
//        Atlas.Sprite sprite = Atlases.getTerrain().getTexture(var7);
//
//        float var8 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
//        float var9 = (float)var6 / 15.0F;
//        float var10 = var9 * 0.6F + 0.4F;
//        if (var6 == 0) {
//            var10 = 0.3F;
//        }
//
//        float var11 = var9 * var9 * 0.7F - 0.5F;
//        float var12 = var9 * var9 * 0.6F - 0.7F;
//        if (var11 < 0.0F) {
//            var11 = 0.0F;
//        }
//
//        if (var12 < 0.0F) {
//            var12 = 0.0F;
//        }
//
//        var5.colour(var8 * var10, var8 * var11, var8 * var12);
//        double var15 = sprite.getStartU();
//        double var17 = sprite.getEndU();
//        double var19 = sprite.getStartV();
//        double var21 = sprite.getEndV();
//        boolean var26 = RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x - 1, y, z, 1) || !blockRendererAccessor.getBlockView().canSuffocate(x - 1, y, z) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x - 1, y - 1, z, -1);
//        boolean var27 = RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x + 1, y, z, 3) || !blockRendererAccessor.getBlockView().canSuffocate(x + 1, y, z) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x + 1, y - 1, z, -1);
//        boolean var28 = RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y, z - 1, 2) || !blockRendererAccessor.getBlockView().canSuffocate(x, y, z - 1) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y - 1, z - 1, -1);
//        boolean var29 = RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y, z + 1, 0) || !blockRendererAccessor.getBlockView().canSuffocate(x, y, z + 1) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y - 1, z + 1, -1);
//        if (!blockRendererAccessor.getBlockView().canSuffocate(x, y + 1, z)) {
//            if (blockRendererAccessor.getBlockView().canSuffocate(x - 1, y, z) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x - 1, y + 1, z, -1)) {
//                var26 = true;
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x + 1, y, z) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x + 1, y + 1, z, -1)) {
//                var27 = true;
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x, y, z - 1) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y + 1, z - 1, -1)) {
//                var28 = true;
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x, y, z + 1) && RedstoneDust.method_1287(blockRendererAccessor.getBlockView(), x, y + 1, z + 1, -1)) {
//                var29 = true;
//            }
//        }
//
//        float var31 = (float)(x);
//        float var32 = (float)(x + 1);
//        float var33 = (float)(z);
//        float var34 = (float)(z + 1);
//        byte var35 = 0;
//        if ((var26 || var27) && !var28 && !var29) {
//            var35 = 1;
//        }
//
//        if ((var28 || var29) && !var27 && !var26) {
//            var35 = 2;
//        }
//
//        if (var35 != 0) {
//            sprite = Atlases.getTerrain().getTexture(var7 + 1);
//            var15 = sprite.getStartU();
//            var17 = sprite.getEndU();
//            var19 = sprite.getStartV();
//            var21 = sprite.getEndV();
//        }
//
//        if (var35 == 0) {
//            if (var27 || var28 || var29 || var26) {
//                if (!var26) {
//                    var31 += 0.3125F;
//                }
//
//                if (!var26) {
//                    var15 += sprite.getWidth() * 0.3125 / Atlases.getTerrain().getWidth();
//                }
//
//                if (!var27) {
//                    var32 -= 0.3125F;
//                }
//
//                if (!var27) {
//                    var17 -= sprite.getHeight() * 0.3125 / Atlases.getTerrain().getHeight();
//                }
//
//                if (!var28) {
//                    var33 += 0.3125F;
//                }
//
//                if (!var28) {
//                    var19 += sprite.getWidth() * 0.3125 / Atlases.getTerrain().getWidth();
//                }
//
//                if (!var29) {
//                    var34 -= 0.3125F;
//                }
//
//                if (!var29) {
//                    var21 -= sprite.getHeight() * 0.3125 / Atlases.getTerrain().getHeight();
//                }
//            }
//
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var17, var19);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var15, var21);
//            var5.colour(var8, var8, var8);
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21 + 0.0625D);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var17, var19 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var15, var21 + 0.0625D);
//        } else if (var35 == 1) {
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var17, var19);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var15, var21);
//            var5.colour(var8, var8, var8);
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21 + 0.0625D);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var17, var19 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var15, var21 + 0.0625D);
//        } else if (var35 == 2) {
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var15, var21);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var17, var19);
//            var5.colour(var8, var8, var8);
//            var5.vertex(var32, (float)y + 0.015625F, var34, var17, var21 + 0.0625D);
//            var5.vertex(var32, (float)y + 0.015625F, var33, var15, var21 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var33, var15, var19 + 0.0625D);
//            var5.vertex(var31, (float)y + 0.015625F, var34, var17, var19 + 0.0625D);
//        }
//
//        if (!blockRendererAccessor.getBlockView().canSuffocate(x, y + 1, z)) {
////            var15 = (float)(var13 + 16) / 256.0F;
////            var17 = ((float)(var13 + 16) + 15.99F) / 256.0F;
////            var19 = (float)var14 / 256.0F;
////            var21 = ((float)var14 + 15.99F) / 256.0F;
//            if (blockRendererAccessor.getBlockView().canSuffocate(x - 1, y, z) && blockRendererAccessor.getBlockView().getTileId(x - 1, y + 1, z) == BlockBase.REDSTONE_DUST.id) {
//                var5.colour(var8 * var10, var8 * var11, var8 * var12);
//                var5.vertex((float)x + 0.015625F, (float)(y + 1) + 0.021875F, z + 1, var17, var19);
//                var5.vertex((float)x + 0.015625F, y, z + 1, var15, var19);
//                var5.vertex((float)x + 0.015625F, y, z, var15, var21);
//                var5.vertex((float)x + 0.015625F, (float)(y + 1) + 0.021875F, z, var17, var21);
//                var5.colour(var8, var8, var8);
//                var5.vertex((float)x + 0.015625F, (float)(y + 1) + 0.021875F, z + 1, var17, var19 + 0.0625D);
//                var5.vertex((float)x + 0.015625F, y, z + 1, var15, var19 + 0.0625D);
//                var5.vertex((float)x + 0.015625F, y, z, var15, var21 + 0.0625D);
//                var5.vertex((float)x + 0.015625F, (float)(y + 1) + 0.021875F, z, var17, var21 + 0.0625D);
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x + 1, y, z) && blockRendererAccessor.getBlockView().getTileId(x + 1, y + 1, z) == BlockBase.REDSTONE_DUST.id) {
//                var5.colour(var8 * var10, var8 * var11, var8 * var12);
//                var5.vertex((float)(x + 1) - 0.015625F, y, z + 1, var15, var21);
//                var5.vertex((float)(x + 1) - 0.015625F, (float)(y + 1) + 0.021875F, z + 1, var17, var21);
//                var5.vertex((float)(x + 1) - 0.015625F, (float)(y + 1) + 0.021875F, z, var17, var19);
//                var5.vertex((float)(x + 1) - 0.015625F, y, z, var15, var19);
//                var5.colour(var8, var8, var8);
//                var5.vertex((float)(x + 1) - 0.015625F, y, z + 1, var15, var21 + 0.0625D);
//                var5.vertex((float)(x + 1) - 0.015625F, (float)(y + 1) + 0.021875F, z + 1, var17, var21 + 0.0625D);
//                var5.vertex((float)(x + 1) - 0.015625F, (float)(y + 1) + 0.021875F, z, var17, var19 + 0.0625D);
//                var5.vertex((float)(x + 1) - 0.015625F, y, z, var15, var19 + 0.0625D);
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x, y, z - 1) && blockRendererAccessor.getBlockView().getTileId(x, y + 1, z - 1) == BlockBase.REDSTONE_DUST.id) {
//                var5.colour(var8 * var10, var8 * var11, var8 * var12);
//                var5.vertex(x + 1, y, (float)z + 0.015625F, var15, var21);
//                var5.vertex(x + 1, (float)(y + 1) + 0.021875F, (float)z + 0.015625F, var17, var21);
//                var5.vertex(x, (float)(y + 1) + 0.021875F, (float)z + 0.015625F, var17, var19);
//                var5.vertex(x, y, (float)z + 0.015625F, var15, var19);
//                var5.colour(var8, var8, var8);
//                var5.vertex(x + 1, y, (float)z + 0.015625F, var15, var21 + 0.0625D);
//                var5.vertex(x + 1, (float)(y + 1) + 0.021875F, (float)z + 0.015625F, var17, var21 + 0.0625D);
//                var5.vertex(x, (float)(y + 1) + 0.021875F, (float)z + 0.015625F, var17, var19 + 0.0625D);
//                var5.vertex(x, y, (float)z + 0.015625F, var15, var19 + 0.0625D);
//            }
//
//            if (blockRendererAccessor.getBlockView().canSuffocate(x, y, z + 1) && blockRendererAccessor.getBlockView().getTileId(x, y + 1, z + 1) == BlockBase.REDSTONE_DUST.id) {
//                var5.colour(var8 * var10, var8 * var11, var8 * var12);
//                var5.vertex(x + 1, (float)(y + 1) + 0.021875F, (float)(z + 1) - 0.015625F, var17, var19);
//                var5.vertex(x + 1, y, (float)(z + 1) - 0.015625F, var15, var19);
//                var5.vertex(x, y, (float)(z + 1) - 0.015625F, var15, var21);
//                var5.vertex(x, (float)(y + 1) + 0.021875F, (float)(z + 1) - 0.015625F, var17, var21);
//                var5.colour(var8, var8, var8);
//                var5.vertex(x + 1, (float)(y + 1) + 0.021875F, (float)(z + 1) - 0.015625F, var17, var19 + 0.0625D);
//                var5.vertex(x + 1, y, (float)(z + 1) - 0.015625F, var15, var19 + 0.0625D);
//                var5.vertex(x, y, (float)(z + 1) - 0.015625F, var15, var21 + 0.0625D);
//                var5.vertex(x, (float)(y + 1) + 0.021875F, (float)(z + 1) - 0.015625F, var17, var21 + 0.0625D);
//            }
//        }
//
//        return true;
//    }

    public boolean renderRails(Rail rail, int x, int y, int z) {
        ExpandableAtlas atlas = Atlases.getTerrain();
        Tessellator var5 = prepareTessellator(atlas);
        int var6 = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        int var7 = rail.getTextureForSide(0, var6);
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            var7 = blockRendererAccessor.getTextureOverride();
        }

        if (rail.method_1108()) {
            var6 &= 7;
        }

        float var8 = rail.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        var5.colour(var8, var8, var8);
        Atlas.Sprite sprite = atlas.getTexture(var7);
        double var11 = sprite.getStartU();
        double var13 = sprite.getEndU();
        double var15 = sprite.getStartV();
        double var17 = sprite.getEndV();
        float var19 = 0.0625F;
        float var20 = (float)(x + 1);
        float var21 = (float)(x + 1);
        float var22 = (float)(x);
        float var23 = (float)(x);
        float var24 = (float)(z);
        float var25 = (float)(z + 1);
        float var26 = (float)(z + 1);
        float var27 = (float)(z);
        float var28 = (float)y + var19;
        float var29 = (float)y + var19;
        float var30 = (float)y + var19;
        float var31 = (float)y + var19;
        if (var6 != 1 && var6 != 2 && var6 != 3 && var6 != 7) {
            if (var6 == 8) {
                var20 = var21 = (float)(x);
                var22 = var23 = (float)(x + 1);
                var24 = var27 = (float)(z + 1);
                var25 = var26 = (float)(z);
            } else if (var6 == 9) {
                var20 = var23 = (float)(x);
                var21 = var22 = (float)(x + 1);
                var24 = var25 = (float)(z);
                var26 = var27 = (float)(z + 1);
            }
        } else {
            var20 = var23 = (float)(x + 1);
            var21 = var22 = (float)(x);
            var24 = var25 = (float)(z + 1);
            var26 = var27 = (float)(z);
        }

        if (var6 != 2 && var6 != 4) {
            if (var6 == 3 || var6 == 5) {
                ++var29;
                ++var30;
            }
        } else {
            ++var28;
            ++var31;
        }

        var5.vertex(var20, var28, var24, var13, var15);
        var5.vertex(var21, var29, var25, var13, var17);
        var5.vertex(var22, var30, var26, var11, var17);
        var5.vertex(var23, var31, var27, var11, var15);
        var5.vertex(var23, var31, var27, var11, var15);
        var5.vertex(var22, var30, var26, var11, var17);
        var5.vertex(var21, var29, var25, var13, var17);
        var5.vertex(var20, var28, var24, var13, var15);
        return true;
    }

    public boolean renderLadder(BlockBase block, int x, int y, int z) {
        ExpandableAtlas atlas = Atlases.getTerrain();
        Tessellator var5 = prepareTessellator(atlas);
        int var6 = block.getTextureForSide(0);
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            var6 = blockRendererAccessor.getTextureOverride();
        }

        float var7 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        var5.colour(var7, var7, var7);
        Atlas.Sprite texture = atlas.getTexture(var6);
        double var10 = texture.getStartU();
        double var12 = texture.getEndU();
        double var14 = texture.getStartV();
        double var16 = texture.getEndV();
        int var18 = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        float var19 = 0.0F;
        float var20 = 0.05F;
        if (var18 == 5) {
            var5.vertex((float)x + var20, (float)(y + 1) + var19, (float)(z + 1) + var19, var10, var14);
            var5.vertex((float)x + var20, (float)(y) - var19, (float)(z + 1) + var19, var10, var16);
            var5.vertex((float)x + var20, (float)(y) - var19, (float)(z) - var19, var12, var16);
            var5.vertex((float)x + var20, (float)(y + 1) + var19, (float)(z) - var19, var12, var14);
        }

        if (var18 == 4) {
            var5.vertex((float)(x + 1) - var20, (float)(y) - var19, (float)(z + 1) + var19, var12, var16);
            var5.vertex((float)(x + 1) - var20, (float)(y + 1) + var19, (float)(z + 1) + var19, var12, var14);
            var5.vertex((float)(x + 1) - var20, (float)(y + 1) + var19, (float)(z) - var19, var10, var14);
            var5.vertex((float)(x + 1) - var20, (float)(y) - var19, (float)(z) - var19, var10, var16);
        }

        if (var18 == 3) {
            var5.vertex((float)(x + 1) + var19, (float)(y) - var19, (float)z + var20, var12, var16);
            var5.vertex((float)(x + 1) + var19, (float)(y + 1) + var19, (float)z + var20, var12, var14);
            var5.vertex((float)(x) - var19, (float)(y + 1) + var19, (float)z + var20, var10, var14);
            var5.vertex((float)(x) - var19, (float)(y) - var19, (float)z + var20, var10, var16);
        }

        if (var18 == 2) {
            var5.vertex((float)(x + 1) + var19, (float)(y + 1) + var19, (float)(z + 1) - var20, var10, var14);
            var5.vertex((float)(x + 1) + var19, (float)(y) - var19, (float)(z + 1) - var20, var10, var16);
            var5.vertex((float)(x) - var19, (float)(y) - var19, (float)(z + 1) - var20, var12, var16);
            var5.vertex((float)(x) - var19, (float)(y + 1) + var19, (float)(z + 1) - var20, var12, var14);
        }

        return true;
    }

    public boolean renderPlant(BlockBase block, int x, int y, int z) {
        float var6 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        int var7 = block.getColourMultiplier(blockRendererAccessor.getBlockView(), x, y, z);
        int meta = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        Tessellator var5 = prepareTessellator(Atlases.getTerrain());
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
        return true;
    }

    public boolean renderCrops(BlockBase block, int x, int y, int z) {
        float var6 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
        int meta = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
        Tessellator t = prepareTessellator(Atlases.getTerrain());
        t.colour(var6, var6, var6);
        renderShiftedColumn(block, meta, x, (float)y - 0.0625F, z);
        return true;
    }

    public void renderTorchTilted(BlockBase block, double renderX, double renderY, double renderZ, double width, double length) {
        Atlas atlas = ((CustomAtlasProvider) block).getAtlas();
        Sprite texture;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            texture = atlas.getTexture(blockRendererAccessor.getTextureOverride()).getSprite();
        } else {
            texture = atlas.getTexture(block.getTextureForSide(0)).getSprite();
        }
        Tessellator t = prepareTessellator(atlas);

        float var16 = texture.getMinU();
        float var17 = texture.getMaxU();
        float var18 = texture.getMinV();
        float var19 = texture.getMaxV();
        float
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV());
        float var20 = var16 + texture.getWidth() * 0.4375F / atlasWidth;
        float var22 = var18 + texture.getHeight() * 0.375F / atlasHeight;
        float var24 = var16 + texture.getWidth() * 0.5625F / atlasWidth;
        float var26 = var18 + texture.getHeight() * 0.5F / atlasHeight;
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

    public void renderCrossed(BlockBase block, int meta, double x, double y, double z) {
        ExpandableAtlas atlas = Atlases.getTerrain();
        Atlas.Sprite texture;
        if (blockRendererAccessor.getTextureOverride() >= 0) {
            texture = atlas.getTexture(blockRendererAccessor.getTextureOverride());
        } else {
            texture = atlas.getTexture(block.getTextureForSide(0, meta));
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

    public void renderShiftedColumn(BlockBase block, int meta, double x, double y, double z) {
        Sprite texture = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(Atlases.getTerrain().getTexture(blockRendererAccessor.getTextureOverride() >= 0 ? blockRendererAccessor.getTextureOverride() : block.getTextureForSide(0, meta)).getId());
        Tessellator t = Tessellator.INSTANCE;
        double var13 = texture.getMinU();
        double var15 = texture.getMaxU();
        double var17 = texture.getMinV();
        double var19 = texture.getMaxV();
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

    public boolean renderFluid(BlockBase block, int x, int y, int z) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        Tessellator t = Tessellator.INSTANCE;

        int var6 = (block.id == BlockBase.FLOWING_WATER.id || block.id == BlockBase.STILL_WATER.id) && Atlases.getTerrain().getTexture(block.getTextureForSide(0)).getSprite().getAnimation() != null ? StationRenderAPI.getBlockColours().getColor(((BlockStateView) blockRendererAccessor.getBlockView()).getBlockState(x, y, z), blockRendererAccessor.getBlockView(), new TilePos(x, y, z), -1) : block.getColourMultiplier(blockRendererAccessor.getBlockView(), x, y, z);
        float var7 = (float)((var6 >> 16) & 255) / 255.0F;
        float var8 = (float)((var6 >> 8) & 255) / 255.0F;
        float var9 = (float)(var6 & 255) / 255.0F;
        boolean var10 = block.isSideRendered(blockRendererAccessor.getBlockView(), x, y + 1, z, 1);
        boolean var11 = block.isSideRendered(blockRendererAccessor.getBlockView(), x, y - 1, z, 0);
        boolean[] var12 = new boolean[] {
                block.isSideRendered(blockRendererAccessor.getBlockView(), x, y, z - 1, 2),
                block.isSideRendered(blockRendererAccessor.getBlockView(), x, y, z + 1, 3),
                block.isSideRendered(blockRendererAccessor.getBlockView(), x - 1, y, z, 4),
                block.isSideRendered(blockRendererAccessor.getBlockView(), x + 1, y, z, 5)
        };
        if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3]) {
            return false;
        } else {
            boolean var13 = false;
            float var14 = 0.5F;
            float var15 = 1.0F;
            float var16 = 0.8F;
            float var17 = 0.6F;
            double var18 = 0.0D;
            double var20 = 1.0D;
            Material var22 = block.material;
            int var23 = blockRendererAccessor.getBlockView().getTileMeta(x, y, z);
            float var24 = blockRendererAccessor.stationapi$method_43(x, y, z, var22);
            float var25 = blockRendererAccessor.stationapi$method_43(x, y, z + 1, var22);
            float var26 = blockRendererAccessor.stationapi$method_43(x + 1, y, z + 1, var22);
            float var27 = blockRendererAccessor.stationapi$method_43(x + 1, y, z, var22);
            if (blockRendererAccessor.getRenderAllSides() || var10) {
                var13 = true;
                Sprite var28 = atlas.getSprite(Atlases.getTerrain().getTexture(block.getTextureForSide(1, var23)).getId());
                float notchCode = 1;
                float var29 = (float) Fluid.method_1223(blockRendererAccessor.getBlockView(), x, y, z, var22);
                if (var29 > -999.0F) {
                    var28 = atlas.getSprite(Atlases.getTerrain().getTexture(block.getTextureForSide(2, var23)).getId());
                    notchCode = 2;
                }
                double atlasWidth = var28.getWidth() / (var28.getMaxU() - var28.getMinU());
                double atlasHeight = var28.getHeight() / (var28.getMaxV() - var28.getMinV());
                double texX = var28.getMinU() * atlasWidth;
                double texY = var28.getMinV() * atlasHeight;

                double var32 = (texX + var28.getWidth() / notchCode / 2D) / atlasWidth;
                double var34 = (texY + var28.getHeight() / notchCode / 2D) / atlasHeight;
                if (var29 < -999.0F) {
                    var29 = 0.0F;
                } else {
                    var32 = (texX + var28.getWidth() / notchCode) / atlasWidth;
                    var34 = (texY + var28.getHeight() / notchCode) / atlasHeight;
                }

                double us = MathHelper.sin(var29) * (var28.getWidth() / notchCode / 2D) / atlasWidth;
                double uc = MathHelper.cos(var29) * (var28.getWidth() / notchCode / 2D) / atlasWidth;
                double vs = MathHelper.sin(var29) * (var28.getHeight() / notchCode / 2D) / atlasHeight;
                double vc = MathHelper.cos(var29) * (var28.getHeight() / notchCode / 2D) / atlasHeight;
                float var38 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y, z);
                t.colour(var15 * var38 * var7, var15 * var38 * var8, var15 * var38 * var9);
                t.vertex(x, y + var24, z, var32 - uc - us, var34 - vc + vs);
                t.vertex(x, y + var25, z + 1, var32 - uc + us, var34 + vc + vs);
                t.vertex(x + 1, y + var26, z + 1, var32 + uc + us, var34 + vc - vs);
                t.vertex(x + 1, y + var27, z, var32 + uc - us, var34 - vc - vs);
            }

            if (blockRendererAccessor.getRenderAllSides() || var11) {
                float var52 = block.getBrightness(blockRendererAccessor.getBlockView(), x, y - 1, z);
                t.colour(var14 * var52, var14 * var52, var14 * var52);
                this.renderBottomFace(block, x, y, z, block.getTextureForSide(0));
                var13 = true;
            }

            for(int var53 = 0; var53 < 4; ++var53) {
                int var54 = x;
                int var55 = z;
                if (var53 == 0) {
                    var55 = z - 1;
                }

                if (var53 == 1) {
                    ++var55;
                }

                if (var53 == 2) {
                    var54 = x - 1;
                }

                if (var53 == 3) {
                    ++var54;
                }

                Sprite var56 = atlas.getSprite(Atlases.getTerrain().getTexture(block.getTextureForSide(var53 + 2, var23)).getId());
                if (blockRendererAccessor.getRenderAllSides() || var12[var53]) {
                    float var35;
                    float var39;
                    float var40;
                    float var58;
                    float var59;
                    float var60;
                    if (var53 == 0) {
                        var35 = var24;
                        var58 = var27;
                        var59 = (float)x;
                        var39 = (float)(x + 1);
                        var60 = (float)z;
                        var40 = (float)z;
                    } else if (var53 == 1) {
                        var35 = var26;
                        var58 = var25;
                        var59 = (float)(x + 1);
                        var39 = (float)x;
                        var60 = (float)(z + 1);
                        var40 = (float)(z + 1);
                    } else if (var53 == 2) {
                        var35 = var25;
                        var58 = var24;
                        var59 = (float)x;
                        var39 = (float)x;
                        var60 = (float)(z + 1);
                        var40 = (float)z;
                    } else {
                        var35 = var27;
                        var58 = var26;
                        var59 = (float)(x + 1);
                        var39 = (float)(x + 1);
                        var60 = (float)z;
                        var40 = (float)(z + 1);
                    }

                    var13 = true;
                    double var41 = var56.getMinU();
                    double var43 = (var56.getMinU() + var56.getMaxU()) / 2;
                    double var45 = var56.getMinV() + (1.0F - var35) * (var56.getMaxV() - var56.getMinV()) / 2;
                    double var47 = var56.getMinV() + (1.0F - var58) * (var56.getMaxV() - var56.getMinV()) / 2;
                    double var49 = (var56.getMinV() + var56.getMaxV()) / 2;
                    float var51 = block.getBrightness(blockRendererAccessor.getBlockView(), var54, y, var55);
                    if (var53 < 2) {
                        var51 *= var16;
                    } else {
                        var51 *= var17;
                    }

                    t.colour(var15 * var51 * var7, var15 * var51 * var8, var15 * var51 * var9);
                    t.vertex(var59, (float)y + var35, var60, var41, var45);
                    t.vertex(var39, (float)y + var58, var40, var43, var47);
                    t.vertex(var39, y, var40, var43, var49);
                    t.vertex(var59, y, var60, var41, var49);
                }
            }

            block.minY = var18;
            block.maxY = var20;
            return var13;
        }
    }

    public void renderBottomFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getBottomFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderTopFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
                startU1 = (texX + block.minX * textureWidth) / atlasWidth,
                endU1 = (texX + block.maxX * textureWidth) / atlasWidth,
                startV1 = (texY + block.minZ * textureHeight) / atlasHeight,
                endV1 = (texY + block.maxZ * textureHeight) / atlasHeight;
        if (block.minX < 0.0D || block.maxX > 1.0D) {
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minZ < 0.0D || block.maxZ > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getTopFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + textureHeight - block.minZ * textureHeight) / atlasHeight;
                endV1 = (texY + textureHeight - block.maxZ * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderEastFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
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
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getEastFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderWestFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
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
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getWestFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minX * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxX * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderNorthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
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
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getNorthFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderSouthFace(BlockBase block, double renderX, double renderY, double renderZ, int textureIndex) {
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
        if (blockRendererAccessor.getTextureOverride() >= 0)
            textureIndex = blockRendererAccessor.getTextureOverride();
        Sprite texture = atlas.getSprite(Atlases.getTerrain().getTexture(textureIndex).getId());
        Tessellator t = Tessellator.INSTANCE;
        double
                atlasWidth = texture.getWidth() / (texture.getMaxU() - texture.getMinU()),
                atlasHeight = texture.getHeight() / (texture.getMaxV() - texture.getMinV()),
                texX = texture.getMinU() * atlasWidth,
                texY = texture.getMinV() * atlasHeight,
                textureWidth = texture.getWidth(),
                textureHeight = texture.getHeight(),
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
            startU1 = texture.getMinU();
            endU1 = texture.getMaxU();
        }

        if (block.minY < 0.0D || block.maxY > 1.0D) {
            startV1 = texture.getMinV();
            endV1 = texture.getMaxV();
        }
        double
                endU2 = endU1,
                startU2 = startU1,
                startV2 = startV1,
                endV2 = endV1;
        switch (blockRendererAccessor.getSouthFaceRotation()) {
            case 1 -> {
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
            }
            case 2 -> {
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
            }
            case 3 -> {
                startU1 = (texX + textureWidth - block.minZ * textureWidth) / atlasWidth;
                endU1 = (texX + textureWidth - block.maxZ * textureWidth) / atlasWidth;
                startV1 = (texY + block.maxY * textureHeight) / atlasHeight;
                endV1 = (texY + block.minY * textureHeight) / atlasHeight;
                endU2 = endU1;
                startU2 = startU1;
                startV2 = startV1;
                endV2 = endV1;
            }
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

    public void renderInventory(BlockBase block, int meta, float brightness, CallbackInfo ci) {
        if (block instanceof BlockWithInventoryRenderer renderer) {
            if (blockRenderer.itemColourEnabled) {
                int var5 = block.getBaseColour(meta);
                float var6 = (float)((var5 >> 16) & 255) / 255.0F;
                float var7 = (float)((var5 >> 8) & 255) / 255.0F;
                float var8 = (float)(var5 & 255) / 255.0F;
                GL11.glColor4f(var6 * brightness, var7 * brightness, var8 * brightness, 1.0F);
            }
            renderer.renderInventory(blockRenderer, meta);
            ci.cancel();
        }
    }
}
