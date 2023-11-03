package net.modificationstation.stationapi.mixin.flattening;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Random;
import net.minecraft.class_543;
import net.minecraft.world.World;

@Mixin(class_543.class)
public class OakTreeMixin {
    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 1
            )
    )
    private int changeBottomYPlus1(int constant, World level, Random random, int x, int y, int z) {
        return level.getBottomY() + 1;
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(intValue = 128)
    )
    private int changeMaxHeight(int constant, World level, Random random, int x, int y, int z) {
        return level.getTopY();
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int changeBottomYLT(int constant, World level, Random random, int x, int y, int z) {
        return level.getBottomY();
    }
}
