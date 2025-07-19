package net.modificationstation.stationapi.impl.effect.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.mixin.effects.AccessorClientNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectRemoveAllPacket extends Packet implements ManagedPacket<EffectRemoveAllPacket> {
    public static final PacketType<EffectRemoveAllPacket> TYPE = PacketType
            .builder(true, false, EffectRemoveAllPacket::new).build();

    private int entityID;
    
    public EffectRemoveAllPacket() {}
    
    public EffectRemoveAllPacket(int entityID) {
        this.entityID = entityID;
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityID = stream.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
        AccessorClientNetworkHandler handler = (AccessorClientNetworkHandler) networkHandler;
        Entity entity = handler.stationapi_getEntityByID(entityID);
        entity.removeAllEffects();
    }
    
    @Override
    public int size() {
        return 4;
    }

    public @NotNull PacketType<EffectRemoveAllPacket> getType() {
        return TYPE;
    }
}
