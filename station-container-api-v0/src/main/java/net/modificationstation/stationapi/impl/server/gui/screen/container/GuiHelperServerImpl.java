package net.modificationstation.stationapi.impl.server.gui.screen.container;

import net.minecraft.class_633;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.impl.gui.screen.container.GuiHelperImpl;
import net.modificationstation.stationapi.mixin.container.server.ServerPlayerEntityAccessor;

public class GuiHelperServerImpl extends GuiHelperImpl {

    @Override
    protected void sideDependentPacket(PlayerEntity player, Inventory inventory, MessagePacket message) {
        message.objects = new Object[] { null };
        ((ServerPlayerEntityAccessor) player).invokeMethod_314();
        message.ints = new int[] { ((ServerPlayerEntityAccessor) player).getField_260() };
    }

    @Override
    protected void afterPacketSent(PlayerEntity player, ScreenHandler container) {
        player.container = container;
        container.syncId = ((ServerPlayerEntityAccessor) player).getField_260();
        container.addListener((class_633) player);
    }
}
