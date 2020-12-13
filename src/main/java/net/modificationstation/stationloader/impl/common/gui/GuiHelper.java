package net.modificationstation.stationloader.impl.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.packet.Message;
import net.modificationstation.stationloader.api.common.packet.PacketHelper;
import net.modificationstation.stationloader.api.common.registry.Identifier;

import java.util.function.Consumer;

public abstract class GuiHelper implements net.modificationstation.stationloader.api.common.gui.GuiHelper {

    @Override
    public void openGUI(PlayerBase player, Identifier identifier, InventoryBase inventory, ContainerBase container, Consumer<Message> customData) {
        Message message = new Message(Identifier.of(StationLoader.INSTANCE.getModID(), "open_gui"));
        message.put(identifier.toString());
        sideDependentPacket(player, inventory, message);
        customData.accept(message);
        PacketHelper.INSTANCE.sendTo(player, message);
        afterPacketSent(player, container);
    }

    protected abstract void sideDependentPacket(PlayerBase player, InventoryBase inventory, Message message);

    protected abstract void afterPacketSent(PlayerBase player, ContainerBase container);
}
