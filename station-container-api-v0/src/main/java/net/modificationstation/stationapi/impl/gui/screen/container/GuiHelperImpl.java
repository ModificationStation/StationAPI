package net.modificationstation.stationapi.impl.gui.screen.container;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.packet.Message;
import net.modificationstation.stationapi.api.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.function.*;

public abstract class GuiHelperImpl {

    public void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container) {
        openGUI(player, identifier, inventory, container, (message) -> {});
    }

    public void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData) {
        Message message = new Message(Identifier.of(StationAPI.MODID, "open_gui"));
        message.put(new String[]{identifier.toString()});
        sideDependentPacket(player, inventory, message);
        customData.accept(message);
        PacketHelper.sendTo(player, message);
        afterPacketSent(player, container);
    }

    protected abstract void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message);

    protected abstract void afterPacketSent(PlayerBase player, ContainerBase container);
}
