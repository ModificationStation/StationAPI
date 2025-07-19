package net.modificationstation.stationapi.impl.effect.packet;

import it.unimi.dsi.fastutil.objects.ReferenceIntPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.mixin.effects.AccessorClientNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsS2CPacket extends Packet implements ManagedPacket<SendAllEffectsS2CPacket> {
    private record IdAndTicksPair(int id, int ticks) {}

    public static final PacketType<SendAllEffectsS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsS2CPacket::new).build();

    private final Collection<IdAndTicksPair> effects;
    private int entityID;
    private int size = 8;
    
    public SendAllEffectsS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsS2CPacket(int entityID, Collection<ReferenceIntPair<EntityEffectType<?>>> effects) {
        this.entityID = entityID;
        this.effects = effects
                .stream()
                .map(pair -> new IdAndTicksPair(
                        EntityEffectTypeRegistry.INSTANCE.getRawId(pair.first()),
                        pair.secondInt()
                ))
                .toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityID = stream.readInt();
            int count = stream.readShort();
            for (int i = 0; i < count; i++)
                effects.add(new IdAndTicksPair(stream.readInt(), stream.readInt()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityID);
            stream.writeShort(effects.size());
            for (IdAndTicksPair pair : effects) {
                stream.writeInt(pair.id);
                stream.writeInt(pair.ticks);
            }
            size = stream.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        AccessorClientNetworkHandler handler = (AccessorClientNetworkHandler) networkHandler;
        Entity entity = handler.stationapi_getEntityByID(entityID);
        for (IdAndTicksPair pair : effects)
            entity.addEffect(EntityEffectTypeRegistry.INSTANCE.getOrThrow(pair.id).factory.create(entity, pair.ticks));
    }
    
    @Override
    public int size() {
        return size;
    }

    public @NotNull PacketType<SendAllEffectsS2CPacket> getType() {
        return TYPE;
    }
}
