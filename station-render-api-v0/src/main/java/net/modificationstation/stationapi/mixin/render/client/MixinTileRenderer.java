package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.sortme.GameRenderer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockFaces;
import net.modificationstation.stationapi.api.client.model.BlockModelProvider;
import net.modificationstation.stationapi.api.client.model.BlockWithInventoryRenderer;
import net.modificationstation.stationapi.api.client.model.BlockWithWorldRenderer;
import net.modificationstation.stationapi.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationapi.api.client.model.CustomModel;
import net.modificationstation.stationapi.api.client.model.CustomTexturedQuad;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(TileRenderer.class)
public abstract class MixinTileRenderer {
    @Shadow
    private int field_55;
    @Shadow
    private boolean field_92;
    @Shadow
    private float field_93;
    @Shadow
    private TileView field_82;
    @Shadow
    private float field_94;
    @Shadow
    private float field_95;
    @Shadow
    private float field_96;
    @Shadow
    private float field_97;
    @Shadow
    private float field_98;
    @Shadow
    private float field_99;
    @Shadow
    private float field_100;
    @Shadow
    private float field_101;
    @Shadow
    private float field_102;
    @Shadow
    private float field_103;
    @Shadow
    private float field_104;
    @Shadow
    private float field_105;
    @Shadow
    private float field_41;
    @Shadow
    private float field_42;
    @Shadow
    private float field_56;
    @Shadow
    private float field_57;
    @Shadow
    private float field_58;
    @Shadow
    private float field_59;
    @Shadow
    private float field_60;
    @Shadow
    private float field_61;
    @Shadow
    private float field_62;
    @Shadow
    private float field_63;
    @Shadow
    private float field_64;
    @Shadow
    private float field_65;
    @Shadow
    private float field_66;
    @Shadow
    private float field_68;
    @Shadow
    private boolean field_69;
    @Shadow
    private boolean field_70;
    @Shadow
    private boolean field_71;
    @Shadow
    private boolean field_72;
    @Shadow
    private boolean field_73;
    @Shadow
    private boolean field_74;
    @Shadow
    private boolean field_75;
    @Shadow
    private boolean field_76;
    @Shadow
    private boolean field_77;
    @Shadow
    private boolean field_78;
    @Shadow
    private boolean field_79;
    @Shadow
    private boolean field_80;
    @Shadow
    private float field_43;
    @Shadow
    private float field_44;
    @Shadow
    private float field_45;
    @Shadow
    private float field_46;
    @Shadow
    private float field_47;
    @Shadow
    private float field_48;
    @Shadow
    private float field_49;
    @Shadow
    private float field_50;
    @Shadow
    private float field_51;
    @Shadow
    private float field_52;
    @Shadow
    private float field_53;
    @Shadow
    private float field_54;
    private boolean inventory;

    @Inject(method = "method_57(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "HEAD"), cancellable = true)
    public void hijackModels(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof BlockModelProvider) {
            Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
            Level level = minecraft.level;
            CustomModel model = ((BlockModelProvider) block).getCustomWorldModel(level, x, y, z, level.getTileMeta(x, y, z));
            if (model != null) {
                Tessellator tessellator = Tessellator.INSTANCE;

                tessellator.draw();
                TextureRegistry lastRegistry = TextureRegistry.currentRegistry();
                int lastTex = lastRegistry.currentTexture();
                TextureRegistry.unbind();

                for (CustomCuboidRenderer cuboid : model.getCuboids()) {
                    for (CustomTexturedQuad texturedQuad : cuboid.getCubeQuads()) {
                        if (texturedQuad.getTexture() != null) {
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/" + cuboid.getModID() + "/" + StationAPI.MODID + "/models/textures/" + texturedQuad.getTexture() + ".png"));
                        }
                        tessellator.start();
                        tessellator.colour(1.0F, 1.0F, 1.0F);
                        for (QuadPoint quadPoint : texturedQuad.getQuadPoints()) {
                            int var5 = block.getColourMultiplier(this.field_82, x, y, z);
                            float var6 = (float) (var5 >> 16 & 255) / 255.0F;
                            float var77 = (float) (var5 >> 8 & 255) / 255.0F;
                            float var88 = (float) (var5 & 255) / 255.0F;
                            if (GameRenderer.field_2340) {
                                var6 = (var6 * 30.0F + var77 * 59.0F + var88 * 11.0F) / 100.0F;
                                var77 = (var6 * 30.0F + var77 * 70.0F) / 100.0F;
                                var88 = (var6 * 30.0F + var88 * 70.0F) / 100.0F;
                            }
                            this.field_92 = true;
                            float var9 = this.field_93;
                            float var10 = this.field_93;
                            float var11 = this.field_93;
                            float var12 = this.field_93;
                            boolean var13 = true;
                            boolean var14 = true;
                            boolean var15 = true;
                            boolean var16 = true;
                            boolean var17 = true;
                            boolean var18 = true;
                            this.field_93 = block.getBrightness(this.field_82, x, y, z);
                            this.field_94 = block.getBrightness(this.field_82, x - 1, y, z);
                            this.field_95 = block.getBrightness(this.field_82, x, y - 1, z);
                            this.field_96 = block.getBrightness(this.field_82, x, y, z - 1);
                            this.field_97 = block.getBrightness(this.field_82, x + 1, y, z);
                            this.field_98 = block.getBrightness(this.field_82, x, y + 1, z);
                            this.field_99 = block.getBrightness(this.field_82, x, y, z + 1);
                            this.field_70 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x + 1, y + 1, z)];
                            this.field_78 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x + 1, y - 1, z)];
                            this.field_74 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x + 1, y, z + 1)];
                            this.field_76 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x + 1, y, z - 1)];
                            this.field_71 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x - 1, y + 1, z)];
                            this.field_79 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x - 1, y - 1, z)];
                            this.field_73 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x - 1, y, z - 1)];
                            this.field_75 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x - 1, y, z + 1)];
                            this.field_72 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x, y + 1, z + 1)];
                            this.field_69 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x, y + 1, z - 1)];
                            this.field_80 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x, y - 1, z + 1)];
                            this.field_77 = BlockBase.ALLOWS_GRASS_UNDER[this.field_82.getTileId(x, y - 1, z - 1)];
                            //<editor-fold desc="320 lines of rendering code.">
                            if (texturedQuad.getSide() == BlockFaces.DOWN) {
                                int yTemp = y;
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_95;
                                } else {
                                    --yTemp;
                                    this.field_101 = block.getBrightness(this.field_82, x - 1, yTemp, z);
                                    this.field_103 = block.getBrightness(this.field_82, x, yTemp, z - 1);
                                    this.field_104 = block.getBrightness(this.field_82, x, yTemp, z + 1);
                                    this.field_41 = block.getBrightness(this.field_82, x + 1, yTemp, z);
                                    if (!this.field_77 && !this.field_79) {
                                        this.field_100 = this.field_101;
                                    } else {
                                        this.field_100 = block.getBrightness(this.field_82, x - 1, yTemp, z - 1);
                                    }

                                    if (!this.field_80 && !this.field_79) {
                                        this.field_102 = this.field_101;
                                    } else {
                                        this.field_102 = block.getBrightness(this.field_82, x - 1, yTemp, z + 1);
                                    }

                                    if (!this.field_77 && !this.field_78) {
                                        this.field_105 = this.field_41;
                                    } else {
                                        this.field_105 = block.getBrightness(this.field_82, x + 1, yTemp, z - 1);
                                    }

                                    if (!this.field_80 && !this.field_78) {
                                        this.field_42 = this.field_41;
                                    } else {
                                        this.field_42 = block.getBrightness(this.field_82, x + 1, yTemp, z + 1);
                                    }

                                    ++yTemp;
                                    var9 = (this.field_102 + this.field_101 + this.field_104 + this.field_95) / 4.0F;
                                    var12 = (this.field_104 + this.field_95 + this.field_42 + this.field_41) / 4.0F;
                                    var11 = (this.field_95 + this.field_103 + this.field_41 + this.field_105) / 4.0F;
                                    var10 = (this.field_101 + this.field_100 + this.field_95 + this.field_103) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var13 ? var6 : 1.0F) * 0.5F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var13 ? var77 : 1.0F) * 0.5F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var13 ? var88 : 1.0F) * 0.5F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            } else if (texturedQuad.getSide() == BlockFaces.UP) {
                                int yTemp = y;
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_98;
                                } else {
                                    ++yTemp;
                                    this.field_44 = block.getBrightness(this.field_82, x - 1, yTemp, z);
                                    this.field_48 = block.getBrightness(this.field_82, x + 1, yTemp, z);
                                    this.field_46 = block.getBrightness(this.field_82, x, yTemp, z - 1);
                                    this.field_49 = block.getBrightness(this.field_82, x, yTemp, z + 1);
                                    if (!this.field_69 && !this.field_71) {
                                        this.field_43 = this.field_44;
                                    } else {
                                        this.field_43 = block.getBrightness(this.field_82, x - 1, yTemp, z - 1);
                                    }

                                    if (!this.field_69 && !this.field_70) {
                                        this.field_47 = this.field_48;
                                    } else {
                                        this.field_47 = block.getBrightness(this.field_82, x + 1, yTemp, z - 1);
                                    }

                                    if (!this.field_72 && !this.field_71) {
                                        this.field_45 = this.field_44;
                                    } else {
                                        this.field_45 = block.getBrightness(this.field_82, x - 1, yTemp, z + 1);
                                    }

                                    if (!this.field_72 && !this.field_70) {
                                        this.field_50 = this.field_48;
                                    } else {
                                        this.field_50 = block.getBrightness(this.field_82, x + 1, yTemp, z + 1);
                                    }

                                    --yTemp;
                                    var12 = (this.field_45 + this.field_44 + this.field_49 + this.field_98) / 4.0F;
                                    var9 = (this.field_49 + this.field_98 + this.field_50 + this.field_48) / 4.0F;
                                    var10 = (this.field_98 + this.field_46 + this.field_48 + this.field_47) / 4.0F;
                                    var11 = (this.field_44 + this.field_43 + this.field_98 + this.field_46) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = var14 ? var6 : 1.0F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = var14 ? var77 : 1.0F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = var14 ? var88 : 1.0F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            } else if (texturedQuad.getSide() == BlockFaces.WEST) {
                                int zTemp = z;
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_96;
                                } else {
                                    --zTemp;
                                    this.field_51 = block.getBrightness(this.field_82, x - 1, y, zTemp);
                                    this.field_103 = block.getBrightness(this.field_82, x, y - 1, zTemp);
                                    this.field_46 = block.getBrightness(this.field_82, x, y + 1, zTemp);
                                    this.field_52 = block.getBrightness(this.field_82, x + 1, y, zTemp);
                                    if (!this.field_73 && !this.field_77) {
                                        this.field_100 = this.field_51;
                                    } else {
                                        this.field_100 = block.getBrightness(this.field_82, x - 1, y - 1, zTemp);
                                    }

                                    if (!this.field_73 && !this.field_69) {
                                        this.field_43 = this.field_51;
                                    } else {
                                        this.field_43 = block.getBrightness(this.field_82, x - 1, y + 1, zTemp);
                                    }

                                    if (!this.field_76 && !this.field_77) {
                                        this.field_105 = this.field_52;
                                    } else {
                                        this.field_105 = block.getBrightness(this.field_82, x + 1, y - 1, zTemp);
                                    }

                                    if (!this.field_76 && !this.field_69) {
                                        this.field_47 = this.field_52;
                                    } else {
                                        this.field_47 = block.getBrightness(this.field_82, x + 1, y + 1, zTemp);
                                    }

                                    ++zTemp;
                                    var9 = (this.field_51 + this.field_43 + this.field_96 + this.field_46) / 4.0F;
                                    var10 = (this.field_96 + this.field_46 + this.field_52 + this.field_47) / 4.0F;
                                    var11 = (this.field_103 + this.field_96 + this.field_105 + this.field_52) / 4.0F;
                                    var12 = (this.field_100 + this.field_51 + this.field_103 + this.field_96) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var15 ? var6 : 1.0F) * 0.8F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var15 ? var77 : 1.0F) * 0.8F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var15 ? var88 : 1.0F) * 0.8F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            } else if (texturedQuad.getSide() == BlockFaces.EAST) {
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_99;
                                } else {
                                    ++z;
                                    this.field_53 = block.getBrightness(this.field_82, x - 1, y, z);
                                    this.field_54 = block.getBrightness(this.field_82, x + 1, y, z);
                                    this.field_104 = block.getBrightness(this.field_82, x, y - 1, z);
                                    this.field_49 = block.getBrightness(this.field_82, x, y + 1, z);
                                    if (!this.field_75 && !this.field_80) {
                                        this.field_102 = this.field_53;
                                    } else {
                                        this.field_102 = block.getBrightness(this.field_82, x - 1, y - 1, z);
                                    }

                                    if (!this.field_75 && !this.field_72) {
                                        this.field_45 = this.field_53;
                                    } else {
                                        this.field_45 = block.getBrightness(this.field_82, x - 1, y + 1, z);
                                    }

                                    if (!this.field_74 && !this.field_80) {
                                        this.field_42 = this.field_54;
                                    } else {
                                        this.field_42 = block.getBrightness(this.field_82, x + 1, y - 1, z);
                                    }

                                    if (!this.field_74 && !this.field_72) {
                                        this.field_50 = this.field_54;
                                    } else {
                                        this.field_50 = block.getBrightness(this.field_82, x + 1, y + 1, z);
                                    }

                                    --z;
                                    var9 = (this.field_53 + this.field_45 + this.field_99 + this.field_49) / 4.0F;
                                    var12 = (this.field_99 + this.field_49 + this.field_54 + this.field_50) / 4.0F;
                                    var11 = (this.field_104 + this.field_99 + this.field_42 + this.field_54) / 4.0F;
                                    var10 = (this.field_102 + this.field_53 + this.field_104 + this.field_99) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var16 ? var6 : 1.0F) * 0.8F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var16 ? var77 : 1.0F) * 0.8F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var16 ? var88 : 1.0F) * 0.8F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            } else if (texturedQuad.getSide() == BlockFaces.SOUTH) {
                                int xTemp = x;
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_94;
                                } else {
                                    --xTemp;
                                    this.field_101 = block.getBrightness(this.field_82, xTemp, y - 1, z);
                                    this.field_51 = block.getBrightness(this.field_82, xTemp, y, z - 1);
                                    this.field_53 = block.getBrightness(this.field_82, xTemp, y, z + 1);
                                    this.field_44 = block.getBrightness(this.field_82, xTemp, y + 1, z);
                                    if (!this.field_73 && !this.field_79) {
                                        this.field_100 = this.field_51;
                                    } else {
                                        this.field_100 = block.getBrightness(this.field_82, xTemp, y - 1, z - 1);
                                    }

                                    if (!this.field_75 && !this.field_79) {
                                        this.field_102 = this.field_53;
                                    } else {
                                        this.field_102 = block.getBrightness(this.field_82, xTemp, y - 1, z + 1);
                                    }

                                    if (!this.field_73 && !this.field_71) {
                                        this.field_43 = this.field_51;
                                    } else {
                                        this.field_43 = block.getBrightness(this.field_82, xTemp, y + 1, z - 1);
                                    }

                                    if (!this.field_75 && !this.field_71) {
                                        this.field_45 = this.field_53;
                                    } else {
                                        this.field_45 = block.getBrightness(this.field_82, xTemp, y + 1, z + 1);
                                    }

                                    ++xTemp;
                                    var12 = (this.field_101 + this.field_102 + this.field_94 + this.field_53) / 4.0F;
                                    var9 = (this.field_94 + this.field_53 + this.field_44 + this.field_45) / 4.0F;
                                    var10 = (this.field_51 + this.field_94 + this.field_43 + this.field_44) / 4.0F;
                                    var11 = (this.field_100 + this.field_101 + this.field_51 + this.field_94) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var17 ? var6 : 1.0F) * 0.6F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var17 ? var77 : 1.0F) * 0.6F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var17 ? var88 : 1.0F) * 0.6F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            } else if (texturedQuad.getSide() == BlockFaces.NORTH) {
                                int xTemp = x;
                                if (this.field_55 <= 0) {
                                    var9 = var10 = var11 = var12 = this.field_97;
                                } else {
                                    ++xTemp;
                                    this.field_41 = block.getBrightness(this.field_82, xTemp, y - 1, z);
                                    this.field_52 = block.getBrightness(this.field_82, xTemp, y, z - 1);
                                    this.field_54 = block.getBrightness(this.field_82, xTemp, y, z + 1);
                                    this.field_48 = block.getBrightness(this.field_82, xTemp, y + 1, z);
                                    if (!this.field_78 && !this.field_76) {
                                        this.field_105 = this.field_52;
                                    } else {
                                        this.field_105 = block.getBrightness(this.field_82, xTemp, y - 1, z - 1);
                                    }

                                    if (!this.field_78 && !this.field_74) {
                                        this.field_42 = this.field_54;
                                    } else {
                                        this.field_42 = block.getBrightness(this.field_82, xTemp, y - 1, z + 1);
                                    }

                                    if (!this.field_70 && !this.field_76) {
                                        this.field_47 = this.field_52;
                                    } else {
                                        this.field_47 = block.getBrightness(this.field_82, xTemp, y + 1, z - 1);
                                    }

                                    if (!this.field_70 && !this.field_74) {
                                        this.field_50 = this.field_54;
                                    } else {
                                        this.field_50 = block.getBrightness(this.field_82, xTemp, y + 1, z + 1);
                                    }

                                    --xTemp;
                                    var9 = (this.field_41 + this.field_42 + this.field_97 + this.field_54) / 4.0F;
                                    var12 = (this.field_97 + this.field_54 + this.field_48 + this.field_50) / 4.0F;
                                    var11 = (this.field_52 + this.field_97 + this.field_47 + this.field_48) / 4.0F;
                                    var10 = (this.field_105 + this.field_41 + this.field_52 + this.field_97) / 4.0F;
                                }

                                this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var18 ? var6 : 1.0F) * 0.6F;
                                this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var18 ? var77 : 1.0F) * 0.6F;
                                this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var18 ? var88 : 1.0F) * 0.6F;
                                this.field_56 *= var9;
                                this.field_60 *= var9;
                                this.field_64 *= var9;
                                this.field_57 *= var10;
                                this.field_61 *= var10;
                                this.field_65 *= var10;
                                this.field_58 *= var11;
                                this.field_62 *= var11;
                                this.field_66 *= var11;
                                this.field_59 *= var12;
                                this.field_63 *= var12;
                                this.field_68 *= var12;
                            }
                            //</editor-fold>
                            // TODO: Make tessellator.colour be called with correct params for each vertex.
                            // TODO: Add a base 3d model class that handles rotations.
                            tessellator.colour(this.field_56, this.field_60, this.field_64);
                            tessellator.vertex(x + (float) quadPoint.pointVector.x * 0.0625F, y + (float) quadPoint.pointVector.y * 0.0625F, z + (float) quadPoint.pointVector.z * 0.0625F, quadPoint.field_1147, quadPoint.field_1148);
                        }

                        tessellator.draw();
                    }
                }

                lastRegistry.bindAtlas(minecraft.textureManager, lastTex);
                tessellator.start();
                cir.setReturnValue(true);
                cir.cancel();
            }
        } else if (block instanceof BlockWithWorldRenderer) {
            ((BlockWithWorldRenderer) block).renderWorld((TileRenderer) (Object) this, field_82, x, y, z, field_82.getTileMeta(x, y, z));
            cir.cancel();
        }
    }

    @ModifyVariable(method = {
            "method_46(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_55(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_61(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_65(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_67(Lnet/minecraft/block/BlockBase;DDDI)V",
            "method_69(Lnet/minecraft/block/BlockBase;DDDI)V"
    }, index = 8, at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0, shift = At.Shift.BEFORE, by = 2))
    private int getTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_45(Lnet/minecraft/block/BlockBase;DDDDD)V", index = 13, at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0, shift = At.Shift.BEFORE, by = 2))
    private int getTorchTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_47(Lnet/minecraft/block/BlockBase;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0, shift = At.Shift.BEFORE, by = 2))
    private int getCrossedTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_56(Lnet/minecraft/block/BlockBase;IDDD)V", index = 10, at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0, shift = At.Shift.BEFORE, by = 2))
    private int getCropsTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_70(Lnet/minecraft/block/BlockBase;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getBrightness(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getFireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_71(Lnet/minecraft/block/BlockBase;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getBrightness(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getRedstoneWireTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_72(Lnet/minecraft/block/BlockBase;III)Z", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getBrightness(Lnet/minecraft/level/TileView;III)F", shift = At.Shift.BEFORE))
    private int getLadderTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_44(Lnet/minecraft/block/Rail;III)Z", index = 7, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Rail;method_1108()Z", shift = At.Shift.BEFORE))
    private int getRailTextureID(int texID) {
        return overrideTexture(texID);
    }

    @ModifyVariable(method = "method_75(Lnet/minecraft/block/BlockBase;III)Z", index = 28, at = @At(value = "CONSTANT", args = "intValue=15", ordinal = 0, shift = At.Shift.BEFORE, by = 2))
    private int getFluidTextureID(int texID) {
        return overrideTexture(texID);
    }

    @Redirect(method = "method_75(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I"))
    private int getFluidTextureID(BlockBase blockBase, int side, int meta) {
        return overrideTexture(blockBase.getTextureForSide(side, meta));
    }

    @Redirect(method = "method_81(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/TileView;IIII)I", ordinal = 0))
    private int getBedTextureID1(BlockBase blockBase, TileView block, int i, int j, int k, int i1) {
        return overrideTexture(blockBase.getTextureForSide(block, i, j, k, i1));
    }

    @Redirect(method = "method_81(Lnet/minecraft/block/BlockBase;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/TileView;IIII)I", ordinal = 1))
    private int getBedTextureID2(BlockBase blockBase, TileView block, int i, int j, int k, int i1) {
        return overrideTexture(blockBase.getTextureForSide(block, i, j, k, i1));
    }

    private int overrideTexture(int texID) {
        if (TextureRegistry.currentRegistry() != null) {
            int atlasID = texID / TextureRegistry.currentRegistry().texturesPerFile();
            if (TextureRegistry.currentRegistry().currentTexture() != atlasID) {
                Tessellator tessellator = Tessellator.INSTANCE;
                boolean hasColor = false;
                if (!inventory) {
                    hasColor = ((TessellatorAccessor) tessellator).getHasColour();
                    tessellator.draw();
                }
                TextureRegistry.currentRegistry().bindAtlas(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, atlasID);
                if (!inventory) {
                    tessellator.start();
                    ((TessellatorAccessor) tessellator).setHasColour(hasColor);
                }
            }
            return texID % TextureRegistry.currentRegistry().texturesPerFile();
        } else
            return texID;
    }

    @Inject(method = "method_48(Lnet/minecraft/block/BlockBase;IF)V", at = @At("HEAD"), cancellable = true)
    private void onRenderBlockInInventory(BlockBase block, int meta, float f, CallbackInfo ci) {
        inventory = true;

        if (block instanceof BlockModelProvider) {
            Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
            CustomModel model = ((BlockModelProvider) block).getCustomInventoryModel(meta);
            if (model != null) {
                GL11.glPushMatrix();

                Tessellator tessellator = Tessellator.INSTANCE;

                Tessellator.INSTANCE.colour(1.0F, 1.0F, 1.0F);
                for (CustomCuboidRenderer cuboid : model.getCuboids()) {
                    for (CustomTexturedQuad texturedQuad : cuboid.getCubeQuads()) {
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/" + cuboid.getModID() + "/" + StationAPI.MODID + "/models/textures/" + texturedQuad.getTexture() + ".png"));
                        Tessellator.INSTANCE.start();
                        for (QuadPoint var7 : texturedQuad.getQuadPoints()) {
                            if (texturedQuad.getSide() == BlockFaces.DOWN) {
                                tessellator.setNormal(0F, -1F, 0F);
                            }
                            if (texturedQuad.getSide() == BlockFaces.UP) {
                                tessellator.setNormal(0F, 1F, 0F);
                            }
                            if (texturedQuad.getSide() == BlockFaces.EAST) {
                                tessellator.setNormal(0F, 0F, -1F);
                            }
                            if (texturedQuad.getSide() == BlockFaces.WEST) {
                                tessellator.setNormal(0F, 0F, 1F);
                            }
                            if (texturedQuad.getSide() == BlockFaces.NORTH) {
                                tessellator.setNormal(-1F, 0F, 0F);
                            }
                            if (texturedQuad.getSide() == BlockFaces.SOUTH) {
                                tessellator.setNormal(1F, 0F, 0F);
                            }
                            tessellator.vertex(((float) var7.pointVector.x * 0.0625F) - .5, ((float) var7.pointVector.y * 0.0625F) - .5, ((float) var7.pointVector.z * 0.0625F) - .5, var7.field_1147, var7.field_1148);
                        }
                        Tessellator.INSTANCE.draw();
                    }
                }

                GL11.glPopMatrix();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/terrain.png"));
                inventory = false;
                ci.cancel();
            }
        } else if (block instanceof BlockWithInventoryRenderer) {
            ((BlockWithInventoryRenderer) block).renderInventory((TileRenderer) (Object) this, meta);
            inventory = false;
            ci.cancel();
        }
    }

    @Inject(method = "method_48(Lnet/minecraft/block/BlockBase;IF)V", at = @At("RETURN"))
    private void afterRenderBlockInInventory(CallbackInfo ci) {
        inventory = false;
    }
}
