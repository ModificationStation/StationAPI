package net.modificationstation.stationapi.impl.vanillafix.achievement;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.StatRegistry;

import java.lang.invoke.MethodHandles;

import static net.minecraft.achievement.Achievements.*;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaAchievementFixImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerStats(AchievementRegisterEvent event) {
        register("open_inventory", OPEN_INVENTORY);
        register("mine_wood", MINE_WOOD);
        register("craft_workbench", CRAFT_WORKBENCH);
        register("craft_pickaxe", CRAFT_PICKAXE);
        register("craft_furnace", CRAFT_FURNACE);
        register("acquire_iron", ACQUIRE_IRON);
        register("craft_hoe", CRAFT_HOE);
        register("craft_bread", CRAFT_BREAD);
        register("craft_cake", CRAFT_CAKE);
        register("craft_stone_pickaxe", CRAFT_STONE_PICKAXE);
        register("cook_fish", COOK_FISH);
        register("craft_rail", CRAFT_RAIL);
        register("craft_sword", CRAFT_SWORD);
        register("kill_enemy", KILL_ENEMY);
        register("kill_cow", KILL_COW);
        register("fly_pig", FLY_PIG);
    }

    private static void register(String id, Achievement achievement) {
        Registry.register(StatRegistry.INSTANCE, achievement.id, of(id), achievement);
    }
}