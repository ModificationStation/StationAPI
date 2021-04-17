package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sword.class)
public class MixinSword implements net.modificationstation.stationapi.api.item.tool.Sword {

    private ToolMaterial toolMaterial;

    @Inject(method = "<init>(ILnet/minecraft/item/tool/ToolMaterial;)V", at = @At("RETURN"))
    private void captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        toolMaterial = arg;
    }

    @Override
    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }

    @Override
    public ToolMaterial getMaterial() {
        return toolMaterial;
    }
}
