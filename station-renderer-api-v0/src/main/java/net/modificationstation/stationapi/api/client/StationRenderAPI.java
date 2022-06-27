package net.modificationstation.stationapi.api.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.colour.block.BlockColors;
import net.modificationstation.stationapi.api.client.colour.item.ItemColors;
import net.modificationstation.stationapi.api.client.render.model.BakedModelManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StationRenderAPI {

    private static final BlockColors BLOCK_COLOURS = BlockColors.create();

    private static final ItemColors ITEM_COLOURS = ItemColors.create(BLOCK_COLOURS);

    @SuppressWarnings("deprecation")
    private static final BakedModelManager BAKED_MODEL_MANAGER = new BakedModelManager(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, BLOCK_COLOURS, 0);

    public static BlockColors getBlockColours() {
        return BLOCK_COLOURS;
    }

    public static BakedModelManager getBakedModelManager() {
        return BAKED_MODEL_MANAGER;
    }

    public static ItemColors getItemColours() {
        return ITEM_COLOURS;
    }
}
