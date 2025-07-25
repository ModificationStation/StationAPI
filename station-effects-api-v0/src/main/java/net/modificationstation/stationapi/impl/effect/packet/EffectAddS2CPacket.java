package net.modificationstation.stationapi.impl.effect.packet;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.effect.StationEffectsEntityImpl;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class EffectAddS2CPacket extends Packet implements ManagedPacket<EffectAddS2CPacket> {
    public static final PacketType<EffectAddS2CPacket> TYPE = PacketType
            .builder(true, false, EffectAddS2CPacket::new).build();

    private Either<Entity, Integer> entity;
    private Either<EntityEffect<?>, Pair<EntityEffectType<?>, NbtCompound>> effect;
    
    private EffectAddS2CPacket() {}
    
    public EffectAddS2CPacket(Entity entity, EntityEffect<?> effect) {
        this.entity = Either.left(entity);
        this.effect = Either.left(effect);
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entity = Either.right(stream.readInt());
            effect = Either.right(Pair.of(
                    EntityEffectTypeRegistry.INSTANCE.getOrThrow(stream.readInt()),
                    NbtIo.read(stream)
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entity.map(e -> e.id, Function.identity()));
            stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(effect.map(
                    EntityEffect::getType, Pair::getFirst
            )));
            NbtIo.write(effect.map(
                    e -> {
                        var nbt = new NbtCompound();
                        e.write(nbt);
                        return nbt;
                    },
                    Pair::getSecond
            ), stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        Entity e = entity.map(
                Function.identity(),
                entityId -> ((ClientNetworkHandlerAccessor) networkHandler).stationapi_getEntityByID(entityId)
        );
        ((StationEffectsEntityImpl) e).stationapi_addEffect(effect.map(
                Function.identity(),
                pair -> {
                    var effect = pair.getFirst().factory.create(e, 0);
                    effect.read(pair.getSecond());
                    return effect;
                }
        ), true);
    }
    
    @Override
    public int size() {
        return 0;
    }

    public @NotNull PacketType<EffectAddS2CPacket> getType() {
        return TYPE;
    }
}
