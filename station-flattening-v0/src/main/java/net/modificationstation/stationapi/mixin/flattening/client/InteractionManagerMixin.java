package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(InteractionManager.class)
class InteractionManagerMixin {
    @Shadow @Final protected Minecraft minecraft;

    @ModifyConstant(
            method = "breakBlock",
            constant = @Constant(intValue = 256)
    )
    private int stationapi_changeMetaShift(int value) {
        return 268435456;
    }
}
