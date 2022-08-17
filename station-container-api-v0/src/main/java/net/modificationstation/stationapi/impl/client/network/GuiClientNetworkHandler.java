package net.modificationstation.stationapi.impl.client.network;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.MessageListenerRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import uk.co.benjiweber.expressions.function.TriFunction;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class GuiClientNetworkHandler {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerMessageListeners(MessageListenerRegistryEvent event) {
        Registry.register(event.registry, MODID.id("open_gui"), GuiClientNetworkHandler::handleGui);
        StationAPI.EVENT_BUS.post(new GuiHandlerRegistryEvent());
    }

    private static void handleGui(PlayerBase player, Message message) {
        boolean isClient = player.level.isServerSide;
        BiTuple<TriFunction<PlayerBase, InventoryBase, Message, ScreenBase>, Supplier<InventoryBase>> guiHandler = GuiHandlerRegistry.INSTANCE.get(Identifier.of(message.strings[0]));
        if (guiHandler != null)
            //noinspection deprecation
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).openScreen(guiHandler.one().apply(player, isClient ? guiHandler.two().get() : (InventoryBase) message.objects[0], message));
        if (isClient)
            player.container.currentContainerId = message.ints[0];
    }
}
