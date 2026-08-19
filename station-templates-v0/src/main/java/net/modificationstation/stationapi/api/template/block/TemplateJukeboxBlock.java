package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.JukeboxBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateJukeboxBlock extends JukeboxBlock implements BlockTemplate {
    public TemplateJukeboxBlock(Identifier identifier, int textureId) {
        this(BlockTemplate.getNextId(), textureId);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateJukeboxBlock(int id, int textureId) {
        super(id, textureId);
    }
}
