package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSign extends SignBlock implements BlockTemplate {
    
    public TemplateSign(Identifier identifier, Class<? extends BlockEntity> arg, boolean flag) {
        this(BlockTemplate.getNextId(), arg, flag);
        BlockTemplate.onConstructor(this, identifier);
    }
    
    public TemplateSign(int i, Class<? extends BlockEntity> arg, boolean flag) {
        super(i, arg, flag);
    }
}
