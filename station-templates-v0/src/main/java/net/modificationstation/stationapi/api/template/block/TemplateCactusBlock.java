package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.CactusBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateCactusBlock extends CactusBlock implements BlockTemplate {
    public TemplateCactusBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateCactusBlock(int id, int textureId) {
        super(id, textureId);
    }
}
