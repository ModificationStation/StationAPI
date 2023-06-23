package net.modificationstation.stationapi.impl.entity.player;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.entity.player.PlayerEvent;
import net.modificationstation.stationapi.api.item.CustomReachProvider;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ItemCustomReachImpl {

    @EventListener
    private static void getReach(PlayerEvent.Reach event) {
        ItemInstance itemInstance = event.player.getHeldItem();
        if (itemInstance != null) {
            ItemBase item = itemInstance.getType();
            if (item instanceof CustomReachProvider provider)
                event.currentReach = provider.getReach(itemInstance, event.player, event.type, event.currentReach);
        }
    }
}