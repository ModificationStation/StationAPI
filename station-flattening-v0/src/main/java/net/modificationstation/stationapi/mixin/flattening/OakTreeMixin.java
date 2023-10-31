package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.level.Level;
import net.minecraft.level.structure.OakTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Random;

@Mixin(OakTree.class)
public class OakTreeMixin {
    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 1
            )
    )
    private int changeBottomYPlus1(int constant, Level level, Random random, int x, int y, int z) {
        return level.getBottomY() + 1;
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(intValue = 128)
    )
    private int changeMaxHeight(int constant, Level level, Random random, int x, int y, int z) {
        return level.getTopY();
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int changeBottomYLT(int constant, Level level, Random random, int x, int y, int z) {
        return level.getBottomY();
    }
}
