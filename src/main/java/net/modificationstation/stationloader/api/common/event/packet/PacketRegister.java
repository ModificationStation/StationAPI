package net.modificationstation.stationloader.api.common.event.packet;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.packet.CustomData;
import net.modificationstation.stationloader.api.common.registry.ModIDRegistry;
import uk.co.benjiweber.expressions.functions.QuadConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface PacketRegister {

    ModEvent<PacketRegister> EVENT = new ModEvent<>(PacketRegister.class, listeners ->
            (register, customDataPackets) -> {
                Map<String, Map<String, BiConsumer<PlayerBase, CustomData>>> packets = ModIDRegistry.packet;
                String modid;
                for (PacketRegister event : listeners) {
                    modid = PacketRegister.EVENT.getListenerContainer(event).getMetadata().getId();
                    if (!packets.containsKey(modid))
                        packets.put(modid, new HashMap<>());
                    customDataPackets = packets.get(modid);
                    event.registerPackets(register, customDataPackets);
                }
            });

    void registerPackets(QuadConsumer<Integer, Boolean, Boolean, Class<? extends AbstractPacket>> register, Map<String, BiConsumer<PlayerBase, CustomData>> customDataPackets);
}
