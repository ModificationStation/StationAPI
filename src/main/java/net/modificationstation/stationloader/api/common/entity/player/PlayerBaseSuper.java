package net.modificationstation.stationloader.api.common.entity.player;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityBase;

public interface PlayerBaseSuper {
    void superMoveFlying(float f, float f1, float f2);

    boolean superIsInsideOfMaterial(Material material);

    float superGetEntityBrightness(float f);

    String superGetHurtSound();

    void superFall(float f);

    void superJump();

    void superDamageEntity(int i);

    double superGetDistanceSqToEntity(EntityBase entity);

    boolean superHandleWaterMovement();

    boolean superHandleLavaMovement();

    void superUpdatePlayerActionState();
}
