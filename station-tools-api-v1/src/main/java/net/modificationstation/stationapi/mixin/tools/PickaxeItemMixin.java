package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PickaxeItem.class)
class PickaxeItemMixin extends ToolItem {
    private PickaxeItemMixin(int i, int j, ToolMaterial arg, Block[] args) {
        super(i, j, arg, args);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void stationapi_injectAtCtor(int arg, ToolMaterial par2, CallbackInfo ci) {
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/pickaxe")));
    }
}
