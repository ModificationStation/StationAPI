package net.modificationstation.stationapi.impl.effect.packet;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.effect.StationEffectsEntityImpl;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class EffectRemoveAllS2CPacket extends Packet implements ManagedPacket<EffectRemoveAllS2CPacket> {
    public static final PacketType<EffectRemoveAllS2CPacket> TYPE = PacketType
            .builder(true, false, EffectRemoveAllS2CPacket::new).build();

    private Either<Entity, Integer> entity;
    
    private EffectRemoveAllS2CPacket() {}
    
    public EffectRemoveAllS2CPacket(Entity entity) {
        this.entity = Either.left(entity);
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entity = Either.right(stream.readInt());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entity.map(e -> e.id, Function.identity()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        ((StationEffectsEntityImpl) entity.map(
                Function.identity(),
                entityId -> ((ClientNetworkHandlerAccessor) networkHandler).stationapi_getEntityByID(entityId)
        )).stationapi_removeAllEffects();
    }
    
    @Override
    public int size() {
        return 4;
    }

    public @NotNull PacketType<EffectRemoveAllS2CPacket> getType() {
        return TYPE;
    }
}
