package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.sltest.model.ModelListener;
import net.modificationstation.stationapi.api.client.model.BlockModelProvider;
import net.modificationstation.stationapi.api.client.model.CustomModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.template.common.block.TemplateBlockBase;

public class ModdedModelBlock extends TemplateBlockBase implements BlockModelProvider {
    protected ModdedModelBlock(Identifier id, Material material) {
        super(id, material);
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public CustomModel getCustomWorldModel(Level level, int i, int j, int k, int i1) {
        return ModelListener.farlandsBlockModel;
    }

    @Override
    public CustomModel getCustomInventoryModel(int i) {
        return ModelListener.farlandsBlockModel;
    }
}
