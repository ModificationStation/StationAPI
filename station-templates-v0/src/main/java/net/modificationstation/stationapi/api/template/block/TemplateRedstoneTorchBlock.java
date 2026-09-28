package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.RedstoneTorchBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateRedstoneTorchBlock extends RedstoneTorchBlock implements BlockTemplate {
    public TemplateRedstoneTorchBlock(Identifier identifier, int textureId, boolean lit) {
        this(BlockTemplate.getNextId(), textureId, lit);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateRedstoneTorchBlock(int id, int textureId, boolean lit) {
        super(id, textureId, lit);
    }
}
