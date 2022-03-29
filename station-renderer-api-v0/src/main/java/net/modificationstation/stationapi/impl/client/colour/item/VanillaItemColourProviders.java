package net.modificationstation.stationapi.impl.client.colour.item;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.client.colour.block.BlockColours;
import net.modificationstation.stationapi.api.client.colour.item.ItemColours;
import net.modificationstation.stationapi.api.client.event.colour.item.ItemColoursRegisterEvent;
import net.modificationstation.stationapi.api.item.ItemConvertible;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class VanillaItemColourProviders {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerItemColours(ItemColoursRegisterEvent event) {
        BlockColours blockColours = event.getBlockColours();
        ItemColours itemColours = event.getItemColours();
        itemColours.register((stack, tintIndex) -> {
            BlockState blockState = ((BlockStateHolder) BlockBase.BY_ID[stack.itemId]).getDefaultState();
            return blockColours.getColour(blockState, null, null, tintIndex);
        }, (ItemConvertible) BlockBase.GRASS, (ItemConvertible) BlockBase.TALLGRASS, (ItemConvertible) BlockBase.LEAVES);
    }
}
