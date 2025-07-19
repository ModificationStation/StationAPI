package net.modificationstation.stationapi.impl.effect.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectRemoveAllS2CPacket extends Packet implements ManagedPacket<EffectRemoveAllS2CPacket> {
    public static final PacketType<EffectRemoveAllS2CPacket> TYPE = PacketType
            .builder(true, false, EffectRemoveAllS2CPacket::new).build();

    private int entityID;
    
    public EffectRemoveAllS2CPacket() {}
    
    public EffectRemoveAllS2CPacket(int entityID) {
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
        ClientNetworkHandlerAccessor handler = (ClientNetworkHandlerAccessor) networkHandler;
        Entity entity = handler.stationapi_getEntityByID(entityID);
        entity.removeAllEffects();
    }
    
    @Override
    public int size() {
        return 4;
    }

    public @NotNull PacketType<EffectRemoveAllS2CPacket> getType() {
        return TYPE;
    }
}
