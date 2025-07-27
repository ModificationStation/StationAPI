package net.modificationstation.stationapi.mixin.item.dispenser.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.dispenser.ItemDispenseContext;
import net.modificationstation.stationapi.impl.dispenser.DispenserInfoStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(DispenserBlock.class)
class DispenserBlockMixin {
    @Inject(
            method = "dispense",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;itemId:I",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void stationapi_dispatchDispenseEvent(
            World world, int x, int y, int z, Random random, CallbackInfo ci,
            @Local(index = 11) DispenserBlockEntity dispenserBlockEntity, @Local(index = 12) LocalRef<ItemStack> dispensedStackRef,
            @Share("skipSpecial") LocalBooleanRef skipSpecialRef
    ) {
        int slot = DispenserInfoStorage.slot;
        ItemDispenseContext context = new ItemDispenseContext(dispensedStackRef.get(), dispenserBlockEntity, slot);

        if (StationAPI.EVENT_BUS.post(
                DispenseEvent.builder().context(context).build()
        ).isCanceled()) ci.cancel();

        dispensedStackRef.set(context.itemStack);
        skipSpecialRef.set(context.skipSpecial);
    }

    @WrapOperation(
            method = "dispense",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Item;id:I"
            )
    )
    private int stationapi_skipSpecial(
            Item instance, Operation<Integer> original,
            @Local(index = 12) ItemStack dispensedStackRef,
            @Share("skipSpecial") LocalBooleanRef skipSpecialRef
    ) {
        return skipSpecialRef.get() ? dispensedStackRef.itemId + 1 : original.call(instance);
    }
}
