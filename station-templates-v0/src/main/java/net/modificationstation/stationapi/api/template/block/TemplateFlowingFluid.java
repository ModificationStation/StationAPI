package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.FlowingFluid;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateFlowingFluid extends FlowingFluid implements BlockTemplate {

    public TemplateFlowingFluid(Identifier identifier, Material material) {
        this(BlockRegistry.INSTANCE.getNextLegacyId(), material);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateFlowingFluid(int i, Material arg) {
        super(i, arg);
    }
}
