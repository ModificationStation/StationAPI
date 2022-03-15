package net.modificationstation.stationapi.mixin.blockitem;

import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {

    @Shadow private int blockId;

    @Inject(
            method = "<init>(I)V",
            at = @At("RETURN")
    )
    private void register(int par1, CallbackInfo ci) {
        ItemRegistry.INSTANCE.register(BlockRegistry.INSTANCE.getIdentifier(blockId).orElseThrow(), (Block) (Object) this);
    }
}
