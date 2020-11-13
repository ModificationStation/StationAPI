package net.modificationstation.stationloader.impl.common.gui;

import net.minecraft.container.ContainerBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
import net.modificationstation.stationloader.api.common.packet.CustomData;
import net.modificationstation.stationloader.api.common.packet.PacketHelper;

import java.util.function.Consumer;

public abstract class GuiHelper implements net.modificationstation.stationloader.api.common.gui.GuiHelper {

    @Override
    public void openGUI(PlayerBase player, String modid, short guiID, InventoryBase inventory, ContainerBase container, Consumer<CustomData> customData) {
        CustomData packet = GeneralFactory.INSTANCE.newInst(CustomData.class, StationLoader.INSTANCE.getData().getId() + ":open_gui");
        packet.set(new String[] {
                modid
        });
        packet.set(new byte[] {
                (byte) guiID
        });
        sideDependentPacket(player, inventory, packet);
        customData.accept(packet);
        PacketHelper.INSTANCE.sendTo(player, packet.getPacketInstance());
        afterPacketSent(player, container);

    }

    protected abstract void sideDependentPacket(PlayerBase player, InventoryBase inventory, CustomData packet);

    protected abstract void afterPacketSent(PlayerBase player, ContainerBase container);
}
