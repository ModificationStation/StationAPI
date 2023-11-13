package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import uk.co.benjiweber.expressions.function.TriFunction;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class GuiClientNetworkHandler {

    @EventListener
    private static void registerMessageListeners(MessageListenerRegistryEvent event) {
        Registry.register(event.registry, NAMESPACE.id("open_gui"), GuiClientNetworkHandler::handleGui);
        StationAPI.EVENT_BUS.post(new GuiHandlerRegistryEvent());
    }

    private static void handleGui(PlayerEntity player, MessagePacket message) {
        boolean isClient = player.world.isRemote;
        BiTuple<TriFunction<PlayerEntity, Inventory, MessagePacket, Screen>, Supplier<Inventory>> guiHandler = GuiHandlerRegistry.INSTANCE.get(Identifier.of(message.strings[0]));
        if (guiHandler != null)
            //noinspection deprecation
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).setScreen(guiHandler.one().apply(player, isClient ? guiHandler.two().get() : (Inventory) message.objects[0], message));
        if (isClient)
            player.container.syncId = message.ints[0];
    }
}
