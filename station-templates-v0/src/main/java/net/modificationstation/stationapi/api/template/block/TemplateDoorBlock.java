package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.DoorBlock;
import net.minecraft.block.Material;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateDoorBlock extends DoorBlock implements BlockTemplate {
    public TemplateDoorBlock(Identifier identifier, Material material) {
        this(BlockTemplate.getNextId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateDoorBlock(int i, Material arg) {
        super(i, arg);
    }
}
