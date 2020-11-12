package net.modificationstation.stationloader.impl.client.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.packet.CustomData;

public class GuiHelper extends net.modificationstation.stationloader.impl.common.gui.GuiHelper {

    @Override
    protected void sideDependentPacket(PlayerBase player, InventoryBase inventory, CustomData packet) {
        packet.set(new Object[] {
                inventory
        });
    }

    @Override
    protected void afterPacketSent(PlayerBase player, ContainerBase container) {

    }
}
