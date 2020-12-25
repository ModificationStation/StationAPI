package net.modificationstation.stationapi.impl.client.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.packet.Message;

public class GuiHelper extends net.modificationstation.stationapi.impl.common.gui.GuiHelper {

    @Override
    protected void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message) {
        message.put(new InventoryBase[]{inventory});
    }

    @Override
    protected void afterPacketSent(PlayerBase player, ContainerBase container) {

    }
}
