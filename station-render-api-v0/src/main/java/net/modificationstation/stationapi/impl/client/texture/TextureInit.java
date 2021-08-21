package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.JsonModelAtlas;
import net.modificationstation.stationapi.api.client.texture.atlas.SquareAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TextureInit {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        //noinspection deprecation
        TextureManager textureManager = ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager;
        TextureBinder textureBinder;
        textureManager.addTextureBinder(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture), new StationStillWaterTextureBinder(), "/custom_water_still.png"));
        textureBinder = new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture + 1), new StationFlowingWaterTextureBinder(), "/custom_water_flowing.png");
        textureBinder.textureSize = 2;
        textureManager.addTextureBinder(textureBinder);
        textureManager.addTextureBinder(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture), new StationStillLavaTextureBinder(), "/custom_lava_still.png"));
        textureBinder = new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture + 1), new StationFlowingLavaTextureBinder(), "/custom_lava_flowing.png");
        textureBinder.textureSize = 2;
        textureManager.addTextureBinder(textureBinder);
        textureManager.addTextureBinder(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FIRE.texture), new StationFireTextureBinder(0), "/custom_fire_e_w.png"));
        textureManager.addTextureBinder(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FIRE.texture + 16), new StationFireTextureBinder(1), "/custom_fire_n_s.png"));
        textureManager.addTextureBinder(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.PORTAL.texture), new StationPortalTextureBinder(), "/custom_portal.png"));
        textureManager.addTextureBinder(new StationCompassTextureBinder());
        textureManager.addTextureBinder(new StationClockTextureBinder());
        JSON_MISSING = JsonModelAtlas.STATION_JSON_MODELS.addTexture(ResourceManager.parsePath(of(MODID, "missing"), "/textures", "png"));
    }

    public static Atlas.Texture JSON_MISSING;
}
