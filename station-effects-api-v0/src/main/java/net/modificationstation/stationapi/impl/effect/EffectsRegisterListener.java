package net.modificationstation.stationapi.impl.effect;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.effect.EffectRegistryEvent;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.impl.effect.packet.EffectAddRemovePacket;
import net.modificationstation.stationapi.impl.effect.packet.EffectRemoveAllPacket;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPacket;
import net.modificationstation.stationapi.impl.effect.packet.SendAllEffectsPlayerPacket;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class EffectsRegisterListener {
    @EventListener(phase = InitEvent.POST_INIT_PHASE)
    public void onInit(InitEvent event) {
        StationAPI.EVENT_BUS.post(new EffectRegistryEvent());
    }

    @EventListener
    public static void registerPackets(PacketRegisterEvent event){
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("effects/effect_add_remove"), EffectAddRemovePacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("effects/effect_remove_all"), EffectRemoveAllPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("effects/send_all_effects"), SendAllEffectsPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NAMESPACE.id("effects/send_all_effects_player"), SendAllEffectsPlayerPacket.TYPE);
    }
}
