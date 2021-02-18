package net.modificationstation.stationapi.impl.client.gui.screen;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.impl.common.gui.GuiHelperImpl;

public class GuiHelperClientImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message) {
        message.put(new Object[] {inventory});
    }

    @Override
    protected void afterPacketSent(PlayerBase player, ContainerBase container) {

    }
}
