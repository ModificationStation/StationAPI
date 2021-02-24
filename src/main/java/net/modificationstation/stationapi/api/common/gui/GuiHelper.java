package net.modificationstation.stationapi.api.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.impl.client.gui.screen.GuiHelperClientImpl;
import net.modificationstation.stationapi.impl.common.gui.GuiHelperImpl;
import net.modificationstation.stationapi.impl.server.gui.GuiHelperServerImpl;

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
    private static final GuiHelperImpl INSTANCE = SideUtils.get(() -> new GuiHelperClientImpl(), () -> new GuiHelperServerImpl());

    /**
     * Handles side dependent container GUI.
     * If executed on client, opens GUI, if executed on server, sends a message to client with all data to open it.
     * Doesn't require any side-checks. If the current game is multiplayer but the code runs on client, it'll be ignored.
     * @param player the player to open GUI for.
     * @param identifier GUI handler identifier.
     * @param inventory the inventory to open (usually a tile entity).
     * @param container the container to open.
     * @see RegistryEvent.GuiHandlers
     * @see GuiHandlerRegistry
     * @see GuiHelper#openGUI(PlayerBase, Identifier, InventoryBase, ContainerBase, Consumer)
     */
    @API
    public static void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container) {
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
     * @see RegistryEvent.GuiHandlers
     * @see GuiHandlerRegistry
     * @see GuiHelper#openGUI(PlayerBase, Identifier, InventoryBase, ContainerBase)
     */
    @API
    public static void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData) {
        INSTANCE.openGUI(player, identifier, inventory, container, customData);
    }
}
