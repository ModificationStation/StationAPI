package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NetherPortalBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateNetherPortalBlock extends NetherPortalBlock implements BlockTemplate {
    public TemplateNetherPortalBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateNetherPortalBlock(int id, int textureId) {
        super(id, textureId);
    }
}
