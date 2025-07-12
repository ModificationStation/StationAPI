package net.modificationstation.stationapi.impl.effect.packet;

import it.unimi.dsi.fastutil.objects.ReferenceIntPair;
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
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsPlayerPacket extends Packet implements ManagedPacket<SendAllEffectsPlayerPacket> {
    public static final PacketType<SendAllEffectsPlayerPacket> TYPE = PacketType.builder(true, false, SendAllEffectsPlayerPacket::new).build();
    private Collection<ReferenceIntPair<Identifier>> effects;
    private int size = 8;
    
    public SendAllEffectsPlayerPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsPlayerPacket(Collection<ReferenceIntPair<Identifier>> effects) {
        this.effects = effects;
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            int count = stream.readShort();
            for (int i = 0; i < count; i++) {
                Identifier id = Identifier.of(stream.readUTF());
                int ticks = stream.readInt();
                effects.add(ReferenceIntPair.of(id, ticks));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeShort(effects.size());
            for (ReferenceIntPair<Identifier> pair : effects) {
                stream.writeUTF(pair.first().toString());
                stream.writeInt(pair.secondInt());
            }
            size = stream.size();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
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
        for (ReferenceIntPair<Identifier> pair : effects) {
            EntityEffect.Factory<?> factory = EntityEffectTypeRegistry.INSTANCE.getOrEmpty(pair.first())
                    .orElseThrow().factory;
            if (factory == null) {
                throw new RuntimeException("Effect with ID " + pair.first() + " is not registered");
            }
            EntityEffect<?> effect = factory.create(player, pair.secondInt());
            player.addEffect(effect);
        }
    }

    public @NotNull PacketType<SendAllEffectsPlayerPacket> getType() {
        return TYPE;
    }
}
