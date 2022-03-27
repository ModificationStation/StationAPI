package net.modificationstation.stationapi.api.registry;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.inventory.InventoryBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.packet.Message;
import uk.co.benjiweber.expressions.function.TriFunction;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class GuiHandlerRegistry extends Registry<BiTuple<TriFunction<PlayerBase, InventoryBase, Message, ScreenBase>, Supplier<InventoryBase>>> {

    public static final GuiHandlerRegistry INSTANCE = new GuiHandlerRegistry(Identifier.of(StationAPI.MODID, "gui_handlers"));

    private GuiHandlerRegistry(Identifier identifier) {
        super(identifier);
    }

    public void registerValueNoMessage(Identifier identifier, BiTuple<BiFunction<PlayerBase, InventoryBase, ScreenBase>, Supplier<InventoryBase>> value) {
        super.register(identifier, Tuple.tuple((playerBase, inventoryBase, message) -> value.one().apply(playerBase, inventoryBase), value.two()));
    }
}
