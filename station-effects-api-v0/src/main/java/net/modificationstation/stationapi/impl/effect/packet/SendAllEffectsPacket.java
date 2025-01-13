package net.modificationstation.stationapi.impl.effect.packet;

import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EnitityEffectRegistry;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectFactory;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.effects.AccessorClientNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SendAllEffectsPacket extends Packet implements ManagedPacket<SendAllEffectsPacket> {
    public static final PacketType<SendAllEffectsPacket> TYPE = PacketType.builder(false, true, SendAllEffectsPacket::new).build();
    private Collection<Pair<Identifier, Integer>> effects;
    private int entityID;
    private int size = 8;
    
    public SendAllEffectsPacket() {
        effects = new ArrayList<>();
    }
    
    public SendAllEffectsPacket(int entityID, Collection<Pair<Identifier, Integer>> effects) {
        this.entityID = entityID;
        this.effects = effects;
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityID = stream.readInt();
            int count = stream.readShort();
            for (int i = 0; i < count; i++) {
                Identifier id = Identifier.of(stream.readUTF());
                int ticks = stream.readInt();
                effects.add(Pair.of(id, ticks));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityID);
            stream.writeShort(effects.size());
            for (Pair<Identifier, Integer> pair : effects) {
                stream.writeUTF(pair.first().toString());
                stream.writeInt(pair.second());
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
        AccessorClientNetworkHandler handler = (AccessorClientNetworkHandler) networkHandler;
        Entity entity = handler.stationapi_getEntityByID(entityID);
        for (Pair<Identifier, Integer> pair : effects) {
            EntityEffectFactory factory = EnitityEffectRegistry.INSTANCE.get(pair.first());
            if (factory == null) {
                throw new RuntimeException("Effect with ID " + pair.first() + " is not registered");
            }
            EntityEffect effect = factory.create(pair.first(), entity, pair.second());
            entity.addEffect(effect);
        }
    }
    
    @Override
    public int size() {
        return size;
    }

    public @NotNull PacketType<SendAllEffectsPacket> getType() {
        return TYPE;
    }
}
