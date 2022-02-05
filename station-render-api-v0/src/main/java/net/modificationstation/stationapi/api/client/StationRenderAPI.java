package net.modificationstation.stationapi.api.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;

public class StationRenderAPI {

    @SuppressWarnings("deprecation")
    public static BakedModelManager BAKED_MODEL_MANAGER = new BakedModelManager(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, BlockColours.create(), 0);
}
