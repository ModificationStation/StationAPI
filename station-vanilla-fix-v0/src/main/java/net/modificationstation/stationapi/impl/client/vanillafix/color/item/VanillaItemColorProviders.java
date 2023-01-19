package net.modificationstation.stationapi.impl.client.vanillafix.color.item;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaItemColorProviders {

//    @EventListener(priority = ListenerPriority.HIGH)
//    private static void registerItemColors(ItemColorsRegisterEvent event) {
//        BlockColors blockColors = event.blockColors;
//        ItemColors itemColors = event.itemColors;
//        itemColors.register((stack, tintIndex) -> {
//            BlockState blockState = ((BlockStateHolder) BlockBase.BY_ID[stack.itemId]).getDefaultState();
//            return blockColors.getColor(blockState, null, null, tintIndex);
//        },
//                (ItemConvertible) BlockBase.GRASS,
//                (ItemConvertible) BlockBase.TALLGRASS,
//                (ItemConvertible) Blocks.OAK_LEAVES,
//                (ItemConvertible) Blocks.SPRUCE_LEAVES,
//                (ItemConvertible) Blocks.BIRCH_LEAVES
//        );
//    }
}
