package net.modificationstation.stationloader.mixin.common.accessor;

import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(PlayerBase.class)
public interface PlayerBaseAccessor {

    @Accessor
    boolean isSleeping();

    void doFall(float fallDist);

    float getFallDistance();

    void setFallDistance(float f);

    boolean getSleeping();

    boolean getJumping();

    void doJump();

    Random getRandom();

    void setYSize(float f);

    void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping);

    void setMoveForward(float value);

    void setMoveStrafing(float value);

    void setIsJumping(boolean value);
}
