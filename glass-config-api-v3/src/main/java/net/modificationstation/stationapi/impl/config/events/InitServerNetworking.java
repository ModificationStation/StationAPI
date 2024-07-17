package net.modificationstation.stationapi.impl.config.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.modificationstation.stationapi.impl.config.GCCore;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.server.event.network.PlayerLoginEvent;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

import java.io.*;
import java.util.*;

@SuppressWarnings("deprecation")
public class InitServerNetworking {

    public static final WeakHashMap<PlayerEntity, Boolean> GCAPI_PLAYERS = new WeakHashMap<>();

    @Entrypoint.Namespace
    private final Namespace namespace = Null.get();

    @EventListener
    private void doPlayerShit(PlayerLoginEvent event) {
        if (((ModdedPacketHandler) event.player.field_255).isModded()) {
            GCCore.log("Ping successful! Sending config to " + event.player.name);
            GCAPI_PLAYERS.put(event.player, true);
            MessagePacket configSync = new MessagePacket(Identifier.of(namespace, "config_sync"));
            NbtCompound nbtCompound = new NbtCompound();
            GCCore.exportConfigsForServer(nbtCompound);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbtCompound, byteArrayOutputStream);
            configSync.bytes = byteArrayOutputStream.toByteArray();
            PacketHelper.sendTo(event.player, configSync);
        }
    }
}
