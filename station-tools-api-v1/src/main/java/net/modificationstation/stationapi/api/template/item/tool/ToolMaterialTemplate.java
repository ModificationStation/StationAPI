package net.modificationstation.stationapi.api.template.item.tool;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface ToolMaterialTemplate<T extends ToolMaterial & ToolMaterialTemplate<T>> {

    ReferenceSet<TagKey<BlockBase>> ALL_TOOL_MATERIAL_TAGS = new ReferenceOpenHashSet<>();

    T inheritsFrom(ToolMaterial... toolMaterials);

    T requiredBlockTag(Identifier tag);

    ReferenceSet<T> getParentMaterials();

    TagKey<BlockBase> getRequiredBlockTag();

    default boolean matches(BlockState state) {
        return ALL_TOOL_MATERIAL_TAGS.stream().noneMatch(state::isIn) || matches0(state);
    }

    private boolean matches0(BlockState state) {
        TagKey<BlockBase> tag = getRequiredBlockTag();
        if (tag != null) {
            if (state.isIn(tag)) return true;
            BiPredicate<T, BlockState> matches0 = ToolMaterialTemplate::matches0;
            Predicate<T> matchesThis = t -> matches0.test(t, state);
            return getParentMaterials().stream().anyMatch(matchesThis);
        } else return false;
    }

    static <T extends ToolMaterial & ToolMaterialTemplate<T>> T cast(ToolMaterial toolMaterial) {
        //noinspection unchecked,RedundantClassCall
        return (T) Object.class.cast(toolMaterial);
    }
}
