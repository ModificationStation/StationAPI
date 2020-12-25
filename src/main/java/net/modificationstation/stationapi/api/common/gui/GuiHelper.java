package net.modificationstation.stationapi.api.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.util.HasHandler;

import java.util.function.Consumer;

public interface GuiHelper extends HasHandler<GuiHelper> {

    GuiHelper INSTANCE = new GuiHelper() {

        private GuiHelper handler;

        @Override
        public void setHandler(GuiHelper handler) {
            this.handler = handler;
        }

        @Override
        public void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container) {
            checkAccess(handler);
            handler.openGUI(player, identifier, inventory, container);
        }

        @Override
        public void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData) {
            checkAccess(handler);
            handler.openGUI(player, identifier, inventory, container, customData);
        }
    };

    default void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container) {
        openGUI(player, identifier, inventory, container, customData -> {
        });
    }

    void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData);
}
