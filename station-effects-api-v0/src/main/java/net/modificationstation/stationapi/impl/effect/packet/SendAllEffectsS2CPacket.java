package net.modificationstation.stationapi.impl.effect.packet;

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
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsS2CPacket extends Packet implements ManagedPacket<SendAllEffectsS2CPacket> {
    public static final PacketType<SendAllEffectsS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsS2CPacket::new).build();

    private int entityId;
    private final Collection<Pair<EntityEffectType<?>, NbtCompound>> effects;
    
    private SendAllEffectsS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsS2CPacket(int entityId, Collection<EntityEffect<?>> effects) {
        this.entityId = entityId;
        this.effects = effects.stream().map(effect -> {
            var nbt = new NbtCompound();
            effect.write(nbt);
            return Pair.<EntityEffectType<?>, NbtCompound>of(effect.getType(), nbt);
        }).toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityId = stream.readInt();
            int count = stream.readShort();
            for (int i = 0; i < count; i++)
                effects.add(Pair.of(
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
            stream.writeInt(entityId);
            stream.writeShort(effects.size());
            for (Pair<EntityEffectType<?>, NbtCompound> pair : effects) {
                stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(pair.getFirst()));
                NbtIo.write(pair.getSecond(), stream);
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
        effects.forEach(pair -> {
            var effect = pair.getFirst().factory.create(entity, 0);
            effect.read(pair.getSecond());
            entity.addEffect(effect);
            effect.onAdded(false);
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
