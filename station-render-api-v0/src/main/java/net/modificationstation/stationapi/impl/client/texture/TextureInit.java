package net.modificationstation.stationapi.impl.client.texture;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.SquareAtlas;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TextureInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void init(TextureRegisterEvent event) {
        //noinspection deprecation
        TextureManager textureManager = ((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager;
        TextureBinder textureBinder;
        textureManager.add(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture), new StationStillWaterTextureBinder(), "/custom_water_still.png"));
        textureBinder = new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_WATER.texture + 1), new StationFlowingWaterTextureBinder(), "/custom_water_flowing.png");
        textureBinder.textureSize = 2;
        textureManager.add(textureBinder);
        textureManager.add(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture), new StationStillLavaTextureBinder(), "/custom_lava_still.png"));
        textureBinder = new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FLOWING_LAVA.texture + 1), new StationFlowingLavaTextureBinder(), "/custom_lava_flowing.png");
        textureBinder.textureSize = 2;
        textureManager.add(textureBinder);
        textureManager.add(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FIRE.texture), new StationFireTextureBinder(0), "/custom_fire_e_w.png"));
        textureManager.add(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.FIRE.texture + 16), new StationFireTextureBinder(1), "/custom_fire_n_s.png"));
        textureManager.add(new StationVanillaTextureBinder(SquareAtlas.TERRAIN.getTexture(BlockBase.PORTAL.texture), new StationPortalTextureBinder(), "/custom_portal.png"));
    }
}
