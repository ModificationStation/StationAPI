package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.impl.level.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityBase.class)
public class MixinEntityBase {

    @Shadow public Level level;

    @ModifyConstant(
            method = "baseTick()V",
            constant = @Constant(doubleValue = -64)
    )
    private double modifyVoidDamage(double constant) {
        return constant + ((HeightLimitView) level).getBottomY();
    }
}
