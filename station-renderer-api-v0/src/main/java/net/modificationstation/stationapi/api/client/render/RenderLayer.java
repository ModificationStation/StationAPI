package net.modificationstation.stationapi.api.client.render;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Util;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Environment(value=EnvType.CLIENT)
public abstract class RenderLayer
extends RenderPhase {
    private static final int field_32776 = 4;
    private static final int field_32777 = 0x100000;
    public static final int SOLID_BUFFER_SIZE = 0x200000;
    public static final int TRANSLUCENT_BUFFER_SIZE = 262144;
    public static final int CUTOUT_BUFFER_SIZE = 131072;
    public static final int DEFAULT_BUFFER_SIZE = 256;
    private static final RenderLayer SOLID = RenderLayer.of("solid", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 0x200000, true, false, MultiPhaseParameters.builder().shader(SOLID_SHADER).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).build(true));
    private static final RenderLayer CUTOUT_MIPPED = RenderLayer.of("cutout_mipped", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 131072, true, false, MultiPhaseParameters.builder().shader(CUTOUT_MIPPED_SHADER).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).build(true));
    private static final RenderLayer CUTOUT = RenderLayer.of("cutout", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 131072, true, false, MultiPhaseParameters.builder().shader(CUTOUT_SHADER).texture(BLOCK_ATLAS_TEXTURE).build(true));
    private static final RenderLayer TRANSLUCENT = RenderLayer.of("translucent", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 0x200000, true, true, RenderLayer.of(TRANSLUCENT_SHADER));
    private static final RenderLayer TRANSLUCENT_MOVING_BLOCK = RenderLayer.of("translucent_moving_block", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 262144, false, true, RenderLayer.getItemPhaseData());
    private static final RenderLayer TRANSLUCENT_NO_CRUMBLING = RenderLayer.of("translucent_no_crumbling", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 262144, false, true, RenderLayer.of(TRANSLUCENT_NO_CRUMBLING_SHADER));
    private static final Function<Identifier, RenderLayer> ARMOR_CUTOUT_NO_CULL = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ARMOR_CUTOUT_NO_CULL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(NO_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).build(true);
        return RenderLayer.of("armor_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });
    private static final Function<Identifier, RenderLayer> ENTITY_SOLID = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_SOLID_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(NO_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("entity_solid", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });
    private static final Function<Identifier, RenderLayer> ENTITY_CUTOUT = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_CUTOUT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(NO_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("entity_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });
    private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_CUTOUT_NO_CULL = Util.memoize((texture, affectsOutline) -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_CUTOUT_NONULL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(NO_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(affectsOutline);
        return RenderLayer.of("entity_cutout_no_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });
    private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_CUTOUT_NO_CULL_Z_OFFSET = Util.memoize((texture, affectsOutline) -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_CUTOUT_NONULL_OFFSET_Z_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(NO_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).build(affectsOutline);
        return RenderLayer.of("entity_cutout_no_cull_z_offset", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });
    private static final Function<Identifier, RenderLayer> ITEM_ENTITY_TRANSLUCENT_CULL = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ITEM_ENTITY_TRANSLUCENT_CULL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).target(ITEM_TARGET).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).writeMaskState(RenderPhase.ALL_MASK).build(true);
        return RenderLayer.of("item_entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });
    private static final Function<Identifier, RenderLayer> ENTITY_TRANSLUCENT_CULL = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_TRANSLUCENT_CULL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("entity_translucent_cull", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });
    private static final BiFunction<Identifier, Boolean, RenderLayer> ENTITY_TRANSLUCENT = Util.memoize((texture, affectsOutline) -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_TRANSLUCENT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(affectsOutline);
        return RenderLayer.of("entity_translucent", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, true, multiPhaseParameters);
    });
    private static final Function<Identifier, RenderLayer> ENTITY_SMOOTH_CUTOUT = Util.memoizeIdentity(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_SMOOTH_CUTOUT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(true);
        return RenderLayer.of("entity_smooth_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, multiPhaseParameters);
    });
//    private static final BiFunction<Identifier, Boolean, RenderLayer> BEACON_BEAM = Util.memoize((texture, affectsOutline) -> {
//        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(BEACON_BEAM_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(affectsOutline != false ? TRANSLUCENT_TRANSPARENCY : NO_TRANSPARENCY).writeMaskState(affectsOutline != false ? COLOR_MASK : ALL_MASK).build(false);
//        return RenderLayer.of("beacon_beam", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
//    });
//    private static final Function<Identifier, RenderLayer> ENTITY_DECAL = Util.memoizeIdentity(texture -> {
//        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_DECAL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).depthTest(EQUAL_DEPTH_TEST).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(false);
//        return RenderLayer.of("entity_decal", VertexFormats.POSITION_COLOUR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, multiPhaseParameters);
//    });
//    private static final Function<Identifier, RenderLayer> ENTITY_NO_OUTLINE = Util.memoizeIdentity(texture -> {
//        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_NO_OUTLINE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).writeMaskState(COLOR_MASK).build(false);
//        return RenderLayer.of("entity_no_outline", VertexFormats.POSITION_COLOUR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
//    });
//    private static final Function<Identifier, RenderLayer> ENTITY_SHADOW = Util.memoizeIdentity(texture -> {
//        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_SHADOW_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(ENABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).writeMaskState(COLOR_MASK).depthTest(LEQUAL_DEPTH_TEST).layering(VIEW_OFFSET_Z_LAYERING).build(false);
//        return RenderLayer.of("entity_shadow", VertexFormats.POSITION_COLOUR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
//    });
//    private static final Function<Identifier, RenderLayer> ENTITY_ALPHA = Util.memoizeIdentity(texture -> {
//        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().shader(ENTITY_ALPHA_SHADER).texture(new RenderPhase.Texture(texture, false, false)).cull(DISABLE_CULLING).build(true);
//        return RenderLayer.of("entity_alpha", VertexFormats.POSITION_COLOUR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, multiPhaseParameters);
//    });
    private static final Function<Identifier, RenderLayer> EYES = Util.memoizeIdentity(texture -> {
        RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, false, false);
        return RenderLayer.of("eyes", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(EYES_SHADER).texture(texture2).transparency(ADDITIVE_TRANSPARENCY).writeMaskState(COLOR_MASK).build(false));
    });
//    private static final RenderLayer LEASH = RenderLayer.of("leash", VertexFormats.POSITION_COLOUR_LIGHT, VertexFormat.DrawMode.TRIANGLE_STRIP, 256, MultiPhaseParameters.builder().shader(LEASH_SHADER).texture(NO_TEXTURE).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).build(false));
//    private static final RenderLayer WATER_MASK = RenderLayer.of("water_mask", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(WATER_MASK_SHADER).texture(NO_TEXTURE).writeMaskState(DEPTH_MASK).build(false));
//    private static final RenderLayer ARMOR_GLINT = RenderLayer.of("armor_glint", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(ARMOR_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).layering(VIEW_OFFSET_Z_LAYERING).build(false));
//    private static final RenderLayer ARMOR_ENTITY_GLINT = RenderLayer.of("armor_entity_glint", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(ARMOR_ENTITY_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).layering(VIEW_OFFSET_Z_LAYERING).build(false));
//    private static final RenderLayer GLINT_TRANSLUCENT = RenderLayer.of("glint_translucent", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(TRANSLUCENT_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).target(ITEM_TARGET).build(false));
//    private static final RenderLayer GLINT = RenderLayer.of("glint", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
//    private static final RenderLayer DIRECT_GLINT = RenderLayer.of("glint_direct", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(DIRECT_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(GLINT_TEXTURING).build(false));
//    private static final RenderLayer ENTITY_GLINT = RenderLayer.of("entity_glint", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(ENTITY_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).target(ITEM_TARGET).texturing(ENTITY_GLINT_TEXTURING).build(false));
//    private static final RenderLayer DIRECT_ENTITY_GLINT = RenderLayer.of("entity_glint_direct", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(DIRECT_ENTITY_GLINT_SHADER).texture(new RenderPhase.Texture(ItemRenderer.ENCHANTED_ITEM_GLINT, true, false)).writeMaskState(COLOR_MASK).cull(DISABLE_CULLING).depthTest(EQUAL_DEPTH_TEST).transparency(GLINT_TRANSPARENCY).texturing(ENTITY_GLINT_TEXTURING).build(false));
    private static final Function<Identifier, RenderLayer> CRUMBLING = Util.memoizeIdentity(texture -> {
        RenderPhase.Texture texture2 = new RenderPhase.Texture(texture, false, false);
        return RenderLayer.of("crumbling", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(CRUMBLING_SHADER).texture(texture2).transparency(CRUMBLING_TRANSPARENCY).writeMaskState(COLOR_MASK).layering(POLYGON_OFFSET_LAYERING).build(false));
    });
//    private static final Function<Identifier, RenderLayer> TEXT = Util.memoizeIdentity(texture -> RenderLayer.of("text", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TEXT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).build(false)));
//    private static final Function<Identifier, RenderLayer> TEXT_INTENSITY = Util.memoizeIdentity(texture -> RenderLayer.of("text_intensity", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TEXT_INTENSITY_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).build(false)));
//    private static final Function<Identifier, RenderLayer> TEXT_POLYGON_OFFSET = Util.memoizeIdentity(texture -> RenderLayer.of("text_polygon_offset", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TEXT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).layering(POLYGON_OFFSET_LAYERING).build(false)));
//    private static final Function<Identifier, RenderLayer> TEXT_INTENSITY_POLYGON_OFFSET = Util.memoizeIdentity(texture -> RenderLayer.of("text_intensity_polygon_offset", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TEXT_INTENSITY_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).layering(POLYGON_OFFSET_LAYERING).build(false)));
//    private static final Function<Identifier, RenderLayer> TEXT_SEE_THROUGH = Util.memoizeIdentity(texture -> RenderLayer.of("text_see_through", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TRANSPARENT_TEXT_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).depthTest(ALWAYS_DEPTH_TEST).writeMaskState(COLOR_MASK).build(false)));
//    private static final Function<Identifier, RenderLayer> TEXT_INTENSITY_SEE_THROUGH = Util.memoizeIdentity(texture -> RenderLayer.of("text_intensity_see_through", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(TRANSPARENT_TEXT_INTENSITY_SHADER).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).depthTest(ALWAYS_DEPTH_TEST).writeMaskState(COLOR_MASK).build(false)));
//    private static final RenderLayer LIGHTNING = RenderLayer.of("lightning", VertexFormats.POSITION_COLOUR, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(LIGHTNING_SHADER).writeMaskState(ALL_MASK).transparency(LIGHTNING_TRANSPARENCY).target(WEATHER_TARGET).build(false));
//    private static final RenderLayer TRIPWIRE = RenderLayer.of("tripwire", VertexFormats.POSITION_COLOUR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 262144, true, true, RenderLayer.getTripwirePhaseData());
//    private static final RenderLayer END_PORTAL = RenderLayer.of("end_portal", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().shader(END_PORTAL_SHADER).texture(RenderPhase.Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build()).build(false));
//    private static final RenderLayer END_GATEWAY = RenderLayer.of("end_gateway", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().shader(END_GATEWAY_SHADER).texture(RenderPhase.Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build()).build(false));
//    public static final MultiPhase LINES = RenderLayer.of("lines", VertexFormats.LINES, VertexFormat.DrawMode.LINES, 256, MultiPhaseParameters.builder().shader(LINES_SHADER).lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty())).layering(VIEW_OFFSET_Z_LAYERING).transparency(TRANSLUCENT_TRANSPARENCY).target(ITEM_TARGET).writeMaskState(ALL_MASK).cull(DISABLE_CULLING).build(false));
//    public static final MultiPhase LINE_STRIP = RenderLayer.of("line_strip", VertexFormats.LINES, VertexFormat.DrawMode.LINE_STRIP, 256, MultiPhaseParameters.builder().shader(LINES_SHADER).lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty())).layering(VIEW_OFFSET_Z_LAYERING).transparency(TRANSLUCENT_TRANSPARENCY).target(ITEM_TARGET).writeMaskState(ALL_MASK).cull(DISABLE_CULLING).build(false));
    private final VertexFormat vertexFormat;
    private final VertexFormat.DrawMode drawMode;
    private final int expectedBufferSize;
    private final boolean hasCrumbling;
    private final boolean translucent;
    private final Optional<RenderLayer> optionalThis;

    public static RenderLayer getSolid() {
        return SOLID;
    }

    public static RenderLayer getCutoutMipped() {
        return CUTOUT_MIPPED;
    }

    public static RenderLayer getCutout() {
        return CUTOUT;
    }

    private static MultiPhaseParameters of(RenderPhase.Shader shader) {
        return MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(shader).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).transparency(TRANSLUCENT_TRANSPARENCY).target(TRANSLUCENT_TARGET).build(true);
    }

    public static RenderLayer getTranslucent() {
        return TRANSLUCENT;
    }

    private static MultiPhaseParameters getItemPhaseData() {
        return MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(TRANSLUCENT_MOVING_BLOCK_SHADER).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).transparency(TRANSLUCENT_TRANSPARENCY).target(ITEM_TARGET).build(true);
    }

    public static RenderLayer getTranslucentMovingBlock() {
        return TRANSLUCENT_MOVING_BLOCK;
    }

    public static RenderLayer getTranslucentNoCrumbling() {
        return TRANSLUCENT_NO_CRUMBLING;
    }

    public static RenderLayer getArmorCutoutNoCull(Identifier texture) {
        return ARMOR_CUTOUT_NO_CULL.apply(texture);
    }

    public static RenderLayer getEntitySolid(Identifier texture) {
        return ENTITY_SOLID.apply(texture);
    }

    public static RenderLayer getEntityCutout(Identifier texture) {
        return ENTITY_CUTOUT.apply(texture);
    }

    public static RenderLayer getEntityCutoutNoCull(Identifier texture, boolean affectsOutline) {
        return ENTITY_CUTOUT_NO_CULL.apply(texture, affectsOutline);
    }

    public static RenderLayer getEntityCutoutNoCull(Identifier texture) {
        return RenderLayer.getEntityCutoutNoCull(texture, true);
    }

    public static RenderLayer getEntityCutoutNoCullZOffset(Identifier texture, boolean affectsOutline) {
        return ENTITY_CUTOUT_NO_CULL_Z_OFFSET.apply(texture, affectsOutline);
    }

    public static RenderLayer getEntityCutoutNoCullZOffset(Identifier texture) {
        return RenderLayer.getEntityCutoutNoCullZOffset(texture, true);
    }

    public static RenderLayer getItemEntityTranslucentCull(Identifier texture) {
        return ITEM_ENTITY_TRANSLUCENT_CULL.apply(texture);
    }

    public static RenderLayer getEntityTranslucentCull(Identifier texture) {
        return ENTITY_TRANSLUCENT_CULL.apply(texture);
    }

    public static RenderLayer getEntityTranslucent(Identifier texture, boolean affectsOutline) {
        return ENTITY_TRANSLUCENT.apply(texture, affectsOutline);
    }

    public static RenderLayer getEntityTranslucent(Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture, true);
    }

    public static RenderLayer getEntitySmoothCutout(Identifier texture) {
        return ENTITY_SMOOTH_CUTOUT.apply(texture);
    }

//    public static RenderLayer getBeaconBeam(Identifier texture, boolean translucent) {
//        return BEACON_BEAM.apply(texture, translucent);
//    }
//
//    public static RenderLayer getEntityDecal(Identifier texture) {
//        return ENTITY_DECAL.apply(texture);
//    }
//
//    public static RenderLayer getEntityNoOutline(Identifier texture) {
//        return ENTITY_NO_OUTLINE.apply(texture);
//    }
//
//    public static RenderLayer getEntityShadow(Identifier texture) {
//        return ENTITY_SHADOW.apply(texture);
//    }
//
//    public static RenderLayer getEntityAlpha(Identifier texture) {
//        return ENTITY_ALPHA.apply(texture);
//    }

    public static RenderLayer getEyes(Identifier texture) {
        return EYES.apply(texture);
    }

    public static RenderLayer getEnergySwirl(Identifier texture, float x, float y) {
        return RenderLayer.of("energy_swirl", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder().shader(ENERGY_SWIRL_SHADER).texture(new RenderPhase.Texture(texture, false, false)).texturing(new RenderPhase.OffsetTexturing(x, y)).transparency(ADDITIVE_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(false));
    }

//    public static RenderLayer getLeash() {
//        return LEASH;
//    }
//
//    public static RenderLayer getWaterMask() {
//        return WATER_MASK;
//    }
//
//    public static RenderLayer getOutline(Identifier texture) {
//        return MultiPhase.CULLING_LAYERS.apply(texture, DISABLE_CULLING);
//    }
//
//    public static RenderLayer getArmorGlint() {
//        return ARMOR_GLINT;
//    }
//
//    public static RenderLayer getArmorEntityGlint() {
//        return ARMOR_ENTITY_GLINT;
//    }
//
//    public static RenderLayer getGlintTranslucent() {
//        return GLINT_TRANSLUCENT;
//    }
//
//    public static RenderLayer getGlint() {
//        return GLINT;
//    }
//
//    public static RenderLayer getDirectGlint() {
//        return DIRECT_GLINT;
//    }
//
//    public static RenderLayer getEntityGlint() {
//        return ENTITY_GLINT;
//    }
//
//    public static RenderLayer getDirectEntityGlint() {
//        return DIRECT_ENTITY_GLINT;
//    }

    public static RenderLayer getBlockBreaking(Identifier texture) {
        return CRUMBLING.apply(texture);
    }

//    public static RenderLayer getText(Identifier texture) {
//        return TEXT.apply(texture);
//    }
//
//    public static RenderLayer getTextIntensity(Identifier texture) {
//        return TEXT_INTENSITY.apply(texture);
//    }
//
//    public static RenderLayer getTextPolygonOffset(Identifier texture) {
//        return TEXT_POLYGON_OFFSET.apply(texture);
//    }
//
//    public static RenderLayer getTextIntensityPolygonOffset(Identifier texture) {
//        return TEXT_INTENSITY_POLYGON_OFFSET.apply(texture);
//    }
//
//    public static RenderLayer getTextSeeThrough(Identifier texture) {
//        return TEXT_SEE_THROUGH.apply(texture);
//    }
//
//    public static RenderLayer getTextIntensitySeeThrough(Identifier texture) {
//        return TEXT_INTENSITY_SEE_THROUGH.apply(texture);
//    }
//
//    public static RenderLayer getLightning() {
//        return LIGHTNING;
//    }

//    private static MultiPhaseParameters getTripwirePhaseData() {
//        return MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(TRIPWIRE_SHADER).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).transparency(TRANSLUCENT_TRANSPARENCY).target(WEATHER_TARGET).build(true);
//    }

//    public static RenderLayer getTripwire() {
//        return TRIPWIRE;
//    }
//
//    public static RenderLayer getEndPortal() {
//        return END_PORTAL;
//    }
//
//    public static RenderLayer getEndGateway() {
//        return END_GATEWAY;
//    }
//
//    public static RenderLayer getLines() {
//        return LINES;
//    }
//
//    public static RenderLayer getLineStrip() {
//        return LINE_STRIP;
//    }

    public RenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, startAction, endAction);
        this.vertexFormat = vertexFormat;
        this.drawMode = drawMode;
        this.expectedBufferSize = expectedBufferSize;
        this.hasCrumbling = hasCrumbling;
        this.translucent = translucent;
        this.optionalThis = Optional.of(this);
    }

    static MultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, MultiPhaseParameters phaseData) {
        return RenderLayer.of(name, vertexFormat, drawMode, expectedBufferSize, false, false, phaseData);
    }

    private static MultiPhase of(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, MultiPhaseParameters phases) {
        return new MultiPhase(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, phases);
    }

    public void draw(BufferBuilder buffer, int cameraX, int cameraY, int cameraZ) {
        if (!buffer.isBuilding()) {
            return;
        }
        if (this.translucent) {
            buffer.sortFrom(cameraX, cameraY, cameraZ);
        }
        BufferBuilder.BuiltBuffer builtBuffer = buffer.end();
        this.startDrawing();
        BufferRenderer.drawWithShader(builtBuffer);
        this.endDrawing();
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static List<RenderLayer> getBlockLayers() {
        return ImmutableList.of(RenderLayer.getSolid(), RenderLayer.getCutoutMipped(), RenderLayer.getCutout(), RenderLayer.getTranslucent()/*, RenderLayer.getTripwire()*/);
    }

    public int getExpectedBufferSize() {
        return this.expectedBufferSize;
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public VertexFormat.DrawMode getDrawMode() {
        return this.drawMode;
    }

    public Optional<RenderLayer> getAffectedOutline() {
        return Optional.empty();
    }

    public boolean isOutline() {
        return false;
    }

    public boolean hasCrumbling() {
        return this.hasCrumbling;
    }

    public Optional<RenderLayer> asOptional() {
        return this.optionalThis;
    }

    @Environment(value=EnvType.CLIENT)
    protected static final class MultiPhaseParameters {
        final RenderPhase.TextureBase texture;
        private final RenderPhase.Shader shader;
        private final RenderPhase.Transparency transparency;
        private final RenderPhase.DepthTest depthTest;
        final RenderPhase.Cull cull;
        private final RenderPhase.Lightmap lightmap;
        private final RenderPhase.Overlay overlay;
        private final RenderPhase.Layering layering;
        private final RenderPhase.Target target;
        private final RenderPhase.Texturing texturing;
        private final RenderPhase.WriteMaskState writeMaskState;
        private final RenderPhase.LineWidth lineWidth;
        final OutlineMode outlineMode;
        final ImmutableList<RenderPhase> phases;

        MultiPhaseParameters(RenderPhase.TextureBase texture, RenderPhase.Shader shader, RenderPhase.Transparency transparency, RenderPhase.DepthTest depthTest, RenderPhase.Cull cull, RenderPhase.Lightmap lightmap, RenderPhase.Overlay overlay, RenderPhase.Layering layering, RenderPhase.Target target, RenderPhase.Texturing texturing, RenderPhase.WriteMaskState writeMaskState, RenderPhase.LineWidth lineWidth, OutlineMode outlineMode) {
            this.texture = texture;
            this.shader = shader;
            this.transparency = transparency;
            this.depthTest = depthTest;
            this.cull = cull;
            this.lightmap = lightmap;
            this.overlay = overlay;
            this.layering = layering;
            this.target = target;
            this.texturing = texturing;
            this.writeMaskState = writeMaskState;
            this.lineWidth = lineWidth;
            this.outlineMode = outlineMode;
            this.phases = ImmutableList.of(this.texture, this.shader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth);
        }

        public String toString() {
            return "CompositeState[" + this.phases + ", outlineProperty=" + this.outlineMode + "]";
        }

        public static Builder builder() {
            return new Builder();
        }

        @Environment(EnvType.CLIENT)
        public static class Builder {
            private RenderPhase.TextureBase texture = RenderPhase.NO_TEXTURE;
            private RenderPhase.Shader shader = RenderPhase.NO_SHADER;
            private RenderPhase.Transparency transparency = RenderPhase.NO_TRANSPARENCY;
            private RenderPhase.DepthTest depthTest = RenderPhase.LEQUAL_DEPTH_TEST;
            private RenderPhase.Cull cull = RenderPhase.ENABLE_CULLING;
            private RenderPhase.Lightmap lightmap = RenderPhase.DISABLE_LIGHTMAP;
            private RenderPhase.Overlay overlay = RenderPhase.DISABLE_OVERLAY_COLOR;
            private RenderPhase.Layering layering = RenderPhase.NO_LAYERING;
            private RenderPhase.Target target = RenderPhase.MAIN_TARGET;
            private RenderPhase.Texturing texturing = RenderPhase.DEFAULT_TEXTURING;
            private RenderPhase.WriteMaskState writeMaskState = RenderPhase.ALL_MASK;
            private RenderPhase.LineWidth lineWidth = RenderPhase.FULL_LINE_WIDTH;

            Builder() {
            }

            public Builder texture(RenderPhase.TextureBase texture) {
                this.texture = texture;
                return this;
            }

            public Builder shader(RenderPhase.Shader shader) {
                this.shader = shader;
                return this;
            }

            public Builder transparency(RenderPhase.Transparency transparency) {
                this.transparency = transparency;
                return this;
            }

            public Builder depthTest(RenderPhase.DepthTest depthTest) {
                this.depthTest = depthTest;
                return this;
            }

            public Builder cull(RenderPhase.Cull cull) {
                this.cull = cull;
                return this;
            }

            public Builder lightmap(RenderPhase.Lightmap lightmap) {
                this.lightmap = lightmap;
                return this;
            }

            public Builder overlay(RenderPhase.Overlay overlay) {
                this.overlay = overlay;
                return this;
            }

            public Builder layering(RenderPhase.Layering layering) {
                this.layering = layering;
                return this;
            }

            public Builder target(RenderPhase.Target target) {
                this.target = target;
                return this;
            }

            public Builder texturing(RenderPhase.Texturing texturing) {
                this.texturing = texturing;
                return this;
            }

            public Builder writeMaskState(RenderPhase.WriteMaskState writeMaskState) {
                this.writeMaskState = writeMaskState;
                return this;
            }

            public Builder lineWidth(RenderPhase.LineWidth lineWidth) {
                this.lineWidth = lineWidth;
                return this;
            }

            public MultiPhaseParameters build(boolean affectsOutline) {
                return this.build(affectsOutline ? OutlineMode.AFFECTS_OUTLINE : OutlineMode.NONE);
            }

            public MultiPhaseParameters build(OutlineMode outlineMode) {
                return new MultiPhaseParameters(this.texture, this.shader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, outlineMode);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    static final class MultiPhase
    extends RenderLayer {
        static final BiFunction<Identifier, RenderPhase.Cull, RenderLayer> CULLING_LAYERS = Util.memoize((texture, culling) -> RenderLayer.of("outline", VertexFormats.POSITION_COLOR_TEXTURE, VertexFormat.DrawMode.QUADS, 256, MultiPhaseParameters.builder().shader(OUTLINE_SHADER).texture(new RenderPhase.Texture(texture, false, false)).cull(culling).depthTest(ALWAYS_DEPTH_TEST)/*.target(OUTLINE_TARGET)*/.build(OutlineMode.IS_OUTLINE)));
        private final MultiPhaseParameters phases;
        private final Optional<RenderLayer> affectedOutline;
        private final boolean outline;

        MultiPhase(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, MultiPhaseParameters phases) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () -> phases.phases.forEach(RenderPhase::startDrawing), () -> phases.phases.forEach(RenderPhase::endDrawing));
            this.phases = phases;
            this.affectedOutline = phases.outlineMode == OutlineMode.AFFECTS_OUTLINE ? phases.texture.getId().map(texture -> CULLING_LAYERS.apply(texture, phases.cull)) : Optional.empty();
            this.outline = phases.outlineMode == OutlineMode.IS_OUTLINE;
        }

        @Override
        public Optional<RenderLayer> getAffectedOutline() {
            return this.affectedOutline;
        }

        @Override
        public boolean isOutline() {
            return this.outline;
        }

        private MultiPhaseParameters getPhases() {
            return this.phases;
        }

        @Override
        public String toString() {
            return "RenderType[" + this.name + ":" + this.phases + "]";
        }
    }

    @Environment(value=EnvType.CLIENT)
    enum OutlineMode {
        NONE("none"),
        IS_OUTLINE("is_outline"),
        AFFECTS_OUTLINE("affects_outline");

        private final String name;

        OutlineMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

