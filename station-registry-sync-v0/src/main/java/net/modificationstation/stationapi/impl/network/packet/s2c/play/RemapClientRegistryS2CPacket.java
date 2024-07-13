package net.modificationstation.stationapi.impl.network.packet.s2c.play;

import it.unimi.dsi.fastutil.objects.Reference2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import lombok.val;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.network.RegistryPacketHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RemapClientRegistryS2CPacket extends Packet implements ManagedPacket<RemapClientRegistryS2CPacket> {
    public static final PacketType<RemapClientRegistryS2CPacket> TYPE = new PacketType<>(true, false, RemapClientRegistryS2CPacket::new);

    public Reference2ReferenceMap<Identifier, Reference2IntMap<Identifier>> map;

    private RemapClientRegistryS2CPacket() {}

    @Environment(EnvType.SERVER)
    public RemapClientRegistryS2CPacket(Reference2ReferenceMap<Identifier, Reference2IntMap<Identifier>> map) {
        this.map = map;
    }

    @Override
    public void read(DataInputStream stream) {
        val map = new Reference2ReferenceLinkedOpenHashMap<Identifier, Reference2IntMap<Identifier>>();
        try {
            val mapSize = stream.readInt();
            for (int i = 0; i < mapSize; i++) {
                val registryId = Identifier.tryParse(readString(stream, 32767));
                val registryMapping = new Reference2IntLinkedOpenHashMap<Identifier>();
                val registryMappingSize = stream.readInt();
                for (int i1 = 0; i1 < registryMappingSize; i1++)
                    registryMapping.put(Identifier.tryParse(readString(stream, 32767)), stream.readInt());
                map.put(registryId, registryMapping);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.map = map;
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(map.size());
            map.forEach((registryId, registryMapping) -> {
                writeString(registryId.toString(), stream);
                try {
                    stream.writeInt(registryMapping.size());
                    registryMapping.forEach((identifier, rawId) -> {
                        writeString(identifier.toString(), stream);
                        try {
                            stream.writeInt(rawId);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler arg) {
        ((RegistryPacketHandler) arg).onRemapClientRegistry(this);
    }

    @Override
    public int size() {
        return map.entrySet()
                .stream()
                .mapToInt(entry ->
                        Short.BYTES + entry.getKey().toString().length() * 2
                                + entry.getValue().reference2IntEntrySet()
                                .stream()
                                .mapToInt(registryEntry -> Short.BYTES + registryEntry.getKey().toString().length() * 2 + Integer.BYTES)
                                .sum())
                .sum();
    }

    @Override
    public @NotNull PacketType<RemapClientRegistryS2CPacket> getType() {
        return TYPE;
    }
}
