package net.modificationstation.stationapi.impl.achievement;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievements;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class AchievementRegisterEventImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void dispatchAchievementRegisterEvent(AfterBlockAndItemRegisterEvent event) {
        //noinspection unchecked
        StationAPI.EVENT_BUS.post(AchievementRegisterEvent.builder().achievements(Achievements.ACHIEVEMENTS).build());
    }
}
