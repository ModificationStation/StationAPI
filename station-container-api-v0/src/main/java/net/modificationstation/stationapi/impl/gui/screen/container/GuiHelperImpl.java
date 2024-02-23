package net.modificationstation.stationapi.impl.gui.screen.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.Consumer;

public abstract class GuiHelperImpl {

    public void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, ScreenHandler container) {
        openGUI(player, identifier, inventory, container, (message) -> {});
    }

    public void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, ScreenHandler container, Consumer<MessagePacket> customData) {
        MessagePacket message = new MessagePacket(Identifier.of(StationAPI.NAMESPACE, "open_gui"));
        message.strings = new String[] { identifier.toString() };
        sideDependentPacket(player, inventory, message);
        customData.accept(message);
        PacketHelper.sendTo(player, message);
        afterPacketSent(player, container);
    }

    protected abstract void sideDependentPacket(PlayerEntity player, Inventory inventory, MessagePacket message);

    protected abstract void afterPacketSent(PlayerEntity player, ScreenHandler container);
}
