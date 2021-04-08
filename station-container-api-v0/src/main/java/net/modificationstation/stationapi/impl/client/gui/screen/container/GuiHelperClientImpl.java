package net.modificationstation.stationapi.impl.client.gui.screen.container;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;

public class GuiHelperClientImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message) {
        message.put(new Object[] {inventory});
    }

    @Override
    protected void afterPacketSent(PlayerBase player, ContainerBase container) {

    }
}
