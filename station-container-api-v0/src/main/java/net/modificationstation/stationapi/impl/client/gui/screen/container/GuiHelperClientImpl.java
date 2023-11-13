package net.modificationstation.stationapi.impl.client.gui.screen.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;

public class GuiHelperClientImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerEntity player, Inventory inventory, MessagePacket message) {
        message.objects = new Object[] { inventory };
    }

    @Override
    protected void afterPacketSent(PlayerEntity player, Container container) {

    }
}
