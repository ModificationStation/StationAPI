package net.modificationstation.stationapi.impl.effect.packet;

import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectRemoveS2CPacket extends Packet implements ManagedPacket<EffectRemoveS2CPacket> {
    public static final PacketType<EffectRemoveS2CPacket> TYPE = PacketType
            .builder(true, false, EffectRemoveS2CPacket::new).build();

    private int entityId;
    private EntityEffectType<?> effectType;

    private EffectRemoveS2CPacket() {}

    public EffectRemoveS2CPacket(int entityId, EntityEffectType<?> effectType) {
        this.entityId = entityId;
        this.effectType = effectType;
    }

    @Override
    public void read(DataInputStream stream) {
        try {
            this.entityId = stream.readInt();
            this.effectType = EntityEffectTypeRegistry.INSTANCE.getOrThrow(stream.readInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityId);
            stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(effectType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
        ((ClientNetworkHandlerAccessor) networkHandler).stationapi_getEntityByID(entityId).removeEffect(effectType);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull PacketType<EffectRemoveS2CPacket> getType() {
        return TYPE;
    }
}
