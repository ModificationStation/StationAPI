package net.modificationstation.stationapi.impl.vanillafix.block;

import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.vanillafix.block.VanillaBlockProperties;
import net.modificationstation.stationapi.api.vanillafix.item.VanillaBlockStateItems;

import java.util.List;

public final class WoolDropImpl {

    // TODO: make this use separate blocks rather than blockstates
    public static List<ItemInstance> drop(Level level, int x, int y, int z, BlockState state, int meta) {
        return List.of(new ItemInstance(switch (state.get(VanillaBlockProperties.COLOR)) {
            case BLACK -> VanillaBlockStateItems.BLACK_WOOL;
            case RED -> VanillaBlockStateItems.RED_WOOL;
            case GREEN -> VanillaBlockStateItems.GREEN_WOOL;
            case BROWN -> VanillaBlockStateItems.BROWN_WOOL;
            case BLUE -> VanillaBlockStateItems.BLUE_WOOL;
            case PURPLE -> VanillaBlockStateItems.PURPLE_WOOL;
            case CYAN -> VanillaBlockStateItems.CYAN_WOOL;
            case LIGHT_GRAY -> VanillaBlockStateItems.LIGHT_GRAY_WOOL;
            case GRAY -> VanillaBlockStateItems.GRAY_WOOL;
            case PINK -> VanillaBlockStateItems.PINK_WOOL;
            case LIME -> VanillaBlockStateItems.LIME_WOOL;
            case YELLOW -> VanillaBlockStateItems.YELLOW_WOOL;
            case LIGHT_BLUE -> VanillaBlockStateItems.LIGHT_BLUE_WOOL;
            case MAGENTA -> VanillaBlockStateItems.MAGENTA_WOOL;
            case ORANGE -> VanillaBlockStateItems.ORANGE_WOOL;
            case WHITE -> VanillaBlockStateItems.WHITE_WOOL;
        }));
    }
}
