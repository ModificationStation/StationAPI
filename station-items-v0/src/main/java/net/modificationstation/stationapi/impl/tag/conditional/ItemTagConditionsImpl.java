package net.modificationstation.stationapi.impl.tag.conditional;

import com.mojang.serialization.Codec;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.tag.conditional.ItemTagConditions;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ItemTagConditionsImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerConditions(ItemRegistryEvent event) {
        event.registry.registerTagCondition(
                NAMESPACE.id("item_damage"),
                Codec.INT.fieldOf("damage"),
                (damage, ctx) -> {
                    Integer ctxDamage = ctx.get(ItemTagConditions.ITEM_DAMAGE);
                    return ctxDamage != null && ctxDamage.equals(damage);
                }
        );
    }
}
