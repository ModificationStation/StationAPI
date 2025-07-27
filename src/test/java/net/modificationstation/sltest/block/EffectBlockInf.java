package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.sltest.effect.TestPlayerInfEffect;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class EffectBlockInf extends TemplateBlock {
    public EffectBlockInf(Identifier id) {
        super(id, Material.WOOD);
    }

    @Override
    public int getTextureId(BlockView view, int x, int y, int z, int side) {
        return (side & 1) == 0 ? LOG.textureId : PLANKS.textureId;
    }

    @Override
    public int getTexture(int side) {
        return (side & 1) == 0 ? GOLD_BLOCK.textureId : DIAMOND_BLOCK.textureId;
    }

    @Override
    public boolean onUse(World level, int x, int y, int z, PlayerEntity player) {
        player.addInfiniteEffect(TestPlayerInfEffect.TYPE);
        return true;
    }
}
