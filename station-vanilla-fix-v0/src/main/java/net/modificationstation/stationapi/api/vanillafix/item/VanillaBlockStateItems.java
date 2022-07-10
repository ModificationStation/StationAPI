package net.modificationstation.stationapi.api.vanillafix.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Dye;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.template.item.BlockStateItem;
import net.modificationstation.stationapi.api.vanillafix.block.VanillaBlockProperties;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class VanillaBlockStateItems {

    // TODO: make this use separate blocks rather than blockstates
    public static BlockStateItem
            BLACK_WOOL,
            RED_WOOL,
            GREEN_WOOL,
            BROWN_WOOL,
            BLUE_WOOL,
            PURPLE_WOOL,
            CYAN_WOOL,
            LIGHT_GRAY_WOOL,
            GRAY_WOOL,
            PINK_WOOL,
            LIME_WOOL,
            YELLOW_WOOL,
            LIGHT_BLUE_WOOL,
            MAGENTA_WOOL,
            ORANGE_WOOL,
            WHITE_WOOL;

    public static BlockStateItem getWoolByColor(int color) {
        return switch (color) {
            case 0 -> VanillaBlockStateItems.BLACK_WOOL;
            case 1 -> VanillaBlockStateItems.RED_WOOL;
            case 2 -> VanillaBlockStateItems.GREEN_WOOL;
            case 3 -> VanillaBlockStateItems.BROWN_WOOL;
            case 4 -> VanillaBlockStateItems.BLUE_WOOL;
            case 5 -> VanillaBlockStateItems.PURPLE_WOOL;
            case 6 -> VanillaBlockStateItems.CYAN_WOOL;
            case 7 -> VanillaBlockStateItems.LIGHT_GRAY_WOOL;
            case 8 -> VanillaBlockStateItems.GRAY_WOOL;
            case 9 -> VanillaBlockStateItems.PINK_WOOL;
            case 10 -> VanillaBlockStateItems.LIME_WOOL;
            case 11 -> VanillaBlockStateItems.YELLOW_WOOL;
            case 12 -> VanillaBlockStateItems.LIGHT_BLUE_WOOL;
            case 13 -> VanillaBlockStateItems.MAGENTA_WOOL;
            case 14 -> VanillaBlockStateItems.ORANGE_WOOL;
            case 15 -> VanillaBlockStateItems.WHITE_WOOL;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlockStateItems(ItemRegistryEvent event) {
        ItemBase.byId[BlockBase.WOOL.id] = null;
        BLACK_WOOL = registerWool(0);
        RED_WOOL = registerWool(1);
        GREEN_WOOL = registerWool(2);
        BROWN_WOOL = registerWool(3);
        BLUE_WOOL = registerWool(4);
        PURPLE_WOOL = registerWool(5);
        CYAN_WOOL = registerWool(6);
        LIGHT_GRAY_WOOL = registerWool(7);
        GRAY_WOOL = registerWool(8);
        PINK_WOOL = registerWool(9);
        LIME_WOOL = registerWool(10);
        YELLOW_WOOL = registerWool(11);
        LIGHT_BLUE_WOOL = registerWool(12);
        MAGENTA_WOOL = registerWool(13);
        ORANGE_WOOL = registerWool(14);
        WHITE_WOOL = registerWool(15);
    }

    private static BlockStateItem registerWool(int colorIndex) {
        String color = Dye.NAMES[colorIndex];
        String colorNew = switch (color) {
            case "silver" -> "light_gray";
            case "lightBlue" -> "light_blue";
            default -> color;
        };
        return (BlockStateItem) register(colorNew + "_wool", ((BlockStateHolder) BlockBase.WOOL).getDefaultState().with(VanillaBlockProperties.COLOR, VanillaBlockProperties.Color.valueOf(colorNew.toUpperCase()))).setTranslationKey("cloth." + color);
    }

    private static BlockStateItem register(String id, BlockState state) {
        return new BlockStateItem(of(id), state);
    }
}
