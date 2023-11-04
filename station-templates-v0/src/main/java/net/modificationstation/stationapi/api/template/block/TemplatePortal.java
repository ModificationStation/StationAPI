package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.NetherPortalBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePortal extends NetherPortalBlock implements BlockTemplate {

    public TemplatePortal(Identifier identifier, int j) {
        this(BlockTemplate.getNextId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePortal(int i, int j) {
        super(i, j);
    }
}
