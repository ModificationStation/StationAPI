package net.modificationstation.stationapi.api.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.colour.item.ItemColours;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StationRenderAPI {

    private static final BlockColours BLOCK_COLOURS = BlockColours.create();

    private static final ItemColours ITEM_COLOURS = ItemColours.create(BLOCK_COLOURS);

    @SuppressWarnings("deprecation")
    private static final BakedModelManager BAKED_MODEL_MANAGER = new BakedModelManager(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, BLOCK_COLOURS, 0);

    public static BlockColours getBlockColours() {
        return BLOCK_COLOURS;
    }

    public static BakedModelManager getBakedModelManager() {
        return BAKED_MODEL_MANAGER;
    }

    public static ItemColours getItemColours() {
        return ITEM_COLOURS;
    }
}
