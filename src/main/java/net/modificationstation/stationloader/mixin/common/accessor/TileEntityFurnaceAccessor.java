package net.modificationstation.stationloader.mixin.common.accessor;

import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TileEntityFurnace.class)
public interface TileEntityFurnaceAccessor {

    @Invoker
    int invokeGetFuelTime(ItemInstance itemInstance);
}
