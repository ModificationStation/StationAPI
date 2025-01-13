package net.modificationstation.stationapi.impl.vanillafix.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.dimension.SkylandsDimension;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaDimensionFixImpl {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @FunctionalInterface
    interface DimensionRegister { void accept(final @NotNull Identifier id, final int serialID, final @NotNull Supplier<@NotNull Dimension> factory); }
    @EventListener
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegister r = (id, serialID, factory) -> event.registry.register(id, serialID, new DimensionContainer<>(factory));
        r.accept(THE_NETHER, -1, NetherDimension::new);
        r.accept(OVERWORLD, 0, OverworldDimension::new);
        r.accept(SKYLANDS, 1, SkylandsDimension::new);
    }
}
