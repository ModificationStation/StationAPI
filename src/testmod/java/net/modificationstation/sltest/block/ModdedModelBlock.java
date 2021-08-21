package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.sltest.texture.TextureListener;
import net.modificationstation.stationapi.api.client.model.BlockInventoryModelProvider;
import net.modificationstation.stationapi.api.client.model.BlockWorldModelProvider;
import net.modificationstation.stationapi.api.client.model.JsonModel;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class ModdedModelBlock extends TemplateBlockBase implements BlockInventoryModelProvider, BlockWorldModelProvider {
    protected ModdedModelBlock(Identifier id, Material material) {
        super(id, material);
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

//    @Override
//    public JsonModel getCustomWorldModel(Level level, int i, int j, int k, int i1) {
//        return TextureListener.farlandsBlockModel;
//    }

    @Override
    public JsonModel getInventoryModel(int i) {
        return TextureListener.farlandsBlockModel;
    }

    @Override
    public JsonModel getCustomWorldModel(BlockView blockView, int x, int y, int z) {
        return TextureListener.farlandsBlockModel;
    }
}
