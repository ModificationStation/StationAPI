package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Trapdoor;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateTrapdoor extends Trapdoor implements BlockTemplate {
    
    public TemplateTrapdoor(Identifier identifier, Material arg) {
        this(BlockTemplate.getNextId(), arg);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateTrapdoor(int i, Material arg) {
        super(i, arg);
    }
}
