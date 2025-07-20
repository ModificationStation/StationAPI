package net.modificationstation.stationapi.impl.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.effect.EffectRegistryEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.effect.packet.*;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class EffectsRegisterListener {
    @EventListener(phase = InitEvent.POST_INIT_PHASE)
    public void onInit(InitEvent event) {
        StationAPI.EVENT_BUS.post(new EffectRegistryEvent());
    }

    @EventListener
    public static void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE)
                .accept("effects/effect_add", EffectAddS2CPacket.TYPE)
                .accept("effects/effect_remove", EffectRemoveS2CPacket.TYPE)
                .accept("effects/effect_remove_all", EffectRemoveAllS2CPacket.TYPE)
                .accept("effects/send_all_effects", SendAllEffectsS2CPacket.TYPE)
                .accept("effects/send_all_effects_player", SendAllEffectsPlayerS2CPacket.TYPE);
    }
}
