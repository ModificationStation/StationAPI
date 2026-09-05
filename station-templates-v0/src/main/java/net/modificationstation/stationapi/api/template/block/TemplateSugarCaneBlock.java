package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.SugarCaneBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSugarCaneBlock extends SugarCaneBlock implements BlockTemplate {
    public TemplateSugarCaneBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateSugarCaneBlock(int id, int textureId) {
        super(id, textureId);
    }
}
