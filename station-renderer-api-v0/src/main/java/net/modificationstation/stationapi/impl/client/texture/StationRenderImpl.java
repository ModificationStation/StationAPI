package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.class_285;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.event.resource.AssetsReloadEvent;
import net.modificationstation.stationapi.api.client.event.resource.AssetsResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.render.item.ItemModels;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.client.texture.NativeImageBackedTexture;
import net.modificationstation.stationapi.api.client.texture.SpriteAtlasTexture;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureUtil;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.resource.*;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.profiler.Profiler;
import org.apache.logging.log4j.Logger;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.HIGH;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class StationRenderImpl {

    @Entrypoint.Namespace
    public static final Namespace MODID = Null.get();

    @Entrypoint.Logger("StationRenderer|API")
    public static final Logger LOGGER = Null.get();

    public static ExpandableAtlas getTerrain() {
        return TERRAIN;
    }

    public static ExpandableAtlas getGuiItems() {
        return GUI_ITEMS;
    }

    private static ExpandableAtlas
            TERRAIN,
            GUI_ITEMS;

    @EventListener(priority = HIGH)
    private static void ensureThread(AssetsReloadEvent event) {
        TextureUtil.maxSupportedTextureSize();
    }

    @EventListener
    private static void registerReloaders(AssetsResourceReloaderRegisterEvent event) {
        ResourceManagerHelper helper = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
        helper.registerReloadListener(StationRenderAPI.getBakedModelManager());
        interface SIResourceReloader extends SynchronousResourceReloader, IdentifiableResourceReloadListener {}
        helper.registerReloadListener(new SIResourceReloader() {
            @Override
            public String getName() {
                return "ItemModels";
            }

            @Override
            public Identifier getId() {
                return StationAPI.NAMESPACE.id("private/item_models");
            }

            @Override
            public Collection<Identifier> getDependencies() {
                return Set.of(BakedModelManager.MODELS);
            }

            @Override
            public void reload(ResourceManager manager) {
                ItemModels.reloadModelsAll();
            }
        });
        helper.registerReloadListener(new SIResourceReloader() {
            @Override
            public String getName() {
                return "BlockDestructionTextures";
            }

            @Override
            public Identifier getId() {
                return StationAPI.NAMESPACE.id("private/block_destruction_stage_textures");
            }

            @Override
            public Collection<Identifier> getDependencies() {
                return Set.of(BakedModelManager.MODELS);
            }

            @Override
            public void reload(ResourceManager manager) {
                //noinspection deprecation
                StationTextureManager textureManager = StationTextureManager.get(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager);
                SpriteAtlasTexture atlas = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE);
                for (int i = 0, size = ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.size(); i < size; i++)
                    textureManager.registerTexture(ModelLoader.BLOCK_DESTRUCTION_STAGE_TEXTURES.get(i), new NativeImageBackedTexture(Objects.requireNonNull(atlas.getSprite(ModelLoader.BLOCK_DESTRUCTION_STAGES.get(i))).getContents().getBaseFrame()));
            }
        });
        abstract class SPIResourceReloader<T> extends SinglePreparationResourceReloader<T> implements IdentifiableResourceReloadListener {}
        helper.registerReloadListener(new SPIResourceReloader<BiTuple<ExpandableAtlas, ExpandableAtlas>>() {

            @Override
            public String getName() {
                return "ModTextures";
            }

            @Override
            public Identifier getId() {
                return StationAPI.NAMESPACE.id("private/mod_textures");
            }

            @Override
            public Collection<Identifier> getDependencies() {
                return Set.of(BakedModelManager.MODELS);
            }

            @Override
            protected BiTuple<ExpandableAtlas, ExpandableAtlas> prepare(ResourceManager manager, Profiler profiler) {
                profiler.startTick();
                profiler.push("legacy_atlases");
                ExpandableAtlas
                        terrain = new ExpandableAtlas(of("textures/atlas/terrain.png")),
                        guiItems = new ExpandableAtlas(of("textures/atlas/gui/items.png"));
                terrain.addSpritesheet(16, TerrainHelper.INSTANCE);
                guiItems.addSpritesheet(16, GuiItemsHelper.INSTANCE);
                profiler.pop();
                profiler.endTick();
                return Tuple.tuple(terrain, guiItems);
            }

            @Override
            protected void apply(BiTuple<ExpandableAtlas, ExpandableAtlas> prepared, ResourceManager manager, Profiler profiler) {
                TERRAIN = prepared.one();
                GUI_ITEMS = prepared.two();
                profiler.startTick();
                profiler.push("mod_textures");
                StationAPI.EVENT_BUS.post(TextureRegisterEvent.builder().build());
                //noinspection deprecation
                Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
                class_285 texturePack = minecraft.field_2768.field_1175;
                profiler.swap("texture_binders");
                TERRAIN.registerTextureBinders(minecraft.textureManager, texturePack);
                GUI_ITEMS.registerTextureBinders(minecraft.textureManager, texturePack);
                profiler.pop();
                profiler.endTick();
            }
        });
    }
}
