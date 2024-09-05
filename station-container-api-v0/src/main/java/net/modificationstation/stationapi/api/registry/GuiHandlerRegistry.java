package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.network.packet.MessagePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import uk.co.benjiweber.expressions.function.TriFunction;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.Tuple;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class GuiHandlerRegistry extends SimpleRegistry<BiTuple<TriFunction<PlayerEntity, Inventory, MessagePacket, Screen>, Supplier<Inventory>>> {
    private static final BiTuple<TriFunction<PlayerEntity, Inventory, MessagePacket, Screen>, Supplier<Inventory>> EMPTY = Tuple.tuple((playerBase, inventoryBase, message) -> null, () -> null);
    public static final RegistryKey<GuiHandlerRegistry> KEY = RegistryKey.ofRegistry(Identifier.of(StationAPI.NAMESPACE, "gui_handlers"));
    public static final GuiHandlerRegistry INSTANCE = Registries.create(KEY, new GuiHandlerRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private GuiHandlerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }

    public void registerValueNoMessage(Identifier identifier, BiTuple<BiFunction<PlayerEntity, Inventory, Screen>, Supplier<Inventory>> value) {
        Registry.register(this, identifier, Tuple.tuple((playerBase, inventoryBase, message) -> value.one().apply(playerBase, inventoryBase), value.two()));
    }
}
