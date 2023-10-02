package net.modificationstation.stationapi.mixin.tools;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.StationToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

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
