package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplatePistonBlock extends PistonBlock implements BlockTemplate {
    public TemplatePistonBlock(Identifier identifier, int textureId, boolean sticky) {
        this(BlockTemplate.getNextId(), textureId, sticky);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePistonBlock(int id, int textureId, boolean sticky) {
        super(id, textureId, sticky);
    }
}
