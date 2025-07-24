package net.modificationstation.stationapi.impl.effect.packet;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectTypeRegistry;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.mixin.effects.ClientNetworkHandlerAccessor;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EffectAddS2CPacket extends Packet implements ManagedPacket<EffectAddS2CPacket> {
    public static final PacketType<EffectAddS2CPacket> TYPE = PacketType
            .builder(true, false, EffectAddS2CPacket::new).build();

    private int entityId;
    private EntityEffect<?> effect;
    
    private EffectAddS2CPacket() {}
    
    public EffectAddS2CPacket(int entityId, EntityEffect<?> effect) {
        this.entityId = entityId;
        this.effect = effect;
    }
    
    @Override
    public void read(DataInputStream stream) {
        try {
            entityId = stream.readInt();
            //noinspection deprecation
            effect = EntityEffectTypeRegistry.INSTANCE.getOrThrow(stream.readInt()).factory.create(
                    SideUtil.get(
                            () -> ((ClientNetworkHandlerAccessor) ((Minecraft) FabricLoader.getInstance()
                                    .getGameInstance()).getNetworkHandler()).stationapi_getEntityByID(entityId),
                            () -> null
                    ),
                    0
            );
            effect.read(NbtIo.read(stream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void write(DataOutputStream stream) {
        try {
            stream.writeInt(entityId);
            stream.writeInt(EntityEffectTypeRegistry.INSTANCE.getRawId(effect.getType()));
            var nbt = new NbtCompound();
            effect.write(nbt);
            NbtIo.write(nbt, stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void apply(NetworkHandler networkHandler) {
        ((ClientNetworkHandlerAccessor) networkHandler).stationapi_getEntityByID(entityId).addEffect(effect);
        effect.onAdded(true);
    }
    
    @Override
    public int size() {
        return 0;
    }

    public @NotNull PacketType<EffectAddS2CPacket> getType() {
        return TYPE;
    }
}
