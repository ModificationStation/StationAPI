package net.modificationstation.stationloader.api.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.packet.CustomData;
import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.function.Consumer;

public interface GuiHelper extends HasHandler<GuiHelper> {

    GuiHelper INSTANCE = new GuiHelper() {

        private GuiHelper handler;

        @Override
        public void setHandler(GuiHelper handler) {
            this.handler = handler;
        }

        @Override
        public void openGUI(PlayerBase player, String modid, short guiID, InventoryBase inventory, ContainerBase container) {
            checkAccess(handler);
            handler.openGUI(player, modid, guiID, inventory, container);
        }

        @Override
        public void openGUI(PlayerBase player, String modid, short guiID, InventoryBase inventory, ContainerBase container, Consumer<CustomData> customData) {
            checkAccess(handler);
            handler.openGUI(player, modid, guiID, inventory, container, customData);
        }
    };

    default void openGUI(PlayerBase player, String modid, short guiID, InventoryBase inventory, ContainerBase container) {
        openGUI(player, modid, guiID, inventory, container, customData -> {});
    }

    void openGUI(PlayerBase player, String modid, short guiID, InventoryBase inventory, ContainerBase container, Consumer<CustomData> customData);
}
