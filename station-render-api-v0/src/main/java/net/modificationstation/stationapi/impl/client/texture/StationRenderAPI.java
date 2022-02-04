package net.modificationstation.stationapi.impl.client.texture;

import com.mojang.datafixers.util.Unit;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.plugin.ProvideRenderPluginEvent;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.client.resource.ResourceReloader;
import net.modificationstation.stationapi.impl.client.texture.plugin.StationRenderPlugin;
import net.modificationstation.stationapi.mixin.render.client.TextureManagerAccessor;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.*;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRenderAPI {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("StationRender|API")
    public static final Logger LOGGER = Null.get();

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(StationAPI.MODID + ".debug.export_atlases", "false"));

    private static final CompletableFuture<Unit> COMPLETED_UNIT_FUTURE = CompletableFuture.completedFuture(Unit.INSTANCE);
    public static BakedModelManager BAKED_MODEL_MANAGER;

    public static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

    @EventListener(priority = ListenerPriority.HIGH)
    private static void preInit(ProvideRenderPluginEvent event) {
        event.pluginProvider = StationRenderPlugin::new;
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        if (BAKED_MODEL_MANAGER == null)
            BAKED_MODEL_MANAGER = new BakedModelManager(minecraft.textureManager, BlockColours.create(), 0);
        TERRAIN = new ExpandableAtlas(Atlases.BLOCK_ATLAS_TEXTURE);
        GUI_ITEMS = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
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
        ResourceReloader.create(minecraft.texturePackManager.texturePack, Collections.singletonList(BAKED_MODEL_MANAGER), Util.getMainWorkerExecutor(), Runnable::run, COMPLETED_UNIT_FUTURE);
        TERRAIN.registerTextureBinders(minecraft.textureManager, texturePack);
        GUI_ITEMS.registerTextureBinders(minecraft.textureManager, texturePack);
        debugExportAtlases();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void beforeTexturePackApplied(TexturePackLoadedEvent.Before event) {
        Map<String, Integer> textureMap = ((TextureManagerAccessor) event.textureManager).getTextures();
        new HashMap<>(textureMap).keySet().stream().filter(s -> event.newTexturePack.getResourceAsStream(s) == null).forEach(s -> GL11.glDeleteTextures(textureMap.remove(s)));
        ((TextureManagerAccessor) event.textureManager).getTextureBinders().clear();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void texturePackApplied(TexturePackLoadedEvent.After event) {
        StationAPI.EVENT_BUS.post(new TextureRegisterEvent());
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
