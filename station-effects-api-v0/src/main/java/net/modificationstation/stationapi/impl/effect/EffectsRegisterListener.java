package net.modificationstation.stationapi.impl.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.effect.EntityEffectTypeRegistryEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.effect.packet.*;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class EffectsRegisterListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void onInit(AfterBlockAndItemRegisterEvent event) {
        StationAPI.EVENT_BUS.post(new EntityEffectTypeRegistryEvent());
    }

    @EventListener(phase = StationAPI.INTERNAL_PHASE)
    private static void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE)
                .accept("effects/effect_add", EffectAddS2CPacket.TYPE)
                .accept("effects/effect_remove", EffectRemoveS2CPacket.TYPE)
                .accept("effects/effect_remove_all", EffectRemoveAllS2CPacket.TYPE)
                .accept("effects/send_all_effects", SendAllEffectsS2CPacket.TYPE)
                .accept("effects/send_all_effects_player", SendAllEffectsPlayerS2CPacket.TYPE);
    }
}
