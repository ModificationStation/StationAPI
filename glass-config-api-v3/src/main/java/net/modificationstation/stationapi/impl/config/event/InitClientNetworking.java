package net.modificationstation.stationapi.impl.config.event;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.modificationstation.stationapi.api.client.event.network.MultiplayerLogoutEvent;
import net.modificationstation.stationapi.api.client.event.network.ServerLoginSuccessEvent;
import net.modificationstation.stationapi.api.config.ConfigRoot;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.network.ModdedPacketHandler;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.ReflectionHelper;
import net.modificationstation.stationapi.impl.config.EventStorage;
import net.modificationstation.stationapi.impl.config.GCCore;
import net.modificationstation.stationapi.impl.config.object.ConfigCategoryHandler;
import net.modificationstation.stationapi.impl.config.object.ConfigEntryHandler;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings("deprecation")
public class InitClientNetworking {

    @Entrypoint.Namespace
    private final Namespace namespace = Null.get();

    @EventListener
    private void registerNetworkShit(MessageListenerRegistryEvent event) {
        Registry.register(event.registry, Identifier.of(namespace, "config_sync"), (playerBase, message) -> {
            GCCore.log("Got config from server!");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.bytes);
            NbtCompound nbtCompound = NbtIo.readCompressed(byteArrayInputStream);
            new ArrayList<>(GCCore.MOD_CONFIGS.keySet()).stream().map(Identifier::toString).filter(nbtCompound::contains).forEach(namespace -> GCCore.loadServerConfig(namespace, nbtCompound.getString(namespace))); // oneliner go brrrrrrr

            FabricLoader.getInstance().getEntrypointContainers(GCCore.NAMESPACE.getMetadata().getId(), Object.class).forEach((entrypointContainer -> {
            if (EventStorage.POST_LOAD_LISTENERS.containsKey(entrypointContainer.getProvider().getMetadata().getId())) {
                EventStorage.POST_LOAD_LISTENERS.get(entrypointContainer.getProvider().getMetadata().getId()).getEntrypoint().PostConfigLoaded(EventStorage.EventSource.SERVER_JOIN | EventStorage.EventSource.MODDED_SERVER_JOIN);
            }}));
        });
    }

    @EventListener
    private void onClientDisconnect(MultiplayerLogoutEvent event) {
        GCCore.log("Unloading server synced config!");
        FabricLoader.getInstance().getEntrypointContainers(GCCore.NAMESPACE.getMetadata().getId(), Object.class).forEach((entrypointContainer -> {
            try {
                for (Field field : ReflectionHelper.getFieldsWithAnnotation(entrypointContainer.getEntrypoint().getClass(), ConfigRoot.class)) {
                    Identifier configID = Identifier.of(entrypointContainer.getProvider().getMetadata().getId() + ":" + field.getAnnotation(ConfigRoot.class).value());
                    GCCore.loadModConfig(entrypointContainer.getEntrypoint(), entrypointContainer.getProvider(), field, configID, null);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }


    @EventListener
    private void onClientJoin(ServerLoginSuccessEvent event) {
        if(!((ModdedPacketHandler) event.clientNetworkHandler).isModded()) {
            GCCore.MOD_CONFIGS.forEach((identifier, configRootEntry) -> recursiveTriggerVanillaBehavior(configRootEntry.configCategoryHandler()));
        }
    }

    private void recursiveTriggerVanillaBehavior(ConfigCategoryHandler configCategory) {
        configCategory.values.forEach((aClass, configBase) -> {
            if(configBase.getClass().isAssignableFrom(ConfigCategoryHandler.class)) {
                recursiveTriggerVanillaBehavior((ConfigCategoryHandler) configBase);
            }
            else {
                ((ConfigEntryHandler<?>) configBase).vanillaServerBehavior();
            }
        });
    }
}
