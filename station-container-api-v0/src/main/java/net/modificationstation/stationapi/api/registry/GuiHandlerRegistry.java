package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
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

public final class GuiHandlerRegistry extends SimpleRegistry<BiTuple<TriFunction<PlayerBase, InventoryBase, Message, ScreenBase>, Supplier<InventoryBase>>> {

    private static final BiTuple<TriFunction<PlayerBase, InventoryBase, Message, ScreenBase>, Supplier<InventoryBase>> EMPTY = Tuple.tuple((playerBase, inventoryBase, message) -> null, () -> null);
    public static final RegistryKey<GuiHandlerRegistry> KEY = RegistryKey.ofRegistry(Identifier.of(StationAPI.MODID, "gui_handlers"));
    public static final GuiHandlerRegistry INSTANCE = Registries.create(KEY, new GuiHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private GuiHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }

    public void registerValueNoMessage(Identifier identifier, BiTuple<BiFunction<PlayerBase, InventoryBase, ScreenBase>, Supplier<InventoryBase>> value) {
        Registry.register(this, identifier, Tuple.tuple((playerBase, inventoryBase, message) -> value.one().apply(playerBase, inventoryBase), value.two()));
    }
}
