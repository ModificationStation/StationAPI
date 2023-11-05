package net.modificationstation.stationapi.impl.entity.player;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.item.CustomReachProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class ItemCustomReachImpl {
    @EventListener
    private static void getReach(PlayerEvent.Reach event) {
        ItemStack stack = event.player.getHand();
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof CustomReachProvider provider)
                event.currentReach = provider.getReach(stack, event.player, event.type, event.currentReach);
        }
    }
}