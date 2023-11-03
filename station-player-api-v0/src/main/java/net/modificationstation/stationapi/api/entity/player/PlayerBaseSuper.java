package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.block.Material;
import net.minecraft.entity.Entity;

public interface PlayerBaseSuper {
    void superMoveFlying(float f, float f1, float f2);

    boolean superIsInsideOfMaterial(Material material);

    float superGetEntityBrightness(float f);

    String superGetHurtSound();

    void superFall(float f);

    void superJump();

    void superDamageEntity(int i);

    double superGetDistanceSqToEntity(Entity entity);

    boolean superHandleWaterMovement();

    boolean superHandleLavaMovement();

    void superUpdatePlayerActionState();
}
