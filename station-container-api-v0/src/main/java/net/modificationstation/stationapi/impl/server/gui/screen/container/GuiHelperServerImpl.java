package net.modificationstation.stationapi.impl.server.gui.screen.container;

import net.minecraft.class_633;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;
import net.modificationstation.stationapi.mixin.container.server.ServerPlayerAccessor;

public class GuiHelperServerImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerEntity player, Inventory inventory, Message message) {
        message.objects = new Object[] { null };
        ((ServerPlayerAccessor) player).invokeMethod_314();
        message.ints = new int[] { ((ServerPlayerAccessor) player).getField_260() };
    }

    @Override
    protected void afterPacketSent(PlayerEntity player, Container container) {
        player.container = container;
        container.syncId = ((ServerPlayerAccessor) player).getField_260();
        container.method_2076((class_633) player);
    }
}
