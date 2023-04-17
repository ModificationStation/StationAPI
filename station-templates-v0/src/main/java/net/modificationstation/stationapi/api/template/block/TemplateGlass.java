package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Glass;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateGlass extends Glass implements BlockTemplate {

    public TemplateGlass(Identifier identifier, int j, Material arg, boolean flag) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j, arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateGlass(int i, int j, Material arg, boolean flag) {
        super(i, j, arg, flag);
    }
}
