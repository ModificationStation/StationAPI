package net.modificationstation.stationapi.impl.client.texture;

import com.mojang.datafixers.util.Unit;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.event.resource.TexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.impl.client.resource.ResourceReloader;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

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

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        TERRAIN = new ExpandableAtlas(Atlases.GAME_ATLAS_TEXTURE);
        GUI_ITEMS = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
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
