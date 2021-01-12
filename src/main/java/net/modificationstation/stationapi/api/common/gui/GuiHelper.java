package net.modificationstation.stationapi.api.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.common.util.SideUtils;
import net.modificationstation.stationapi.impl.client.gui.GuiHelperClientImpl;
import net.modificationstation.stationapi.impl.common.gui.GuiHelperImpl;
import net.modificationstation.stationapi.impl.server.gui.GuiHelperServerImpl;

import java.util.function.Consumer;

public class GuiHelper {

    private static final GuiHelperImpl INSTANCE = SideUtils.get(GuiHelperClientImpl::new, GuiHelperServerImpl::new);

    @API
    public static void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container) {
        INSTANCE.openGUI(player, identifier, inventory, container);
    }

    @API
    public static void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData) {
        INSTANCE.openGUI(player, identifier, inventory, container, customData);
    }
}
