package net.modificationstation.stationapi.impl.tag.conditional;

import com.mojang.serialization.Codec;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class ItemTagConditionsImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerConditions(ItemRegistryEvent event) {
        event.registry
                .buildItemTagCondition(
                        NAMESPACE.id("item_damage"),
                        Codec.INT.fieldOf("damage"),
                        (damage, ctx) -> ctx.hasDamage() && ctx.damage() == damage
                )
                .shorthand(
                        Pattern.compile("@(\\d+)"),
                        dynamic -> dynamic.emptyMap().set(
                                "damage", dynamic.createInt(Integer.parseInt(dynamic.asString("0")))
                        )
                )
                .register();
    }
}
