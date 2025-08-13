package net.modificationstation.stationapi.impl.effect.packet;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.effect.StationEffectsEntityImpl;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class SendAllEffectsS2CPacket extends Packet implements ManagedPacket<SendAllEffectsS2CPacket> {
    public static final PacketType<SendAllEffectsS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsS2CPacket::new).build();

    private int entityId;
    private final Collection<Either<EntityEffect<?>, Pair<EntityEffectType<?>, NbtCompound>>> effects;
    
    private SendAllEffectsS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsS2CPacket(int entityId, Collection<EntityEffect<?>> effects) {
        this.entityId = entityId;
        this.effects = effects
                .stream()
                .map(Either::<EntityEffect<?>, Pair<EntityEffectType<?>, NbtCompound>>left)
                .toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityId = stream.readInt();
            int count = stream.readShort();
            for (int i = 0; i < count; i++)
                effects.add(Either.right(Pair.of(
                        EntityEffectTypeRegistry.INSTANCE.getOrThrow(stream.readInt()),
                        NbtIo.read(stream)
                )));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityId);
            stream.writeShort(effects.size());
            for (var either : effects) {
                stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(
                        either.map(EntityEffect::getType, Pair::getFirst)
                ));
                NbtIo.write(either.map(
                        effect -> {
                            var nbt = new NbtCompound();
                            effect.write(nbt);
                            return nbt;
                        },
                        Pair::getSecond
                ), stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        var entity = SideUtil.get(
                () -> ((ClientNetworkHandlerAccessor) networkHandler).stationapi_getEntityByID(entityId),
                () -> null
        );
        effects.forEach(either -> {
            var effect = either.map(
                    Function.identity(),
                    pair -> {
                        var e = pair.getFirst().factory.create(entity, 0);
                        e.read(pair.getSecond());
                        return e;
                    }
            );
            ((StationEffectsEntityImpl) entity).stationapi_addEffect(effect, false);
        });
    }
    
    @Override
    public int size() {
        return 0;
    }

    public @NotNull PacketType<SendAllEffectsS2CPacket> getType() {
        return TYPE;
    }
}
