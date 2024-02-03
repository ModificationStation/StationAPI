package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldRenderer.class)
class WorldRendererMixin {
    @Shadow private World field_1805;

    @ModifyConstant(
            method = "method_1544",
            constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO)
    )
    private int stationapi_changeMinHeight(int value) {
        return field_1805.getBottomY();
    }

    @ModifyConstant(
            method = "method_1544",
            constant = @Constant(
                    intValue = 0,
                    ordinal = 5
            )
    )
    private int stationapi_changeMinBlockHeight(int value) {
        return field_1805.getBottomY();
    }

    @ModifyConstant(
            method = "method_1544",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int value) {
        return field_1805.getTopY();
    }

    @ModifyConstant(
            method = "method_1544",
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeMaxBlockHeight(int value) {
        return field_1805.getTopY() - 1;
    }

    @ModifyConstant(
            method = "method_1537",
            constant = @Constant(intValue = 8)
    )
    private int stationapi_changeSectionCount(int value) {
        return field_1805 == null ? value : field_1805.countVerticalSections();
    }

    @ModifyConstant(
            method = "method_1152",
            constant = @Constant(
                    intValue = 0xFF,
                    ordinal = 0
            )
    )
    private int stationapi_changeBlockIDBitmask1(int value) {
        if (0xFF < Block.BLOCKS.length) {
            return 0x0FFFFFFF;
        } else {
            return 0xFF;
        }
    }

    @ModifyConstant(
            method = "method_1152",
            constant = @Constant(
                    intValue = 0xFF,
                    ordinal = 1
            )
    )
    private int stationapi_changeBlockIDBitmask2(int value) {
        if (0xFF < Block.BLOCKS.length) {
            return 0x0FFFFFFF;
        } else {
            return 0xFF;
        }
    }

    @ModifyConstant(
            method = "method_1152",
            constant = @Constant(
                    intValue = 0xFF,
                    ordinal = 2
            )
    )
    private int stationapi_changeMetaBitmask(int value) {
        if (0xFF < Block.BLOCKS.length) {
            return 15;
        } else {
            return 0xFF;
        }
    }

    @ModifyConstant(
            method = "method_1152",
            constant = @Constant(intValue = 8)
    )
    private int stationapi_changeMetaBitshift(int value) {
        if (0xFF < Block.BLOCKS.length) {
            return 28;
        } else {
            return 8;
        }
    }

    @ModifyArg(
            method = "method_1537",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/chunk/ChunkBuilder;<init>(Lnet/minecraft/world/World;Ljava/util/List;IIIII)V"
            ),
            index = 3
    )
    private int stationapi_offsetYBlockCoord1(int original) {
        return field_1805 == null ? original : field_1805.getBottomY() + original;
    }

    @ModifyArg(
            method = "method_1553(III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/chunk/ChunkBuilder;setPosition(III)V"
            ),
            index = 1
    )
    private int stationapi_offsetYBlockCoord2(int y) {
        return field_1805.getBottomY() + y;
    }

    @ModifyVariable(
            method = "method_1543(IIIIII)V",
            at = @At("HEAD"),
            index = 2,
            argsOnly = true
    )
    private int stationapi_modWhateverTheFuckThisIs1(int value) {
        return value - field_1805.getBottomY();
    }

    @ModifyVariable(
            method = "method_1543(IIIIII)V",
            at = @At("HEAD"),
            index = 5,
            argsOnly = true
    )
    private int stationapi_modWhateverTheFuckThisIs2(int value) {
        return value - field_1805.getBottomY();
    }
}
