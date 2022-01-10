package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ToolBase.class)
public class MixinToolBase extends ItemBase implements ToolLevel {

    @Shadow protected ToolMaterial toolMaterial;

    protected MixinToolBase(int id) {
        super(id);
    }

    @Override
    public ToolMaterial getMaterial(ItemInstance itemInstance) {
        return toolMaterial;
    }
}
