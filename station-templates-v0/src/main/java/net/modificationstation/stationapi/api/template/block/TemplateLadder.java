package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.Ladder;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateLadder extends Ladder implements BlockTemplate {

    public TemplateLadder(Identifier identifier, int texUVStart) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), texUVStart);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateLadder(int id, int texUVStart) {
        super(id, texUVStart);
    }
}
