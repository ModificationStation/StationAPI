package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolMaterial.class)
class ToolMaterialMixin implements StationToolMaterial {
    @Unique
    private TagKey<Block> stationapi_miningLevelTag;

    @Override
    @Unique
    public ToolMaterial miningLevelTag(TagKey<Block> tag) {
        stationapi_miningLevelTag = tag;
        return ToolMaterial.class.cast(this);
    }

    @Override
    @Unique
    public TagKey<Block> getMiningLevelTag() {
        return stationapi_miningLevelTag;
    }
}
