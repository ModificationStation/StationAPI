package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class ModdedModelBlock extends TemplateBlockBase /*implements BlockInventoryModelProvider, BlockWorldModelProvider*/ {
    protected ModdedModelBlock(Identifier id, Material material) {
        super(id, material);
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public void afterPlaced(Level level, int x, int y, int z, Living living) {
        level.setTileMeta(x, y, z, MathHelper.floor((double)(living.yaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

//    @Override
//    public JsonModel getCustomWorldModel(Level level, int i, int j, int k, int i1) {
//        return TextureListener.farlandsBlockModel;
//    }

//    @Override
//    public JsonUnbakedModel getInventoryModel(int i) {
//        return TextureListener.farlandsBlockModel;
//    }
//
//    @Override
//    public JsonUnbakedModel getCustomWorldModel(BlockView blockView, int x, int y, int z) {
//        return TextureListener.farlandsBlockModel;
//    }
}
