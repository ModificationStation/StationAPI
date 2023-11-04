package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AxeItem.class)
public class MixinHatchet extends ToolItem {

    protected MixinHatchet(int i, int j, ToolMaterial arg, Block[] args) {
        super(i, j, arg, args);
    }

    @Inject(
            method = "<init>(ILnet/minecraft/item/tool/ToolMaterial;)V",
            at = @At("RETURN")
    )
    private void injectAtCtor(int arg, ToolMaterial par2, CallbackInfo ci) {
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/axe")));
    }
}
