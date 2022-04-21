package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Fluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.colour.item.ItemColours;
import net.modificationstation.stationapi.api.client.render.OverlayTexture;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedModelRenderer;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.ModelIdentifier;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.api.util.exception.CrashReportSectionBlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import net.modificationstation.stationapi.mixin.arsenic.TilePosAccessor;
import net.modificationstation.stationapi.mixin.arsenic.client.BlockRendererAccessor;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class BakedModelRendererImpl implements BakedModelRenderer {

    private static final Direction[] DIRECTIONS = Util.make(() -> {
        Direction[] originalValues = Direction.values();
        return Arrays.copyOf(originalValues, originalValues.length + 1);
    });

    private final LightingCalculatorImpl light = new LightingCalculatorImpl(3);
    private final Random random = new Random();
    private final TilePos pos = new TilePos(0, 0, 0);
    private final TilePosAccessor posAccessor = (TilePosAccessor) pos;
    private final ItemModels itemModels = Util.make(new ItemModels(StationRenderAPI.getBakedModelManager()), models -> {
        for (Map.Entry<Identifier, ItemBase> entry : ItemRegistry.INSTANCE)
            models.putModel(entry.getValue(), ModelIdentifier.of(entry.getKey(), "inventory"));
        models.reloadModels();
    });
    private final BlockColours blockColours = StationRenderAPI.getBlockColours();
    private final ItemColours itemColours = StationRenderAPI.getItemColours();
    private final ThreadLocal<BlockRenderContext> BLOCK_CONTEXTS = ThreadLocal.withInitial(BlockRenderContext::new);
    private final ThreadLocal<ItemRenderContext> ITEM_CONTEXTS = ThreadLocal.withInitial(() -> new ItemRenderContext(itemColours));
    private final MatrixStack matrices = new MatrixStack();
    private final BlockRenderer blockRenderer = new BlockRenderer();

    @Override
    public boolean renderBlock(BlockState state, TilePos pos, BlockView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random) {
        try {
//            BlockRenderType blockRenderType = state.getRenderType();
//            if (blockRenderType != BlockRenderType.MODEL) {
//                return false;
//            }
            return render(world, StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state), state, pos, matrices, vertexConsumer, cull, random, state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tesselated");
            CrashReportSectionBlockState.addBlockInfo(crashReportSection, world, pos, state);
            throw new CrashException(crashReport);
        }
    }

    @Override
    public boolean render(BlockView world, BakedModel model, BlockState state, TilePos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay) {
        if (state.getBlock().getRenderType() == 4) {
            ((BlockRendererAccessor) blockRenderer).stationapi$setBlockView(world);
            return renderFluid(blockRenderer, world, pos, vertexConsumer, state);
        }
        model = Objects.requireNonNull(model.getOverrides().apply(model, state, world, pos, (int) seed));
        matrices.push();
        matrices.translate(pos.x, pos.y, pos.z);
        boolean rendered = false;
        if (!model.isVanillaAdapter()) {
            rendered = BLOCK_CONTEXTS.get().render(world, model, state, pos, matrices, vertexConsumer, random, seed, -1);
        } else {
            BlockBase block = state.getBlock();
//            if (textureOverride >= 0) {
//                matrices.pop();
//                return true;
//            }
            light.initialize(
                    block,
                    world, pos.x, pos.y, pos.z,
                    Minecraft.isSmoothLightingEnabled() && model.useAmbientOcclusion()
            );
            ImmutableList<BakedQuad> qs;
            BakedQuad q;
            float[] qlight = light.light;
            for (int quadSet = 0, size = DIRECTIONS.length; quadSet < size; quadSet++) {
                Direction face = DIRECTIONS[quadSet];
                random.setSeed(seed);
                qs = model.getQuads(state, face, random);
                if (!qs.isEmpty() && (face == null || block.isSideRendered(world, pos.x + face.vector.x, pos.y + face.vector.y, pos.z + face.vector.z, quadSet))) {
                    rendered = true;
                    for (int j = 0, quadSize = qs.size(); j < quadSize; j++) {
                        q = qs.get(j);
                        light.calculateForQuad(q);
                        renderQuad(world, state, pos, vertexConsumer, matrices.peek(), q, qlight, -1);
                    }
                }
            }
        }
        matrices.pop();
        return rendered;
    }

    private void renderQuad(BlockView world, BlockState state, TilePos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightness, int overlay) {
        float h;
        float g;
        float f;
        if (quad.hasColour()) {
            int i = blockColours.getColour(state, world, pos, quad.getColorIndex());
            f = (float)(i >> 16 & 0xFF) / 255.0f;
            g = (float)(i >> 8 & 0xFF) / 255.0f;
            h = (float)(i & 0xFF) / 255.0f;
        } else {
            f = 1.0f;
            g = 1.0f;
            h = 1.0f;
        }
        vertexConsumer.quad(matrixEntry, quad, brightness, f, g, h, new int[]{0, 0, 0, 0}, overlay, true);
    }

    private boolean renderFluid(BlockRenderer blockRenderer, BlockView world, TilePos pos, VertexConsumer vertexConsumer, BlockState state) {
        BlockRendererAccessor blockRendererAccessor = (BlockRendererAccessor) blockRenderer;
        int x = pos.x;
        int y = pos.y;
        int z = pos.z;
        BlockBase block = state.getBlock();
        SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);

        int var6 = (block.id == BlockBase.FLOWING_WATER.id || block.id == BlockBase.STILL_WATER.id) && Atlases.getTerrain().getTexture(block.getTextureForSide(0)).getSprite().getAnimation() != null ? StationRenderAPI.getBlockColours().getColour(((BlockStateView) world).getBlockState(x, y, z), world, new TilePos(x, y, z), -1) : block.getColourMultiplier(world, x, y, z);
        float var7 = (float)((var6 >> 16) & 255) / 255.0F;
        float var8 = (float)((var6 >> 8) & 255) / 255.0F;
        float var9 = (float)(var6 & 255) / 255.0F;
        boolean var10 = block.isSideRendered(world, x, y + 1, z, 1);
        boolean var11 = block.isSideRendered(world, x, y - 1, z, 0);
        boolean[] var12 = new boolean[] {
                block.isSideRendered(world, x, y, z - 1, 2),
                block.isSideRendered(world, x, y, z + 1, 3),
                block.isSideRendered(world, x - 1, y, z, 4),
                block.isSideRendered(world, x + 1, y, z, 5)
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
            int var23 = world.getTileMeta(x, y, z);
            float var24 = blockRendererAccessor.stationapi$method_43(x, y, z, var22);
            float var25 = blockRendererAccessor.stationapi$method_43(x, y, z + 1, var22);
            float var26 = blockRendererAccessor.stationapi$method_43(x + 1, y, z + 1, var22);
            float var27 = blockRendererAccessor.stationapi$method_43(x + 1, y, z, var22);
            if (blockRendererAccessor.getRenderAllSides() || var10) {
                var13 = true;
                Sprite var28 = atlas.getSprite(Atlases.getTerrain().getTexture(block.getTextureForSide(1, var23)).getId());
                float notchCode = 1;
                float var29 = (float) Fluid.method_1223(world, x, y, z, var22);
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
                float var38 = block.getBrightness(world, x, y, z);
                float r = var15 * var38 * var7;
                float g = var15 * var38 * var8;
                float b = var15 * var38 * var9;
                vertex(vertexConsumer, x, y + var24, z, r, g, b, (float) (var32 - uc - us), (float) (var34 - vc + vs));
                vertex(vertexConsumer, x, y + var25, z + 1, r, g, b, (float) (var32 - uc + us), (float) (var34 + vc + vs));
                vertex(vertexConsumer, x + 1, y + var26, z + 1, r, g, b, (float) (var32 + uc + us), (float) (var34 + vc - vs));
                vertex(vertexConsumer, x + 1, y + var27, z, r, g, b, (float) (var32 + uc - us), (float) (var34 - vc - vs));
            }

            if (blockRendererAccessor.getRenderAllSides() || var11) {
                float var52 = block.getBrightness(world, x, y - 1, z);
                float colour = var14 * var52;
                Sprite sprite = Atlases.getTerrain().getTexture(block.getTextureForSide(0)).getSprite();
                blockRenderer.renderBottomFace(block, x, y, z, block.getTextureForSide(0));
                vertex(vertexConsumer, x, y, z + 1, colour, colour, colour, sprite.getMinU(), sprite.getMaxV());
                vertex(vertexConsumer, x, y, z, colour, colour, colour, sprite.getMinU(), sprite.getMinV());
                vertex(vertexConsumer, x + 1, y, z, colour, colour, colour, sprite.getMaxU(), sprite.getMinV());
                vertex(vertexConsumer, x + 1, y, z + 1, colour, colour, colour, sprite.getMaxU(), sprite.getMaxV());
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
                    float var51 = block.getBrightness(world, var54, y, var55);
                    if (var53 < 2) {
                        var51 *= var16;
                    } else {
                        var51 *= var17;
                    }

                    float r = var15 * var51 * var7;
                    float g = var15 * var51 * var8;
                    float b = var15 * var51 * var9;
                    vertex(vertexConsumer, var59, (float)y + var35, var60, r, g, b, (float) var41, (float) var45);
                    vertex(vertexConsumer, var39, (float)y + var58, var40, r, g, b, (float) var43, (float) var47);
                    vertex(vertexConsumer, var39, y, var40, r, g, b, (float) var43, (float) var49);
                    vertex(vertexConsumer, var59, y, var60, r, g, b, (float) var41, (float) var49);
                }
            }

            block.minY = var18;
            block.maxY = var20;
            return var13;
        }
    }

    private void vertex(VertexConsumer vertexConsumer, double x, double y, double z, float red, float green, float blue, float u, float v) {
//        vertexConsumer.vertex(x, y, z).colour(red, green, blue, 1.0f).texture(u, v).light(0).normal(0.0f, 1.0f, 0.0f).next();
        vertexConsumer.vertex(x, y, z).texture(u, v).colour(red, green, blue, 1.0f).normal(0.0f, 1.0f, 0.0f).next();
    }

    private float redI2F(int colour) {
        return ((colour >> 16) & 255) / 255F;
    }

    private float greenI2F(int colour) {
        return ((colour >> 8) & 255) / 255F;
    }

    private float blueI2F(int colour) {
        return (colour & 255) / 255F;
    }

    private int colourF2I(float r, float g, float b) {
        return (255 << 24) | (colourChannelF2I(r) << 16) | (colourChannelF2I(g) << 8) | colourChannelF2I(b);
    }

    private int colourChannelF2I(float colourChannel) {
        return Ints.constrainToRange((int) (colourChannel * 255), 0, 255);
    }

    @Override
    public ItemModels getItemModels() {
        return this.itemModels;
    }

    private void renderBakedItemModel(BakedModel model, ItemInstance stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
        Random random = new Random();
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            this.renderBakedItemQuads(matrices, vertices, model.getQuads(null, direction, random), stack, light, overlay);
        }
        random.setSeed(42L);
        this.renderBakedItemQuads(matrices, vertices, model.getQuads(null, null, random), stack, light, overlay);
    }

    @Override
    public void renderItem(ItemInstance stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, BakedModel model) {
        if (stack == null || stack.itemId == 0 || stack.count < 1) return;
        if (model.isVanillaAdapter()) {
            matrices.push();
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5, -0.5, -0.5);
            if (!model.isBuiltin())
                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            matrices.pop();
        } else {
            ITEM_CONTEXTS.get().renderModel(stack, renderMode, leftHanded, matrices, vertexConsumer, light, overlay, model, this::renderBakedItemModel);
        }
    }

    private void renderBakedItemQuads(MatrixStack matrices, VertexConsumer vertices, List<BakedQuad> quads, ItemInstance stack, int light, int overlay) {
        boolean bl = stack != null && stack.itemId != 0 && stack.count > 0;
        MatrixStack.Entry entry = matrices.peek();
        for (BakedQuad bakedQuad : quads) {
            int i = bl && bakedQuad.hasColour() ? this.itemColours.getColour(stack, bakedQuad.getColorIndex()) : -1;
            float f = (float)(i >> 16 & 0xFF) / 255.0f;
            float g = (float)(i >> 8 & 0xFF) / 255.0f;
            float h = (float)(i & 0xFF) / 255.0f;
            vertices.quad(entry, bakedQuad, f, g, h, light, overlay);
        }
    }

    @Override
    public BakedModel getModel(ItemInstance stack, @Nullable Level world, @Nullable Living entity, int seed) {
        BakedModel bakedModel = this.itemModels.getModel(stack);
        ClientLevel clientWorld = world instanceof ClientLevel ? (ClientLevel) world : null;
        BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
        return bakedModel2 == null ? this.itemModels.getModelManager().getMissingModel() : bakedModel2;
    }

    @Override
    public void renderItem(ItemInstance stack, ModelTransformation.Mode transformationType, int light, int overlay, MatrixStack matrices, VertexConsumer vertexConsumer, int seed) {
        this.renderItem(null, stack, transformationType, false, matrices, vertexConsumer, null, light, overlay, seed);
    }

    @Override
    public void renderItem(@Nullable Living entity, ItemInstance item, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumer vertexConsumer, @Nullable Level world, int light, int overlay, int seed) {
        if (item == null || item.itemId == 0 || item.count < 1) return;
        BakedModel bakedModel = this.getModel(item, world, entity, seed);
        this.renderItem(item, renderMode, leftHanded, matrices, vertexConsumer, light, overlay, bakedModel);
    }

    @Override
    public void renderGuiItemIcon(ItemInstance stack, int x, int y) {
        this.renderGuiItemModel(stack, x, y, this.getModel(stack, null, null, 0));
    }

    protected void renderGuiItemModel(ItemInstance stack, int x, int y, BakedModel model) {
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).setFilter(false, false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        matrices.push();
        matrices.translate(x, y, 100.0f/* + this.zOffset*/);
        matrices.translate(8.0, 8.0, 0.0);
        matrices.scale(1.0f, -1.0f, 1.0f);
        matrices.scale(16.0f, 16.0f, 16.0f);
        boolean bl = !model.isSideLit();
        if (bl) {
            RenderHelper.disableLighting();
        }
        Tessellator.INSTANCE.start();
        this.renderItem(stack, ModelTransformation.Mode.GUI, false, matrices, StationTessellator.get(Tessellator.INSTANCE), -1/*LightmapTextureManager.MAX_LIGHT_COORDINATE*/, -1/*OverlayTexture.DEFAULT_UV*/, model);
        Tessellator.INSTANCE.draw();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (bl) {
            RenderHelper.enableLighting();
        }
        matrices.pop();
    }

    /**
     * Renders an item in a GUI with the player as the attached entity
     * for calculating model overrides.
     */
    @Override
    public void renderInGuiWithOverrides(ItemInstance stack, int x, int y) {
        //noinspection deprecation
        this.innerRenderInGui(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, stack, x, y, 0);
    }

    public void renderInGuiWithOverrides(ItemInstance stack, int x, int y, int seed) {
        //noinspection deprecation
        this.innerRenderInGui(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, stack, x, y, seed);
    }

    public void renderInGuiWithOverrides(ItemInstance stack, int x, int y, int seed, int depth) {
        //noinspection deprecation
        this.innerRenderInGui(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, stack, x, y, seed, depth);
    }

    /**
     * Renders an item in a GUI without an attached entity.
     */
    public void renderInGui(ItemInstance stack, int x, int y) {
        this.innerRenderInGui(null, stack, x, y, 0);
    }

    /**
     * Renders an item in a GUI with an attached entity.
     *
     * <p>The entity is used to calculate model overrides for the item.
     */
    public void renderInGuiWithOverrides(Living entity, ItemInstance stack, int x, int y, int seed) {
        this.innerRenderInGui(entity, stack, x, y, seed);
    }

    private void innerRenderInGui(@Nullable Living entity, ItemInstance stack, int x, int y, int seed) {
        this.innerRenderInGui(entity, stack, x, y, seed, 0);
    }

    private void innerRenderInGui(@Nullable Living entity, ItemInstance itemStack, int x, int y, int seed, int depth) {
        if (itemStack == null || itemStack.itemId == 0 || itemStack.count < 1) return;
        BakedModel bakedModel = this.getModel(itemStack, null, entity, seed);
//        this.zOffset = bakedModel.hasDepth() ? this.zOffset + 50.0f + (float)depth : this.zOffset + 50.0f;
        try {
            this.renderGuiItemModel(itemStack, x, y, bakedModel);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(itemStack.getType()));
            crashReportSection.add("Item Damage", () -> String.valueOf(itemStack.getDamage()));
            crashReportSection.add("Item NBT", () -> String.valueOf(StationNBT.cast(itemStack).getStationNBT()));
            throw new CrashException(crashReport);
        }
//        this.zOffset = bakedModel.hasDepth() ? this.zOffset - 50.0f - (float)depth : this.zOffset - 50.0f;
    }
}
