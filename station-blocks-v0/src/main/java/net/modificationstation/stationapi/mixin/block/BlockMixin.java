package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.block.StationBlock;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
abstract class BlockMixin implements StationBlock {
    @Shadow public abstract Block setTranslationKey(String string);

    @Override
    public Block setTranslationKey(Namespace namespace, String translationKey) {
        return setTranslationKey(namespace + "." + translationKey);
    }

    @WrapOperation(
            method = "getDroppedItemId",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;id:I"
            )
    )
    private int stationapi_returnCorrectItem(Block instance, Operation<Integer> original) {
        return instance.asItem().id;
    }
}
