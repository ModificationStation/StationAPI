package net.modificationstation.stationapi.mixin.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(PlayerInventory.class)
class PlayerInventoryMixin {
    @Shadow
    public ItemStack[] main;

    @WrapOperation(
            method = "combineStacks",
            at = @At(
                    value = "NEW",
                    target = "(III)Lnet/minecraft/item/ItemStack;"
            )
    )
    private ItemStack stationapi_newItemStack(
            int id, int count, int damage, Operation<ItemStack> original,
            @Local(index = 1, argsOnly = true) ItemStack stack
    ) {
        final var newStack = original.call(id, count, damage);
        StationNBTSetter.cast(newStack).setStationNbt(stack.getStationNbt());
        return newStack;
    }

    @Redirect(
            method = "indexOf(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemStack;count:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private int stationapi_captureItemStack(ItemStack instance, ItemStack instance2) {
        if (Objects.equals(instance.getStationNbt(), instance2.getStationNbt()))
            return instance.count;
        else {
            notchGodDamnit = true;
            return Integer.MAX_VALUE;
        }
    }

    @Unique
    private boolean notchGodDamnit;

    @Redirect(
            method = "indexOf(Lnet/minecraft/item/ItemStack;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerInventory;getMaxCountPerStack()I"
            )
    )
    private int stationapi_fixStackableNBTs(PlayerInventory instance) {
        if (notchGodDamnit) {
            notchGodDamnit = false;
            return Integer.MIN_VALUE;
        } else
            return instance.getMaxCountPerStack();
    }
}
