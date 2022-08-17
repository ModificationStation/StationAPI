package net.modificationstation.stationapi.mixin.tools;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.template.item.tool.ToolMaterialTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;

@Mixin(ToolMaterial.class)
public class MixinToolMaterial<T extends ToolMaterial & ToolMaterialTemplate<T>> implements ToolMaterialTemplate<T> {

    @Unique
    private TagKey<BlockBase> stationapi_requiredBlockTag;
    @Unique
    private ReferenceSet<T> stationapi_parentMaterials;
    @Unique
    private ReferenceSet<T> stationapi_parentMaterialsView;

    @Inject(
            method = "<init>(Ljava/lang/String;IIIFI)V",
            at = @At("RETURN")
    )
    private void init(String i, int j, int k, int f, float l, int par6, CallbackInfo ci) {
        stationapi_parentMaterials = new ReferenceOpenHashSet<>();
        stationapi_parentMaterialsView = ReferenceSets.unmodifiable(stationapi_parentMaterials);
    }

    @Override
    @Unique
    public T inheritsFrom(ToolMaterial... toolMaterials) {
        //noinspection unchecked,RedundantClassCall
        Collections.addAll(stationapi_parentMaterials, (T[]) Object[].class.cast(toolMaterials));
        //noinspection unchecked,ConstantConditions
        return (T) (Object) this;
    }

    @Override
    @Unique
    public T requiredBlockTag(Identifier tag) {
        ALL_TOOL_MATERIAL_TAGS.remove(stationapi_requiredBlockTag);
        ALL_TOOL_MATERIAL_TAGS.add(stationapi_requiredBlockTag = TagKey.of(BlockRegistry.INSTANCE.getKey(), tag));
        //noinspection unchecked,ConstantConditions
        return (T) (Object) this;
    }

    @Override
    @Unique
    public ReferenceSet<T> getParentMaterials() {
        return stationapi_parentMaterialsView;
    }

    @Override
    @Unique
    public TagKey<BlockBase> getRequiredBlockTag() {
        return stationapi_requiredBlockTag;
    }
}
