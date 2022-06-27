package net.modificationstation.stationapi.impl.client.texture;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.gl.Program;
import net.modificationstation.stationapi.api.client.render.Shader;
import net.modificationstation.stationapi.api.client.render.VertexFormats;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.resource.ResourceFactory;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.client.resource.ResourceReloader;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRenderImpl {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("StationRenderer|API")
    public static final Logger LOGGER = Null.get();

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(StationAPI.MODID + ".debug.export_atlases", "false"));

    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);

    public static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

    private static final Map<String, Shader> shaders = new HashMap<>();
    @Nullable
    private static Shader positionShader;
    @Nullable
    private static Shader positionColorShader;
    @Nullable
    private static Shader positionColorTexShader;
    @Nullable
    private static Shader positionTexShader;
    @Nullable
    private static Shader positionTexColorShader;
    @Nullable
    private static Shader blockShader;
    @Nullable
    private static Shader newEntityShader;
    @Nullable
    private static Shader particleShader;
    @Nullable
    private static Shader positionColorLightmapShader;
    @Nullable
    private static Shader positionColorTexLightmapShader;
    @Nullable
    private static Shader positionTexColorNormalShader;
    @Nullable
    private static Shader positionTexLightmapColorShader;
    @Nullable
    private static Shader renderTypeSolidShader;
    @Nullable
    private static Shader renderTypeCutoutMippedShader;
    @Nullable
    private static Shader renderTypeCutoutShader;
    @Nullable
    private static Shader renderTypeTranslucentShader;
    @Nullable
    private static Shader renderTypeTranslucentMovingBlockShader;
    @Nullable
    private static Shader renderTypeTranslucentNoCrumblingShader;
    @Nullable
    private static Shader renderTypeArmorCutoutNoCullShader;
    @Nullable
    private static Shader renderTypeEntitySolidShader;
    @Nullable
    private static Shader renderTypeEntityCutoutShader;
    @Nullable
    private static Shader renderTypeEntityCutoutNoNullShader;
    @Nullable
    private static Shader renderTypeEntityCutoutNoNullZOffsetShader;
    @Nullable
    private static Shader renderTypeItemEntityTranslucentCullShader;
    @Nullable
    private static Shader renderTypeEntityTranslucentCullShader;
    @Nullable
    private static Shader renderTypeEntityTranslucentShader;
    @Nullable
    private static Shader renderTypeEntitySmoothCutoutShader;
    @Nullable
    private static Shader renderTypeBeaconBeamShader;
    @Nullable
    private static Shader renderTypeEntityDecalShader;
    @Nullable
    private static Shader renderTypeEntityNoOutlineShader;
    @Nullable
    private static Shader renderTypeEntityShadowShader;
    @Nullable
    private static Shader renderTypeEntityAlphaShader;
    @Nullable
    private static Shader renderTypeEyesShader;
    @Nullable
    private static Shader renderTypeEnergySwirlShader;
    @Nullable
    private static Shader renderTypeLeashShader;
    @Nullable
    private static Shader renderTypeWaterMaskShader;
    @Nullable
    private static Shader renderTypeOutlineShader;
    @Nullable
    private static Shader renderTypeArmorGlintShader;
    @Nullable
    private static Shader renderTypeArmorEntityGlintShader;
    @Nullable
    private static Shader renderTypeGlintTranslucentShader;
    @Nullable
    private static Shader renderTypeGlintShader;
    @Nullable
    private static Shader renderTypeGlintDirectShader;
    @Nullable
    private static Shader renderTypeEntityGlintShader;
    @Nullable
    private static Shader renderTypeEntityGlintDirectShader;
    @Nullable
    private static Shader renderTypeTextShader;
    @Nullable
    private static Shader renderTypeTextIntensityShader;
    @Nullable
    private static Shader renderTypeTextSeeThroughShader;
    @Nullable
    private static Shader renderTypeTextIntensitySeeThroughShader;
    @Nullable
    private static Shader renderTypeLightningShader;
    @Nullable
    private static Shader renderTypeTripwireShader;
    @Nullable
    private static Shader renderTypeEndPortalShader;
    @Nullable
    private static Shader renderTypeEndGatewayShader;
    @Nullable
    private static Shader renderTypeLinesShader;
    @Nullable
    private static Shader renderTypeCrumblingShader;

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        TERRAIN = new ExpandableAtlas(Atlases.GAME_ATLAS_TEXTURE);
        GUI_ITEMS = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
        //noinspection deprecation
        loadShaders((ResourceFactory) ((Minecraft) FabricLoader.getInstance().getGameInstance()).texturePackManager.texturePack);
    }

    public static void loadShaders(ResourceFactory manager) {
        RenderSystem.assertOnRenderThread();
        List<Program> list = Lists.newArrayList();
        list.addAll(Program.Type.FRAGMENT.getProgramCache().values());
        list.addAll(Program.Type.VERTEX.getProgramCache().values());
        list.forEach(Program::release);
        List<Pair<Shader, Consumer<Shader>>> list2 = new ArrayList<>(shaders.size());

        try {
            list2.add(Pair.of(new Shader(manager, "block", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader) -> blockShader = shader));
            list2.add(Pair.of(new Shader(manager, "new_entity", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> newEntityShader = shader));
            list2.add(Pair.of(new Shader(manager, "particle", VertexFormats.POSITION_TEXTURE_COLOR_LIGHT), (shader) -> particleShader = shader));
            list2.add(Pair.of(new Shader(manager, "position", VertexFormats.POSITION), (shader) -> positionShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_color", VertexFormats.POSITION_COLOR), (shader) -> positionColorShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_color_lightmap", VertexFormats.POSITION_COLOR_LIGHT), (shader) -> positionColorLightmapShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_color_tex", VertexFormats.POSITION_COLOR_TEXTURE), (shader) -> positionColorTexShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_color_tex_lightmap", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT), (shader) -> positionColorTexLightmapShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_tex", VertexFormats.POSITION_TEXTURE), (shader) -> positionTexShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_tex_color", VertexFormats.POSITION_TEXTURE_COLOR), (shader) -> positionTexColorShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_tex_color_normal", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> positionTexColorNormalShader = shader));
            list2.add(Pair.of(new Shader(manager, "position_tex_lightmap_color", VertexFormats.POSITION_TEXTURE_LIGHT_COLOR), (shader) -> positionTexLightmapColorShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_solid", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeSolidShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_cutout_mipped", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeCutoutMippedShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_cutout", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeCutoutShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_translucent", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeTranslucentShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_translucent_moving_block", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeTranslucentMovingBlockShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_translucent_no_crumbling", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeTranslucentNoCrumblingShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_armor_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeArmorCutoutNoCullShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_solid", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntitySolidShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityCutoutShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityCutoutNoNullShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_cutout_no_cull_z_offset", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityCutoutNoNullZOffsetShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_item_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeItemEntityTranslucentCullShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityTranslucentCullShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityTranslucentShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_smooth_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntitySmoothCutoutShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_beacon_beam", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader) -> renderTypeBeaconBeamShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_decal", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityDecalShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_no_outline", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityNoOutlineShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_shadow", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityShadowShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_alpha", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEntityAlphaShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_eyes", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEyesShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_energy_swirl", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL), (shader) -> renderTypeEnergySwirlShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_leash", VertexFormats.POSITION_COLOR_LIGHT), (shader) -> renderTypeLeashShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_water_mask", VertexFormats.POSITION), (shader) -> renderTypeWaterMaskShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_outline", VertexFormats.POSITION_COLOR_TEXTURE), (shader) -> renderTypeOutlineShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_armor_glint", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeArmorGlintShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_armor_entity_glint", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeArmorEntityGlintShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_glint_translucent", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeGlintTranslucentShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_glint", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeGlintShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_glint_direct", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeGlintDirectShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_glint", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeEntityGlintShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_entity_glint_direct", VertexFormats.POSITION_TEXTURE), (shader) -> renderTypeEntityGlintDirectShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_text", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT), (shader) -> renderTypeTextShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_text_intensity", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT), (shader) -> renderTypeTextIntensityShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_text_see_through", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT), (shader) -> renderTypeTextSeeThroughShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_text_intensity_see_through", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT), (shader) -> renderTypeTextIntensitySeeThroughShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_lightning", VertexFormats.POSITION_COLOR), (shader) -> renderTypeLightningShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_tripwire", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL), (shader) -> renderTypeTripwireShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_end_portal", VertexFormats.POSITION), (shader) -> renderTypeEndPortalShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_end_gateway", VertexFormats.POSITION), (shader) -> renderTypeEndGatewayShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_lines", VertexFormats.LINES), (shader) -> renderTypeLinesShader = shader));
            list2.add(Pair.of(new Shader(manager, "rendertype_crumbling", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL), (shader) -> renderTypeCrumblingShader = shader));
        } catch (IOException var5) {
            list2.forEach((pair) -> pair.getFirst().close());
            throw new RuntimeException("could not reload shaders", var5);
        }

        clearShaders();
        list2.forEach((pair) -> {
            Shader shader = pair.getFirst();
            shaders.put(shader.getName(), shader);
            pair.getSecond().accept(shader);
        });
    }

    private static void clearShaders() {
        RenderSystem.assertOnRenderThread();
        shaders.values().forEach(Shader::close);
        shaders.clear();
    }

    @Nullable
    public static Shader getPositionShader() {
        return positionShader;
    }

    @Nullable
    public static Shader getPositionColorShader() {
        return positionColorShader;
    }

    @Nullable
    public static Shader getPositionColorTexShader() {
        return positionColorTexShader;
    }

    @Nullable
    public static Shader getPositionTexShader() {
        return positionTexShader;
    }

    @Nullable
    public static Shader getPositionTexColorShader() {
        return positionTexColorShader;
    }

    @Nullable
    public static Shader getBlockShader() {
        return blockShader;
    }

    @Nullable
    public static Shader getNewEntityShader() {
        return newEntityShader;
    }

    @Nullable
    public static Shader getParticleShader() {
        return particleShader;
    }

    @Nullable
    public static Shader getPositionColorLightmapShader() {
        return positionColorLightmapShader;
    }

    @Nullable
    public static Shader getPositionColorTexLightmapShader() {
        return positionColorTexLightmapShader;
    }

    @Nullable
    public static Shader getPositionTexColorNormalShader() {
        return positionTexColorNormalShader;
    }

    @Nullable
    public static Shader getPositionTexLightmapColorShader() {
        return positionTexLightmapColorShader;
    }

    @Nullable
    public static Shader getRenderTypeSolidShader() {
        return renderTypeSolidShader;
    }

    @Nullable
    public static Shader getRenderTypeCutoutMippedShader() {
        return renderTypeCutoutMippedShader;
    }

    @Nullable
    public static Shader getRenderTypeCutoutShader() {
        return renderTypeCutoutShader;
    }

    @Nullable
    public static Shader getRenderTypeTranslucentShader() {
        return renderTypeTranslucentShader;
    }

    @Nullable
    public static Shader getRenderTypeTranslucentMovingBlockShader() {
        return renderTypeTranslucentMovingBlockShader;
    }

    @Nullable
    public static Shader getRenderTypeTranslucentNoCrumblingShader() {
        return renderTypeTranslucentNoCrumblingShader;
    }

    @Nullable
    public static Shader getRenderTypeArmorCutoutNoCullShader() {
        return renderTypeArmorCutoutNoCullShader;
    }

    @Nullable
    public static Shader getRenderTypeEntitySolidShader() {
        return renderTypeEntitySolidShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityCutoutShader() {
        return renderTypeEntityCutoutShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityCutoutNoNullShader() {
        return renderTypeEntityCutoutNoNullShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityCutoutNoNullZOffsetShader() {
        return renderTypeEntityCutoutNoNullZOffsetShader;
    }

    @Nullable
    public static Shader getRenderTypeItemEntityTranslucentCullShader() {
        return renderTypeItemEntityTranslucentCullShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityTranslucentCullShader() {
        return renderTypeEntityTranslucentCullShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityTranslucentShader() {
        return renderTypeEntityTranslucentShader;
    }

    @Nullable
    public static Shader getRenderTypeEntitySmoothCutoutShader() {
        return renderTypeEntitySmoothCutoutShader;
    }

    @Nullable
    public static Shader getRenderTypeBeaconBeamShader() {
        return renderTypeBeaconBeamShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityDecalShader() {
        return renderTypeEntityDecalShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityNoOutlineShader() {
        return renderTypeEntityNoOutlineShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityShadowShader() {
        return renderTypeEntityShadowShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityAlphaShader() {
        return renderTypeEntityAlphaShader;
    }

    @Nullable
    public static Shader getRenderTypeEyesShader() {
        return renderTypeEyesShader;
    }

    @Nullable
    public static Shader getRenderTypeEnergySwirlShader() {
        return renderTypeEnergySwirlShader;
    }

    @Nullable
    public static Shader getRenderTypeLeashShader() {
        return renderTypeLeashShader;
    }

    @Nullable
    public static Shader getRenderTypeWaterMaskShader() {
        return renderTypeWaterMaskShader;
    }

    @Nullable
    public static Shader getRenderTypeOutlineShader() {
        return renderTypeOutlineShader;
    }

    @Nullable
    public static Shader getRenderTypeArmorGlintShader() {
        return renderTypeArmorGlintShader;
    }

    @Nullable
    public static Shader getRenderTypeArmorEntityGlintShader() {
        return renderTypeArmorEntityGlintShader;
    }

    @Nullable
    public static Shader getRenderTypeGlintTranslucentShader() {
        return renderTypeGlintTranslucentShader;
    }

    @Nullable
    public static Shader getRenderTypeGlintShader() {
        return renderTypeGlintShader;
    }

    @Nullable
    public static Shader getRenderTypeGlintDirectShader() {
        return renderTypeGlintDirectShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityGlintShader() {
        return renderTypeEntityGlintShader;
    }

    @Nullable
    public static Shader getRenderTypeEntityGlintDirectShader() {
        return renderTypeEntityGlintDirectShader;
    }

    @Nullable
    public static Shader getRenderTypeTextShader() {
        return renderTypeTextShader;
    }

    @Nullable
    public static Shader getRenderTypeTextIntensityShader() {
        return renderTypeTextIntensityShader;
    }

    @Nullable
    public static Shader getRenderTypeTextSeeThroughShader() {
        return renderTypeTextSeeThroughShader;
    }

    @Nullable
    public static Shader getRenderTypeTextIntensitySeeThroughShader() {
        return renderTypeTextIntensitySeeThroughShader;
    }

    @Nullable
    public static Shader getRenderTypeLightningShader() {
        return renderTypeLightningShader;
    }

    @Nullable
    public static Shader getRenderTypeTripwireShader() {
        return renderTypeTripwireShader;
    }

    @Nullable
    public static Shader getRenderTypeEndPortalShader() {
        return renderTypeEndPortalShader;
    }

    @Nullable
    public static Shader getRenderTypeEndGatewayShader() {
        return renderTypeEndGatewayShader;
    }

    @Nullable
    public static Shader getRenderTypeLinesShader() {
        return renderTypeLinesShader;
    }

    @Nullable
    public static Shader getRenderTypeCrumblingShader() {
        return renderTypeCrumblingShader;
    }

    @EventListener(priority = ListenerPriority.LOW)
    private static void stitchExpandableAtlasesPostInit(TextureRegisterEvent event) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TexturePack texturePack = minecraft.texturePackManager.texturePack;
        ResourceReloader.create(minecraft.texturePackManager.texturePack, Collections.singletonList(
                StationRenderAPI.getBakedModelManager()
        ), Util.getMainWorkerExecutor(), Runnable::run, COMPLETED_UNIT_FUTURE);
        ItemModels.reloadModelsAll();
        TERRAIN.registerTextureBinders(minecraft.textureManager, texturePack);
        GUI_ITEMS.registerTextureBinders(minecraft.textureManager, texturePack);
        debugExportAtlases();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void texturePackApplied(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(TextureRegisterEvent.builder().build());
    }

    private static void debugExportAtlases() {
//        if (DEBUG_EXPORT_ATLASES) {
//            Stream.of(TERRAIN, GUI_ITEMS).forEach(expandableAtlas -> {
//                if (expandableAtlas.getImage() == null)
//                    LOGGER.debug("Empty atlas " + expandableAtlas.id + ". Skipping export.");
//                else {
//                    LOGGER.debug("Exporting atlas " + expandableAtlas.id + "...");
//                    File debug = new File("." + StationAPI.MODID + ".out/exported_atlases/" + expandableAtlas.id.toString().replace(":", "_") + ".png");
//                    //noinspection ResultOfMethodCallIgnored
//                    debug.mkdirs();
//                    try {
//                        ImageIO.write(expandableAtlas.getImage(), "png", debug);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
//        }
    }
}
