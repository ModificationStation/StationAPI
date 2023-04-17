package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolItem;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolBase.class)
public class MixinToolBase implements StationToolItem {

    @Shadow protected ToolMaterial toolMaterial;
    @Unique
    private TagKey<BlockBase> stationapi_effectiveBlocks;

    @Override
    public void setEffectiveBlocks(TagKey<BlockBase> effectiveBlocks) {
        stationapi_effectiveBlocks = effectiveBlocks;
    }

    @Override
    public TagKey<BlockBase> getEffectiveBlocks(ItemInstance itemInstance) {
        return stationapi_effectiveBlocks;
    }

    @Override
    public ToolMaterial getMaterial(ItemInstance itemInstance) {
        return toolMaterial;
    }
}
