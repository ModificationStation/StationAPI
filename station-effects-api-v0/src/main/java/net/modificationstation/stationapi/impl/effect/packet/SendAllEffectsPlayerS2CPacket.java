package net.modificationstation.stationapi.impl.effect.packet;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.impl.effect.StationEffectsEntityImpl;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class SendAllEffectsPlayerS2CPacket extends Packet implements ManagedPacket<SendAllEffectsPlayerS2CPacket> {
    public static final PacketType<SendAllEffectsPlayerS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsPlayerS2CPacket::new).build();

    private final Collection<Either<EntityEffect<?>, Pair<EntityEffectType<?>, NbtCompound>>> effects;
    
    private SendAllEffectsPlayerS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsPlayerS2CPacket(Collection<EntityEffect<?>> effects) {
        this.effects = effects
                .stream()
                .map(Either::<EntityEffect<?>, Pair<EntityEffectType<?>, NbtCompound>>left)
                .toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
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
        PlayerEntity player = PlayerHelper.getPlayerFromPacketHandler(networkHandler);
        effects.forEach(either -> {
            var effect = either.map(
                    Function.identity(),
                    pair -> {
                        var e = pair.getFirst().factory.create(player, 0);
                        e.read(pair.getSecond());
                        return e;
                    }
            );
            ((StationEffectsEntityImpl) player).stationapi_addEffect(effect, false);
        });
    }
    
    @Override
    public int size() {
        return 0;
    }

    public @NotNull PacketType<SendAllEffectsPlayerS2CPacket> getType() {
        return TYPE;
    }
}
