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

    private static final BlockColors BLOCK_COLORS = BlockColors.create();

    private static final ItemColors ITEM_COLORS = ItemColors.create(BLOCK_COLORS);

    @SuppressWarnings("deprecation")
    private static final BakedModelManager BAKED_MODEL_MANAGER = new BakedModelManager(((Minecraft) FabricLoader.getInstance().getGameInstance()).textureManager, BLOCK_COLORS, 0);

    public static BlockColors getBlockColors() {
        return BLOCK_COLORS;
    }

    public static BakedModelManager getBakedModelManager() {
        return BAKED_MODEL_MANAGER;
    }

    public static ItemColors getItemColors() {
        return ITEM_COLORS;
    }
}
