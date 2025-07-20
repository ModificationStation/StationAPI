package net.modificationstation.stationapi.impl.effect.packet;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
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
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsPlayerS2CPacket extends Packet implements ManagedPacket<SendAllEffectsPlayerS2CPacket> {
    public static final PacketType<SendAllEffectsPlayerS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsPlayerS2CPacket::new).build();

    private final Collection<Pair<EntityEffectType<?>, NbtCompound>> effects;
    
    private SendAllEffectsPlayerS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsPlayerS2CPacket(Collection<EntityEffect<?>> effects) {
        this.effects = effects.stream().map(effect -> Pair
                .<EntityEffectType<?>, NbtCompound>of(effect.getType(), effect.write())
        ).toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
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
            stream.writeShort(effects.size());
            for (var pair : effects) {
                stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(pair.getFirst()));
                NbtIo.write(pair.getSecond(), stream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        @SuppressWarnings("deprecation") PlayerEntity player = SideUtil.get(
                () -> ((Minecraft) FabricLoader.getInstance().getGameInstance()).player,
                () -> null
        );
        effects.forEach(pair -> {
            var effect = pair.getFirst().factory.create(player, 0);
            effect.read(pair.getSecond());
            player.addEffect(effect);
            effect.onAdded();
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
