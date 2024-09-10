package net.modificationstation.stationapi.api.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;

public record GuiHandler(ScreenFactory screenFactory, InventoryFactory inventoryFactory) {
    @FunctionalInterface
    public interface ScreenFactory {
        Screen create(PlayerEntity player, Inventory inventory, MessagePacket packet);
    }

    @FunctionalInterface
    public interface ScreenFactoryNoMessage extends ScreenFactory {
        @Override
        default Screen create(PlayerEntity player, Inventory inventory, MessagePacket packet) {
            return create(player, inventory);
        }

        Screen create(PlayerEntity player, Inventory inventory);
    }

    @FunctionalInterface
    public interface InventoryFactory {
        Inventory create();
    }
}
