package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.level.ClientLevel;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.color.item.ItemColors;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.*;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.exception.CrashException;
import net.modificationstation.stationapi.api.util.exception.CrashReport;
import net.modificationstation.stationapi.api.util.exception.CrashReportSection;
import net.modificationstation.stationapi.api.util.exception.CrashReportSectionBlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.aocalc.LightingCalculatorImpl;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BakedModelRendererImpl<T extends Tessellator & StationTessellator> implements BakedModelRenderer {

    private static final Direction[] DIRECTIONS = Util.make(() -> {
        Direction[] originalValues = Direction.values();
        return Arrays.copyOf(originalValues, originalValues.length + 1);
    });

    @SuppressWarnings("unchecked")
    private final T tessellator = (T) Tessellator.INSTANCE;
    private final LightingCalculatorImpl light = new LightingCalculatorImpl(3);
    private final Random random = new Random();
    private final ItemModels itemModels = Util.make(new ItemModels(StationRenderAPI.getBakedModelManager()), models -> {
        for (Identifier id : ItemRegistry.INSTANCE.getIds())
            models.putModel(ItemRegistry.INSTANCE.get(id), ModelIdentifier.of(id, "inventory"));
        models.reloadModels();
    });
    private final BlockColors blockColors = StationRenderAPI.getBlockColors();
    private final ItemColors itemColors = StationRenderAPI.getItemColors();
    private final ThreadLocal<BlockRenderContext> BLOCK_CONTEXTS = ThreadLocal.withInitial(BlockRenderContext::new);
    private final ThreadLocal<ItemRenderContext> ITEM_CONTEXTS = ThreadLocal.withInitial(() -> new ItemRenderContext(itemColors));
    private boolean damage;

    @Override
    public boolean renderBlock(BlockState state, TilePos pos, BlockView world, boolean cull, Random random) {
        try {
//            BlockRenderType blockRenderType = state.getRenderType();
//            if (blockRenderType != BlockRenderType.MODEL) {
//                return false;
//            }
            return render(world, StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state), state, pos, cull, random, state.getRenderingSeed(pos));
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block in world");
            CrashReportSection crashReportSection = crashReport.addElement("Block being tesselated");
            CrashReportSectionBlockState.addBlockInfo(crashReportSection, world, pos, state);
            throw new CrashException(crashReport);
        }
    }

    @Override
    public boolean render(BlockView world, BakedModel model, BlockState state, TilePos pos, boolean cull, Random random, long seed) {
        boolean rendered = false;
        model = Objects.requireNonNull(model.getOverrides().apply(model, state, world, pos, (int) seed));
        if (!model.isVanillaAdapter()) {
            rendered = BLOCK_CONTEXTS.get().render(world, model, state, pos, random, seed);
        } else {
            BlockBase block = state.getBlock();
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
                if (!qs.isEmpty() && (face == null || block.isSideRendered(world, pos.x + face.getOffsetX(), pos.y + face.getOffsetY(), pos.z + face.getOffsetZ(), quadSet))) {
                    rendered = true;
                    for (int j = 0, quadSize = qs.size(); j < quadSize; j++) {
                        q = qs.get(j);
                        light.calculateForQuad(q);
                        renderQuad(world, state, pos, q, qlight);
                    }
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
        return (255 << 24) | (colorChannelF2I(r) << 16) | (colorChannelF2I(g) << 8) | colorChannelF2I(b);
    }

    private int colorChannelF2I(float colorChannel) {
        return Ints.constrainToRange((int) (colorChannel * 255), 0, 255);
    }

    @Override
    public void renderDamage(BlockState state, TilePos pos, BlockView world, float progress) {
//        if (state.getRenderType() != BlockRenderType.MODEL) {
//            return;
//        }
        BakedModel bakedModel = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(state);
        long l = state.getRenderingSeed(pos);
        damage = true;
        //noinspection deprecation
        StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager).getTexture(ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get((int) (progress * 10))).bindTexture();
        render(world, bakedModel, state, pos, true, this.random, l);
        damage = false;
    }

    private void renderQuad(BlockView world, BlockState state, TilePos pos, BakedQuad quad, float[] brightness) {
        if (quad.hasColor()) {
            int i = blockColors.getColor(state, world, pos, quad.getColorIndex());
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

    private void renderBakedItemModel(BakedModel model, ItemInstance stack, float brightness) {
        Random random = new Random();
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            this.renderBakedItemQuads(model.getQuads(null, direction, random), stack, brightness);
        }
        random.setSeed(42L);
        this.renderBakedItemQuads(model.getQuads(null, null, random), stack, brightness);
    }

    @Override
    public void renderItem(ItemInstance stack, ModelTransformation.Mode renderMode, float brightness, BakedModel model) {
        if (stack == null || stack.itemId == 0 || stack.count < 1) return;
        if (model.isVanillaAdapter()) {
            model.getTransformation().getTransformation(renderMode).apply();
            if (model.isSideLit() && renderMode == ModelTransformation.Mode.GUI) // kind of a dirty way to do this, should probably look into replacing
                GL11.glRotatef(90, 0, 1, 0);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            if (!model.isBuiltin())
                this.renderBakedItemModel(model, stack, brightness);
        } else {
            ITEM_CONTEXTS.get().renderModel(stack, renderMode, model, this::renderBakedItemModel);
        }
    }

    private void renderBakedItemQuads(List<BakedQuad> quads, ItemInstance stack, float brightness) {
        boolean bl = stack != null && stack.itemId != 0 && stack.count > 0;
        for (BakedQuad bakedQuad : quads) {
            int i = bl && bakedQuad.hasColor() ? this.itemColors.getColor(stack, bakedQuad.getColorIndex()) : -1;
            i = colorF2I(redI2F(i) * brightness, greenI2F(i) * brightness, blueI2F(i) * brightness);
            Direction face = bakedQuad.getFace();
            tessellator.quad(bakedQuad, 0, 0, 0, i, i, i, i, face.getOffsetX(), face.getOffsetY(), face.getOffsetZ(), false);
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
    public void renderItem(@Nullable Living entity, ItemInstance item, ModelTransformation.Mode renderMode, @Nullable Level world, float brightness, int seed) {
        if (item == null || item.itemId == 0 || item.count < 1) return;
        BakedModel bakedModel = this.getModel(item, world, entity, seed);
        this.renderItem(item, renderMode, brightness, bakedModel);
    }

    protected void renderGuiItemModel(ItemInstance stack, int x, int y, BakedModel model) {
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).setFilter(false, false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 100);
        GL11.glTranslatef(8, 8, 0);
        GL11.glScalef(1, -1, 1);
        GL11.glScalef(16, 16, 16);
        boolean bl = !model.isSideLit();
        if (bl) {
            GL11.glDisable(2896);
        }
        tessellator.start();
        this.renderItem(stack, ModelTransformation.Mode.GUI, 1, model);
        tessellator.draw();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (bl) {
            GL11.glEnable(2896);
        }
        GL11.glPopMatrix();
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

    private void innerRenderInGui(@Nullable Living entity, ItemInstance stack, int x, int y, int seed) {
        if (stack == null || stack.itemId == 0 || stack.count < 1) return;
        BakedModel bakedModel = this.getModel(stack, null, entity, seed);
        try {
            this.renderGuiItemModel(stack, x, y, bakedModel);
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Rendering item");
            CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
            crashReportSection.add("Item Type", () -> String.valueOf(stack.getType()));
            crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
            crashReportSection.add("Item NBT", () -> String.valueOf(StationNBT.class.cast(stack).getStationNBT()));
            throw new CrashException(crashReport);
        }
    }
}