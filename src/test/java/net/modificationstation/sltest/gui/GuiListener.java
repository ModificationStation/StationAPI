package net.modificationstation.sltest.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.sltest.tileentity.TileEntityFreezer;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import uk.co.benjiweber.expressions.tuple.BiTuple;

public class GuiListener {

    @Environment(EnvType.CLIENT)
    @EventListener
    public void registerGuiHandlers(GuiHandlerRegistryEvent event) {
        GuiHandlerRegistry registry = event.registry;
        registry.registerValueNoMessage(Identifier.of(SLTest.MODID, "freezer"), BiTuple.of(this::openFreezer, TileEntityFreezer::new));
    }

    @Environment(EnvType.CLIENT)
    public ScreenBase openFreezer(PlayerBase player, InventoryBase inventoryBase) {
        return new GuiFreezer(player.inventory, (TileEntityFreezer) inventoryBase);
    }
}
