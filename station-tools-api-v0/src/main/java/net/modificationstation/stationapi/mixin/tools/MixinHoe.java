package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hoe.class)
public class MixinHoe implements net.modificationstation.stationapi.api.item.tool.Hoe {

    private ToolMaterial toolMaterial;

    @Inject(method = "<init>(ILnet/minecraft/item/tool/ToolMaterial;)V", at = @At("RETURN"))
    private void captureToolMaterial(int i, ToolMaterial arg, CallbackInfo ci) {
        toolMaterial = arg;
    }

    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }

    public ToolMaterial getMaterial() {
        return toolMaterial;
    }

    @Override
    public ToolMaterial getMaterial(ItemInstance itemInstance) {
        return toolMaterial;
    }
}
