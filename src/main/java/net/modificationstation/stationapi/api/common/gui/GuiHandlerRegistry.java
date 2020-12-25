package net.modificationstation.stationapi.api.common.gui;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.packet.Message;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;
import uk.co.benjiweber.expressions.functions.TriFunction;
import uk.co.benjiweber.expressions.tuples.BiTuple;
import uk.co.benjiweber.expressions.tuples.Tuple;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class GuiHandlerRegistry extends Registry<BiTuple<TriFunction<PlayerBase, InventoryBase, Message, ScreenBase>, Supplier<InventoryBase>>> {

    public static final GuiHandlerRegistry INSTANCE = new GuiHandlerRegistry(Identifier.of(StationAPI.INSTANCE.getModID(), "gui_handlers"));

    private GuiHandlerRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public void registerValueNoMessage(Identifier identifier, BiTuple<BiFunction<PlayerBase, InventoryBase, ScreenBase>, Supplier<InventoryBase>> value) {
        super.registerValue(identifier, Tuple.tuple((playerBase, inventoryBase, message) -> value.one().apply(playerBase, inventoryBase), value.two()));
    }
}
