package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import net.modificationstation.stationapi.api.world.dimension.TeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
class PlayerEntityMixin implements HasTeleportationManager {
    @Unique
    private TeleportationManager stationapi_teleportationManager;

    @Unique
    @Override
    public void setTeleportationManager(TeleportationManager manager) {
        stationapi_teleportationManager = manager;
    }

    @Unique
    @Override
    public TeleportationManager getTeleportationManager() {
        return stationapi_teleportationManager;
    }
}
