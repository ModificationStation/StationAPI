package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public class MixinEntityBase {

    @Shadow public World level;

    @ModifyConstant(
            method = "baseTick()V",
            constant = @Constant(doubleValue = -64)
    )
    private double modifyVoidDamage(double constant) {
        return constant + level.getBottomY();
    }
}
