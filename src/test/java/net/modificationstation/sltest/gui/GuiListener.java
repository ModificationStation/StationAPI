package net.modificationstation.sltest.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class GuiListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerGuiHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(Identifier.of(SLTest.NAMESPACE, "freezer"), BiTuple.of(this::openFreezer, TileEntityFreezer::new));
    }

    @Environment(EnvType.CLIENT)
    public Screen openFreezer(PlayerEntity player, Inventory inventoryBase) {
        return new GuiFreezer(player.inventory, (TileEntityFreezer) inventoryBase);
    }
}
