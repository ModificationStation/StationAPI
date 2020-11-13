package net.modificationstation.stationloader.mixin.common.accessor;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolBase.class)
public interface ToolBaseAccessor {

    @Accessor
    void setEffectiveBlocksBase(BlockBase[] blockBases);

    @Accessor
    ToolMaterial getToolMaterial();
}
