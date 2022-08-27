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

    ReferenceSet<TagKey<BlockBase>> ALL_TOOL_MATERIAL_TAGS = new ReferenceOpenHashSet<>();

    default ToolMaterial inheritsFrom(ToolMaterial... toolMaterials) {
        return Util.assertImpl();
    }

    default ToolMaterial requiredBlockTag(Identifier tag) {
        return Util.assertImpl();
    }

    default ReferenceSet<ToolMaterial> getParentMaterials() {
        return Util.assertImpl();
    }

    default TagKey<BlockBase> getRequiredBlockTag() {
        return Util.assertImpl();
    }

    default boolean matches(BlockState state) {
        return ALL_TOOL_MATERIAL_TAGS.stream().noneMatch(state::isIn) || matches0(state);
    }

    private boolean matches0(BlockState state) {
        TagKey<BlockBase> tag = getRequiredBlockTag();
        if (tag != null) {
            if (state.isIn(tag)) return true;
            BiPredicate<ToolMaterial, BlockState> matches0 = StationToolMaterial::matches0;
            Predicate<ToolMaterial> matchesThis = t -> matches0.test(t, state);
            return getParentMaterials().stream().anyMatch(matchesThis);
        } else return false;
    }
}
