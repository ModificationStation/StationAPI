package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.plugin.ProvideRenderPluginEvent;
import net.modificationstation.stationapi.api.client.registry.AtlasRegistry;
import net.modificationstation.stationapi.api.client.registry.ModelRegistry;
import net.modificationstation.stationapi.api.client.texture.TexturePackDependent;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.client.texture.plugin.StationRenderPlugin;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.lwjgl.opengl.GL11;

import javax.imageio.*;
import java.io.*;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRenderAPI {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(StationAPI.MODID + ".debug.export_atlases", "false"));

    public static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

//    @Deprecated
//    public static ExpandableAtlas
//            STATION_TERRAIN,
//            STATION_GUI_ITEMS;

    @Deprecated
    public static JsonModelAtlas STATION_JSON_MODELS;

    @EventListener(priority = ListenerPriority.HIGH)
    private static void preInit(ProvideRenderPluginEvent event) {
        event.pluginProvider = StationRenderPlugin::new;
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        TERRAIN = new ExpandableAtlas(of("terrain")).setTessellator(() -> Tessellator.INSTANCE);
        GUI_ITEMS = new ExpandableAtlas(of("gui_items"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
//        STATION_TERRAIN = new ExpandableAtlas(of(StationAPI.MODID, "terrain"), TERRAIN).initTessellator();
//        STATION_GUI_ITEMS = new ExpandableAtlas(of(StationAPI.MODID, "gui_items"), GUI_ITEMS);
        STATION_JSON_MODELS = new JsonModelAtlas(of(StationAPI.MODID, "json_textures")).setTessellator(TessellatorAccessor.newInst(8388608));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture), staticReference -> new StationVanillaTextureBinder(staticReference, new StationStillWaterTextureBinder(), "/custom_water_still.png"));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture + 1), staticReference -> Util.make(new StationVanillaTextureBinder(staticReference, new StationFlowingWaterTextureBinder(), "/custom_water_flowing.png"), textureBinder -> textureBinder.textureSize = 2));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture), staticReference -> new StationVanillaTextureBinder(staticReference, new StationStillLavaTextureBinder(), "/custom_lava_still.png"));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture + 1), staticReference -> Util.make(new StationVanillaTextureBinder(staticReference, new StationFlowingLavaTextureBinder(), "/custom_lava_flowing.png"), textureBinder -> textureBinder.textureSize = 2));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FIRE.texture), staticReference -> new StationVanillaTextureBinder(staticReference, new StationFireTextureBinder(0), "/custom_fire_e_w.png"));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.FIRE.texture + 16), staticReference -> new StationVanillaTextureBinder(staticReference, new StationFireTextureBinder(1), "/custom_fire_n_s.png"));
        TERRAIN.addTextureBinder(TERRAIN.getTexture(BlockBase.PORTAL.texture), staticReference -> new StationVanillaTextureBinder(staticReference, new StationPortalTextureBinder(), "/custom_portal.png"));
        GUI_ITEMS.addTextureBinder(GUI_ITEMS.getTexture(ItemBase.compass.getTexturePosition(0)), StationCompassTextureBinder::new);
        GUI_ITEMS.addTextureBinder(GUI_ITEMS.getTexture(ItemBase.clock.getTexturePosition(0)), StationClockTextureBinder::new);
    }

    @EventListener(priority = ListenerPriority.LOW)
    private static void stitchExpandableAtlasesPostInit(TextureRegisterEvent event) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TexturePack texturePack = minecraft.texturePackManager.texturePack;
        ModelRegistry.INSTANCE.forEach((identifier, model) -> model.reloadFromTexturePack(texturePack));
        AtlasRegistry.INSTANCE.stream().map(Map.Entry::getValue).filter(atlas -> atlas instanceof ExpandableAtlas).map(atlas -> (ExpandableAtlas) atlas).forEach(ExpandableAtlas::stitch);
        TextureManager textureManager = minecraft.textureManager;
        AtlasRegistry.INSTANCE.forEach((identifier, atlas) -> atlas.textureBinders.forEach(arg -> {
            if (arg instanceof TexturePackDependent)
                ((TexturePackDependent) arg).reloadFromTexturePack(texturePack);
            textureManager.addTextureBinder(arg);
        }));
        debugExportAtlases();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void beforeTexturePackApplied(TexturePackLoadedEvent.Before event) {
        Map<String, Integer> textureMap = ((TextureManagerAccessor) event.textureManager).getTextures();
        new HashMap<>(textureMap).keySet().stream().filter(s -> event.newTexturePack.getResourceAsStream(s) == null).forEach(s -> GL11.glDeleteTextures(textureMap.remove(s)));
        ModelRegistry.INSTANCE.forEach((identifier, model) -> AtlasRegistry.INSTANCE.unregister(identifier));
        ((TextureManagerAccessor) event.textureManager).getTextureBinders().clear();
        AtlasRegistry.INSTANCE.forEach((identifier, atlas) -> AtlasRegistry.INSTANCE.unregister(identifier));
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void texturePackApplied(TexturePackLoadedEvent.After event) {
//        AtlasRegistry.INSTANCE.forEach((identifier, atlas) -> atlas.reloadFromTexturePack(event.newTexturePack));
//        new ArrayList<>(((TextureManagerAccessor) event.textureManager).getTextureBinders()).stream().filter(textureBinder -> textureBinder instanceof TexturePackDependent).forEach(textureBinder -> ((TexturePackDependent) textureBinder).reloadFromTexturePack(event.newTexturePack));
        StationAPI.EVENT_BUS.post(new TextureRegisterEvent());
    }

    private static void debugExportAtlases() {
        if (DEBUG_EXPORT_ATLASES) {
            AtlasRegistry.INSTANCE.stream().map(Map.Entry::getValue).filter(atlas -> atlas instanceof ExpandableAtlas).map(atlas -> (ExpandableAtlas) atlas).forEach(expandableAtlas -> {
                if (expandableAtlas.getImage() == null)
                    LOGGER.debug("Empty atlas " + expandableAtlas.id + ". Skipping export.");
                else {
                    LOGGER.debug("Exporting atlas " + expandableAtlas.id + "...");
                    File debug = new File("." + StationAPI.MODID + ".out/exported_atlases/" + expandableAtlas.id.toString().replace(":", "_") + ".png");
                    //noinspection ResultOfMethodCallIgnored
                    debug.mkdirs();
                    try {
                        ImageIO.write(expandableAtlas.getImage(), "png", debug);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
