package net.modificationstation.stationapi.api.item.tool;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface StationToolMaterial {

    default ToolMaterial requiredBlockTag(Identifier tag) {
        return Util.assertImpl();
    }

    default TagKey<BlockBase> getRequiredBlockTag() {
        return Util.assertImpl();
    }
}
