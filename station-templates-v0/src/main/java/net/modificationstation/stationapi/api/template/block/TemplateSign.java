package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Sign;
import net.minecraft.tileentity.TileEntityBase;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateSign extends Sign implements BlockTemplate {
    
    public TemplateSign(Identifier identifier, Class<? extends TileEntityBase> arg, boolean flag) {
        this(BlockTemplate.getNextId(), arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSign(int i, Class<? extends TileEntityBase> arg, boolean flag) {
        super(i, arg, flag);
    }
}
