package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NetherPortalBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateNetherPortalBlock extends NetherPortalBlock implements BlockTemplate {
    public TemplateNetherPortalBlock(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateNetherPortalBlock(int i, int j) {
        super(i, j);
    }
}
