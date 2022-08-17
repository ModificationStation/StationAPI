package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.tool.Shovel;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Shovel.class)
public abstract class MixinShovel implements ToolLevel {

    @Inject(
            method = "<init>(ILnet/minecraft/item/tool/ToolMaterial;)V",
            at = @At("RETURN")
    )
    private void injectAtCtor(int arg, ToolMaterial par2, CallbackInfo ci) {
        setEffectiveBlocks(TagKey.of(BlockRegistry.INSTANCE.getKey(), Identifier.of("mineable/shovel")));
    }
}
