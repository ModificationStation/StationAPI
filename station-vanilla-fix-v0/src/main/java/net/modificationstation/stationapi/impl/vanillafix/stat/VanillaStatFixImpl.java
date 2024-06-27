package net.modificationstation.stationapi.impl.vanillafix.stat;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.StatRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.StatRegistry;

import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaStatFixImpl {
    @EventListener
    private static void registerStats(StatRegistryEvent event) {
        StatRegistry registry = event.registry;

        register(registry, "start_game", Stats.START_GAME);
        register(registry, "create_world", Stats.CREATE_WORLD);
        register(registry, "load_world", Stats.LOAD_WORLD);
        register(registry, "join_multiplayer", Stats.JOIN_MULTIPLAYER);
        register(registry, "leave_game", Stats.LEAVE_GAME);
        register(registry, "play_one_minute", Stats.PLAY_ONE_MINUTE);
        register(registry, "walk_one_cm", Stats.WALK_ONE_CM);
        register(registry, "swim_one_cm", Stats.SWIM_ONE_CM);
        register(registry, "fall_one_cm", Stats.FALL_ONE_CM);
        register(registry, "climb_one_cm", Stats.CLIMB_ONE_CM);
        register(registry, "fly_one_cm", Stats.FLY_ONE_CM);
        register(registry, "dive_one_cm", Stats.DIVE_ONE_CM);
        register(registry, "minecart_one_cm", Stats.MINECART_ONE_CM);
        register(registry, "boat_one_cm", Stats.BOAT_ONE_CM);
        register(registry, "pig_one_cm", Stats.PIG_ONE_CM);
        register(registry, "jump", Stats.JUMP);
        register(registry, "drop", Stats.DROP);
        register(registry, "damage_dealt", Stats.DAMAGE_DEALT);
        register(registry, "damage_taken", Stats.DAMAGE_TAKEN);
        register(registry, "deaths", Stats.DEATHS);
        register(registry, "mob_kills", Stats.MOB_KILLS);
        register(registry, "player_kills", Stats.PLAYER_KILLS);
        register(registry, "fish_caught", Stats.FISH_CAUGHT);
    }

    private static void register(Registry<Stat> registry, String id, Stat stat) {
        Registry.register(registry, stat.id, of(id), stat);
    }
}
