package net.modificationstation.stationapi.impl.effect.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsPlayerS2CPacket extends Packet implements ManagedPacket<SendAllEffectsPlayerS2CPacket> {
    private record IdAndTicksPair(int id, int ticks) {}

    public static final PacketType<SendAllEffectsPlayerS2CPacket> TYPE = PacketType
            .builder(true, false, SendAllEffectsPlayerS2CPacket::new).build();

    private final Collection<IdAndTicksPair> effects;
    private int size = 8;
    
    private SendAllEffectsPlayerS2CPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsPlayerS2CPacket(Collection<EntityEffect<?>> effects) {
        this.effects = effects
                .stream()
                .map(effect -> new IdAndTicksPair(
                        EntityEffectTypeRegistry.INSTANCE.getRawId(effect.getType()),
                        effect.getTicks()
                ))
                .toList();
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
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
        applyEffects();
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Environment(EnvType.CLIENT)
    private void applyEffects() {
        @SuppressWarnings("deprecation")
        PlayerEntity player = ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
        for (IdAndTicksPair pair : effects)
            player.addEffect(EntityEffectTypeRegistry.INSTANCE.getOrThrow(pair.id).factory.create(player, pair.ticks));
    }

    public @NotNull PacketType<SendAllEffectsPlayerS2CPacket> getType() {
        return TYPE;
    }
}
