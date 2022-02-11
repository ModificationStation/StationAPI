package net.modificationstation.stationapi.mixin.item;

import net.minecraft.stat.Stats;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Stats.class)
public class MixinStats {

    @ModifyConstant(
            method = "setupCrafting()V",
            constant = @Constant(intValue = 32000)
    )
    private static int getItemsSize(int constant) {
        return ItemRegistry.INSTANCE.getSize();
    }
}
