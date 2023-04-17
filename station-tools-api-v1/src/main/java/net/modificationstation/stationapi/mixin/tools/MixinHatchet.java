package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hatchet.class)
public class MixinHatchet extends ToolBase {

    protected MixinHatchet(int i, int j, ToolMaterial arg, BlockBase[] args) {
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
