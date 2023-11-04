package net.modificationstation.stationapi.impl.gui.screen.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.Consumer;

public abstract class GuiHelperImpl {

    public void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, Container container) {
        openGUI(player, identifier, inventory, container, (message) -> {});
    }

    public void openGUI(PlayerEntity player, Identifier identifier, Inventory inventory, Container container, Consumer<Message> customData) {
        Message message = new Message(Identifier.of(StationAPI.NAMESPACE, "open_gui"));
        message.strings = new String[] { identifier.toString() };
        sideDependentPacket(player, inventory, message);
        customData.accept(message);
        PacketHelper.sendTo(player, message);
        afterPacketSent(player, container);
    }

    protected abstract void sideDependentPacket(PlayerEntity player, Inventory inventory, Message message);

    protected abstract void afterPacketSent(PlayerEntity player, Container container);
}
