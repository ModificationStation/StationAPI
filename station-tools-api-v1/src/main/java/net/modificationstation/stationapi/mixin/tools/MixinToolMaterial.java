package net.modificationstation.stationapi.mixin.tools;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ToolMaterial.class)
public class MixinToolMaterial implements StationToolMaterial {

    // TODO : Clean the vanilla mess up
    // TODO : Decouple some tool parameters from materials

    @Unique
    private TagKey<BlockBase> stationapi_requiredBlockTag;
    
    @Override
    @Unique
    public TagKey<BlockBase> getRequiredBlockTag() {
        return stationapi_requiredBlockTag;
    }
}
