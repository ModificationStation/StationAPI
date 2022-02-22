package net.modificationstation.stationapi.api.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StationRenderAPI {

    private static final BlockColours BLOCK_COLOURS = BlockColours.create();

    @SuppressWarnings("deprecation")
    private static BakedModelManager BAKED_MODEL_MANAGER = new BakedModelManager(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, BLOCK_COLOURS, 0);

    public static BlockColours getBlockColours() {
        return BLOCK_COLOURS;
    }

    public static BakedModelManager getBakedModelManager() {
        return BAKED_MODEL_MANAGER;
    }
}
