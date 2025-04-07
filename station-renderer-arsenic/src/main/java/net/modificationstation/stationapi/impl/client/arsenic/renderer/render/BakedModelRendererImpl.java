package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.class_454;
import net.minecraft.class_583;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.color.item.ItemColors;
import net.modificationstation.stationapi.api.client.render.Renderer;
import net.modificationstation.stationapi.api.client.render.StateManager;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.*;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.render.model.json.Transformation;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.crash.CrashException;
import net.modificationstation.stationapi.api.util.crash.CrashReport;
import net.modificationstation.stationapi.api.util.crash.CrashReportSection;
import net.modificationstation.stationapi.api.util.exception.CrashReportSectionBlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BakedModelRendererImpl implements BakedModelRenderer {

    private static final Direction[] DIRECTIONS = Util.make(() -> {
        Direction[] originalValues = Direction.values();
        return Arrays.copyOf(originalValues, originalValues.length + 1);
    });

    private final Tessellator tessellator = Tessellator.INSTANCE;
    private final LightingCalculatorImpl light = new LightingCalculatorImpl(3);
    private final Random random = new Random();
    private final ItemModels itemModels = Util.make(new ItemModels(StationRenderAPI.getBakedModelManager()), models -> {
        for (Identifier id : ItemRegistry.INSTANCE.getIds())
            models.putModel(ItemRegistry.INSTANCE.get(id), ModelIdentifier.of(id, "inventory"));
        models.reloadModels();
    });
    private final BlockColors blockColors = StationRenderAPI.getBlockColors();
    private final ItemColors itemColors = StationRenderAPI.getItemColors();
    private boolean damage;

    @Override
    public boolean renderBlock(VertexConsumer consumer, BlockState state, BlockPos pos, BlockView world, boolean cull, Random random) {
        try {
//            BlockRenderType blockRenderType = state.getRenderType();
//            if (blockRenderType != BlockRenderType.MODEL) {
//                return false;
//            }
            return render(consumer, world, StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state), state, pos, cull, random, state.getRenderingSeed(pos));
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tesselated");
            CrashReportSectionBlockState.addBlockInfo(crashReportSection, world, pos, state);
            throw new CrashException(crashReport);
        }
    }

    @Override
    public boolean render(VertexConsumer consumer, BlockView world, BakedModel model, BlockState state, BlockPos pos, boolean cull, Random random, long seed) {
        boolean rendered = false;
        model = Objects.requireNonNull(model.getOverrides().apply(model, state, world, pos, (int) seed));
        Block block = state.getBlock();
        light.initialize(
                block,
                world, pos.x, pos.y, pos.z,
                Minecraft.method_2148() && model.useAmbientOcclusion()
        );
        ImmutableList<BakedQuad> qs;
        BakedQuad q;
        float[] qlight = light.light;
        for (int quadSet = 0, size = DIRECTIONS.length; quadSet < size; quadSet++) {
            Direction face = DIRECTIONS[quadSet];
            random.setSeed(seed);
            qs = model.getQuads(state, face, random);
            if (!qs.isEmpty() && (face == null || block.isSideVisible(world, pos.x + face.getOffsetX(), pos.y + face.getOffsetY(), pos.z + face.getOffsetZ(), quadSet))) {
                rendered = true;
                for (int j = 0, quadSize = qs.size(); j < quadSize; j++) {
                    q = qs.get(j);
                    light.calculateForQuad(q);
                    renderQuad(null, consumer, world, state, pos, q, qlight);
                }
            }
        }
        return rendered;
    }

    private float redI2F(int color) {
        return ((color >> 16) & 255) / 255F;
    }

    private float greenI2F(int color) {
        return ((color >> 8) & 255) / 255F;
    }

    private float blueI2F(int color) {
        return (color & 255) / 255F;
    }

    private int colorF2I(float r, float g, float b) {
        final int ri = colorChannelF2I(r), gi = colorChannelF2I(g), bi = colorChannelF2I(b);
        return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ?
                (0xFF << 24) | (bi << 16) | (gi << 8) | ri :
                (ri << 24) | (gi << 16) | (bi << 8) | 0xFF;
    }

    private int colorChannelF2I(float colorChannel) {
        return Ints.constrainToRange((int) (colorChannel * 255), 0, 255);
    }

    @Override
    public void renderDamage(VertexConsumer consumer, BlockState state, BlockPos pos, BlockView world, float progress) {
//        if (state.getRenderType() != BlockRenderType.MODEL) {
//            return;
//        }
        BakedModel bakedModel = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state);
        long l = state.getRenderingSeed(pos);
        damage = true;
        //noinspection deprecation
        StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getTexture(ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get((int) (progress * 10))).bindTexture();
        render(consumer, world, bakedModel, state, pos, true, this.random, l);
        damage = false;
    }

    private void renderQuad(MatrixStack.Entry entry, VertexConsumer consumer, BlockView world, BlockState state, BlockPos pos, BakedQuad quad, float[] brightness) {
        if (quad.hasTint()) {
            int i = blockColors.getColor(state, world, pos, quad.tintIndex());
            float
                    r = redI2F(i),
                    g = greenI2F(i),
                    b = blueI2F(i);
            tessellator.quad(quad, pos.x, pos.y, pos.z,
                    colorF2I(r * brightness[0], g * brightness[0], b * brightness[0]),
                    colorF2I(r * brightness[1], g * brightness[1], b * brightness[1]),
                    colorF2I(r * brightness[2], g * brightness[2], b * brightness[2]),
                    colorF2I(r * brightness[3], g * brightness[3], b * brightness[3]),
                    0, 0, 0,
                    damage
            );
        } else
            tessellator.quad(quad, pos.x, pos.y, pos.z,
                    colorF2I(brightness[0], brightness[0], brightness[0]),
                    colorF2I(brightness[1], brightness[1], brightness[1]),
                    colorF2I(brightness[2], brightness[2], brightness[2]),
                    colorF2I(brightness[3], brightness[3], brightness[3]),
                    0, 0, 0,
                    damage
            );
    }

    @Override
    public ItemModels getItemModels() {
        return this.itemModels;
    }

    private void renderBakedItemModel(BakedModel model, ItemStack stack, float brightness) {
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderBakedItemQuads(model.getQuads(null, direction, random), stack, brightness);
        }
        random.setSeed(42L);
        renderBakedItemQuads(model.getQuads(null, null, random), stack, brightness);
    }

    private void renderBakedItemModelFlat(BakedModel model, ItemStack stack, float brightness) {
        random.setSeed(42L);
        boolean bl = stack != null && stack.itemId != 0 && stack.count > 0;
        for (BakedQuad bakedQuad : model.getQuads(null, null, random)) {
            if (bakedQuad.face() != Direction.WEST) continue;
            int i = bl && bakedQuad.hasTint() ? this.itemColors.getColor(stack, bakedQuad.tintIndex()) : -1;
            float light = MathHelper.lerp(bakedQuad.lightEmission(), brightness, 1F);
            i = colorF2I(redI2F(i) * light, greenI2F(i) * light, blueI2F(i) * light);
            tessellator.quad(bakedQuad, 0, 0, 0, i, i, i, i, 0, 1, 0, false);
        }
    }

    @Override
    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, float brightness, BakedModel model) {
        if (stack == null || stack.itemId == 0) return;
        StateManager states = Renderer.get().stateManager();
        Transformation transformation = model.getTransformation().getTransformation(renderMode);
        transformation.apply();
        boolean side = model.isSideLit();
        if (side && renderMode == ModelTransformation.Mode.GUI) {
            float angle = transformation.rotation.y() - 315;
            if (angle != 0) {
                class_583.method_1927();
                states.pushMatrix();
                states.rotate(angle, 0, 1, 0);
                class_583.method_1930();
                states.popMatrix();
            }
        }
        states.translate(-0.5F, -0.5F, -0.5F);
        if (model.isBuiltin()) return;
        if (!side && renderMode == ModelTransformation.Mode.GROUND)
            renderBakedItemModelFlat(model, stack, brightness);
        else
            renderBakedItemModel(model, stack, brightness);
    }

    private void renderBakedItemQuads(List<BakedQuad> quads, ItemStack stack, float brightness) {
        boolean bl = stack != null && stack.itemId != 0 && stack.count > 0;
        for (BakedQuad bakedQuad : quads) {
            int i = bl && bakedQuad.hasTint() ? this.itemColors.getColor(stack, bakedQuad.tintIndex()) : -1;
            float light = MathHelper.lerp(bakedQuad.lightEmission(), brightness, 1F);
            i = colorF2I(redI2F(i) * light, greenI2F(i) * light, blueI2F(i) * light);
            Direction face = bakedQuad.face();
            tessellator.quad(bakedQuad, 0, 0, 0, i, i, i, i, face.getOffsetX(), face.getOffsetY(), face.getOffsetZ(), false);
        }
    }

    @Override
    public BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {
        BakedModel bakedModel = this.itemModels.getModel(stack);
        class_454 clientWorld = world instanceof class_454 ? (class_454) world : null;
        BakedModel bakedModel2 = bakedModel.getOverrides().apply(bakedModel, stack, clientWorld, entity, seed);
        return bakedModel2 == null ? this.itemModels.getModelManager().getMissingModel() : bakedModel2;
    }

    @Override
    public void renderItem(@Nullable LivingEntity entity, ItemStack item, ModelTransformation.Mode renderMode, @Nullable World world, float brightness, int seed) {
        if (item == null || item.itemId == 0 || item.count < 1) return;
        BakedModel bakedModel = this.getModel(item, world, entity, seed);
        this.renderItem(item, renderMode, brightness, bakedModel);
    }

    protected void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model) {
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).setFilter(false, false);
        StateManager states = Renderer.get().stateManager();
        states.pushMatrix();
        states.translate(x, y, 14.5F /* approximate. should probably be replaced later with a value properly calculated against vanilla's transformations */);
        states.translate(8, 8, 0);
        states.scale(1, -1, 1);
        states.scale(16, 16, 16);
        boolean flat = !model.isSideLit();
        if (flat) states.disableLighting();
        tessellator.startQuads();
        this.renderItem(stack, ModelTransformation.Mode.GUI, 1, model);
        tessellator.draw();
        if (flat) states.enableLighting();
        states.popMatrix();
        if (!flat) {
            class_583.method_1927();
            states.pushMatrix();
            states.rotate(120.0f, 1.0f, 0.0f, 0.0f);
            class_583.method_1930();
            states.popMatrix();
        }
        states.disableCull();
    }

    /**
     * Renders an item in a GUI with the player as the attached entity
     * for calculating model overrides.
     */
    @Override
    public void renderInGuiWithOverrides(ItemStack stack, int x, int y) {
        //noinspection deprecation
        this.innerRenderInGui(((Minecraft) FabricLoader.getInstance().getGameInstance()).player, stack, x, y, 0);
    }

    private void innerRenderInGui(@Nullable LivingEntity entity, ItemStack stack, int x, int y, int seed) {
        if (stack == null || stack.itemId == 0 || stack.count < 1) return;
        BakedModel bakedModel = this.getModel(stack, null, entity, seed);
        try {
            this.renderGuiItemModel(stack, x, y, bakedModel);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
            crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
            crashReportSection.add("Item NBT", () -> String.valueOf(stack.getStationNbt()));
            throw new CrashException(crashReport);
        }
    }
}
