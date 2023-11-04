package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDoor extends DoorBlock implements BlockTemplate {

    public TemplateDoor(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDoor(int i, Material arg) {
        super(i, arg);
    }
}
