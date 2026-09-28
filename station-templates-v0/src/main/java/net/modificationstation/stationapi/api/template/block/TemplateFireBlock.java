package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FireBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateFireBlock extends FireBlock implements BlockTemplate {
    public TemplateFireBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFireBlock(int id, int textureId) {
        super(id, textureId);
    }
}
