package net.modificationstation.stationapi.impl.effect.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.effects.AccessorClientNetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectAddRemovePacket extends Packet implements ManagedPacket<EffectAddRemovePacket> {
    public static final PacketType<EffectAddRemovePacket> TYPE = PacketType.builder(true, false, EffectAddRemovePacket::new).build();
    private Identifier effectID;
    private int entityID;
    private int ticks;
    private int size = 11;
    
    public EffectAddRemovePacket() {}
    
    public EffectAddRemovePacket(int entityID, Identifier effectID, int ticks) {
        this.effectID = effectID;
        this.entityID = entityID;
        this.ticks = ticks;
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityID = stream.readInt();
            effectID = Identifier.of(stream.readUTF());
            ticks = stream.readInt();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityID);
            stream.writeUTF(effectID.toString());
            stream.writeInt(ticks);
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
        if (ticks != 0) entity.addEffect(effectID, ticks);
        else entity.removeEffect(effectID);
    }
    
    @Override
    public int size() {
        return size;
    }

    public @NotNull PacketType<EffectAddRemovePacket> getType() {
        return TYPE;
    }
}
