package net.modificationstation.stationapi.impl.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.AbstractToolMaterial;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AbstractToolMaterialImpl {
    public static final ThreadLocal<AbstractToolMaterial> ABSTRACT_TOOL_MATERIAL = new ThreadLocal<>();

    public static ToolMaterial pushAbstractToolMaterial(AbstractToolMaterial abstractToolMaterial) {
        ABSTRACT_TOOL_MATERIAL.set(abstractToolMaterial);
        return null;
    }

    public static AbstractToolMaterial popAbstractToolMaterial() {
        var abstractToolMaterial = ABSTRACT_TOOL_MATERIAL.get();
        ABSTRACT_TOOL_MATERIAL.remove();
        return abstractToolMaterial;
    }
}
