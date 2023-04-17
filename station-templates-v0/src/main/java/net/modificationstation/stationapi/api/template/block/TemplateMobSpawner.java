package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.MobSpawner;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateMobSpawner extends MobSpawner implements BlockTemplate {

    public TemplateMobSpawner(Identifier identifier, int j) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), j);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateMobSpawner(int i, int j) {
        super(i, j);
    }
}
