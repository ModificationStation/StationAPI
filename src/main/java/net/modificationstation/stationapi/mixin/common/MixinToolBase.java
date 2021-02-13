package net.modificationstation.stationapi.mixin.common;

import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.common.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolBase.class)
public class MixinToolBase implements ToolLevel {

    @Shadow
    protected ToolMaterial toolMaterial;

    @Override
    public int getToolLevel() {
        return toolMaterial.getMiningLevel();
    }

    @Override
    public ToolMaterial getMaterial() {
        return toolMaterial;
    }
}
