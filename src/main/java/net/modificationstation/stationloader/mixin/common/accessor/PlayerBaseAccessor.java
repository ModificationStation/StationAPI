package net.modificationstation.stationloader.mixin.common.accessor;

import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(PlayerBase.class)
public interface PlayerBaseAccessor {

    @Accessor
    boolean isSleeping();
}
