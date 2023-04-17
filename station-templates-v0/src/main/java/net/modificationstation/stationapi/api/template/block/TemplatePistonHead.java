package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.PistonHead;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplatePistonHead extends PistonHead implements BlockTemplate {

    public TemplatePistonHead(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplatePistonHead(int i, int j) {
        super(i, j);
    }
}
