package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Door;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateDoor extends Door implements BlockTemplate {

    public TemplateDoor(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDoor(int i, Material arg) {
        super(i, arg);
    }
}
