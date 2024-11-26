package net.modificationstation.stationapi.api.gui.screen.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.SideUtil;
import net.modificationstation.stationapi.impl.client.gui.screen.container.GuiHelperClientImpl;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;
import net.modificationstation.stationapi.impl.server.gui.screen.container.GuiHelperServerImpl;

import java.util.function.Consumer;

/**
 * Sided container GUI helper class.
 * @author mine_diver
 */
public final class GuiHelper {

    /**
     * Implementation instance.
     */
    @SuppressWarnings("Convert2MethodRef") // Method references load their target classes on both sides, causing crashes.
    private static final GuiHelperImpl INSTANCE = SideUtil.get(() -> new GuiHelperClientImpl(), () -> new GuiHelperServerImpl());

    /**
     * Handles side dependent container GUI.
     * If executed on client, opens GUI, if executed on server, sends a message to client with all data to open it.
     * Doesn't require any side-checks. If the current game is multiplayer but the code runs on client, it'll be ignored.
     * @param player the player to open GUI for.
     * @param identifier GUI handler identifier.
     * @param inventory the inventory to open (usually a tile entity).
     * @param container the container to open.
     * @see GuiHandlerRegistryEvent
     * @see GuiHandlerRegistry
     * @see GuiHelper#openGUI(PlayerEntity, Identifier, Inventory, ScreenHandler, Consumer)
     */
    @API
    public static void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, ScreenHandler container) {
        INSTANCE.openGUI(player, identifier, inventory, container);
    }

    /**
     * Handles side dependent container GUI with additional data via customData consumer.
     * If executed on client, opens GUI, if executed on server, sends a message to client with all data to open it.
     * Doesn't require any side-checks. If the current game is multiplayer but the code runs on client, it'll be ignored.
     * @param player the player to open GUI for.
     * @param identifier GUI handler identifier.
     * @param inventory the inventory to open (usually a tile entity).
     * @param container the container to open.
     * @param customData the packet consumer that can add additional data to the GUI packet.
     * @see GuiHandlerRegistryEvent
     * @see GuiHandlerRegistry
     * @see GuiHelper#openGUI(PlayerEntity, Identifier, Inventory, ScreenHandler)
     */
    @API
    public static void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, ScreenHandler container, Consumer<MessagePacket> customData) {
        INSTANCE.openGUI(player, identifier, inventory, container, customData);
    }
}
