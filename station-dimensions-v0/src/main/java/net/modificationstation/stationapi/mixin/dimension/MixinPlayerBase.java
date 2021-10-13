package net.modificationstation.stationapi.mixin.dimension;

import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.entity.HasTeleportationManager;
import net.modificationstation.stationapi.api.level.dimension.TeleportationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerBase.class)
public class MixinPlayerBase implements HasTeleportationManager {

    @Unique
    private TeleportationManager teleportationManager;

    @Unique
    @Override
    public void setTeleportationManager(TeleportationManager manager) {
        teleportationManager = manager;
    }

    @Unique
    @Override
    public TeleportationManager getTeleportationManager() {
        return teleportationManager;
    }
}
