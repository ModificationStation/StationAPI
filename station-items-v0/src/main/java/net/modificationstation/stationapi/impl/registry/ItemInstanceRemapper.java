package net.modificationstation.stationapi.impl.registry;

import com.google.common.collect.MapMaker;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.event.registry.PostRegistryRemapEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class ItemInstanceRemapper {

    public static final Map<ItemInstance, Identifier> ITEM_INSTANCE_TO_IDENTIFIER = new MapMaker().weakKeys().makeMap();

    @EventListener(priority = ListenerPriority.HIGH)
    private static void afterRegistryRemap(PostRegistryRemapEvent event) {
        System.out.println("Remap!");
        ITEM_INSTANCE_TO_IDENTIFIER.forEach((itemInstance, identifier) -> {
            System.out.println(itemInstance.itemId + " " + identifier);
            ItemRegistry.INSTANCE.getSerialID(identifier).ifPresent(value -> itemInstance.itemId = value);
            System.out.println(itemInstance.itemId + " " + identifier);
        });
        System.out.println("Remap done!");
    }
}
