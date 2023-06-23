package net.modificationstation.stationapi.impl.vanillafix.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.dimension.Overworld;
import net.minecraft.level.dimension.Skylands;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.world.dimension.VanillaDimensions.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaDimensionFixImpl {

    @FunctionalInterface
    interface DimensionRegister { void accept(final @NotNull Identifier id, final int serialID, final @NotNull Supplier<@NotNull Dimension> factory); }
    @EventListener
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegister r = (id, serialID, factory) -> event.registry.register(id, serialID, new DimensionContainer<>(factory));
        r.accept(THE_NETHER, -1, Nether::new);
        r.accept(OVERWORLD, 0, Overworld::new);
        r.accept(SKYLANDS, 1, Skylands::new);
    }
}
