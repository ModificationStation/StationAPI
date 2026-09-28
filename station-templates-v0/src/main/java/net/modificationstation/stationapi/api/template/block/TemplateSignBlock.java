package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSignBlock extends SignBlock implements BlockTemplate {
    public TemplateSignBlock(Identifier identifier, Class<? extends BlockEntity> blockEntityClazz, boolean standing) {
        this(BlockTemplate.getNextId(), blockEntityClazz, standing);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSignBlock(int id, Class<? extends BlockEntity> blockEntityClazz, boolean standing) {
        super(id, blockEntityClazz, standing);
    }
}
