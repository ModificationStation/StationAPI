package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FurnaceBlockEntity.class)
public interface TileEntityFurnaceAccessor {

    @Invoker
    int invokeGetFuelTime(ItemStack itemInstance);
}
