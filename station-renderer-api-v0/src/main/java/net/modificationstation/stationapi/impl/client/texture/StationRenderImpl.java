package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.TexturePack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourcesReloadEvent;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.texture.TextureUtil;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.FakeResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceHelper;
import net.modificationstation.stationapi.api.resource.SynchronousResourceReloader;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class StationRenderImpl {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    @Entrypoint.Logger("StationRenderer|API")
    public static final Logger LOGGER = Null.get();

    private static final boolean DEBUG_EXPORT_ATLASES = Boolean.parseBoolean(System.getProperty(StationAPI.MODID + ".debug.export_atlases", "false"));

    public static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerReloaders(ClientResourceReloaderRegisterEvent event) {
        Function<ExpandableAtlas, Function<String, InputStream>> providerFactory = expandableAtlas -> path -> {
            try {
                Identifier identifier = ResourceHelper.ASSETS.toId(path, StationAPI.MODID + "/textures", "png");
                if (expandableAtlas.slicedSpritesheetView.containsKey(identifier)) {
                    BufferedImage image = expandableAtlas.slicedSpritesheetView.get(identifier);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(image, "png", outputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return new ByteArrayInputStream(outputStream.toByteArray());
                }
            } catch (IllegalArgumentException ignored) {}
            return null;
        };
        FakeResourceManager.registerProvider(providerFactory.apply(TERRAIN));
        FakeResourceManager.registerProvider(providerFactory.apply(GUI_ITEMS));
        event.resourceManager.registerReloader(StationRenderAPI.getBakedModelManager());
        event.resourceManager.registerReloader((SynchronousResourceReloader) manager -> ItemModels.reloadModelsAll());
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 + Integer.MAX_VALUE / 8)
    private static void init(ClientResourcesReloadEvent event) {
        TERRAIN = new ExpandableAtlas(of("textures/atlas/terrain.png"));
        GUI_ITEMS = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
        TERRAIN.addSpritesheet("/terrain.png", 16, TerrainHelper.INSTANCE);
        GUI_ITEMS.addSpritesheet("/gui/items.png", 16, GuiItemsHelper.INSTANCE);
        TextureUtil.maxSupportedTextureSize();
    }

    @EventListener(numPriority = Integer.MIN_VALUE / 2 + Integer.MIN_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void stitchExpandableAtlasesPostInit(ClientResourcesReloadEvent event) {
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
