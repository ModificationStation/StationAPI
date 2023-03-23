package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.texture.NativeImageBackedTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureUtil;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.SynchronousResourceReloader;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRenderImpl {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("StationRenderer|API")
    public static final Logger LOGGER = Null.get();

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(StationAPI.MODID + ".debug.export_atlases", "false"));

    public static ExpandableAtlas getTerrain() {
        return TERRAIN;
    }

    public static ExpandableAtlas getGuiItems() {
        return GUI_ITEMS;
    }

    private static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerReloaders(AssetsResourceReloaderRegisterEvent event) {
        event.resourceManager.registerReloader(StationRenderAPI.getBakedModelManager());
        event.resourceManager.registerReloader((SynchronousResourceReloader) manager -> ItemModels.reloadModelsAll());
        event.resourceManager.registerReloader((SynchronousResourceReloader) manager -> {
            //noinspection deprecation
            StationTextureManager textureManager = StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager);
            SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
            for (int i = 0, size = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.size(); i < size; i++)
                textureManager.registerTexture(ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get(i), new NativeImageBackedTexture(Objects.requireNonNull(atlas.getSprite(ModelLoader.BLOCK_DESTRUCTION_STAGES.get(i))).getContents().getBaseFrame()));
        });
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 + Integer.MAX_VALUE / 8)
    private static void init(AssetsReloadEvent event) {
        TERRAIN = new ExpandableAtlas(of("textures/atlas/terrain.png"));
        GUI_ITEMS = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
        StationAPI.EVENT_BUS.post(TextureRegisterEvent.builder().build());
        TextureUtil.maxSupportedTextureSize();
    }

    @EventListener(numPriority = Integer.MIN_VALUE / 2 + Integer.MIN_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void stitchExpandableAtlasesPostInit(AssetsReloadEvent event) {
        //noinspection deprecation
        Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
        TexturePack texturePack = minecraft.texturePackManager.texturePack;
        TERRAIN.registerTextureBinders(minecraft.textureManager, texturePack);
        GUI_ITEMS.registerTextureBinders(minecraft.textureManager, texturePack);
        debugExportAtlases();
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
