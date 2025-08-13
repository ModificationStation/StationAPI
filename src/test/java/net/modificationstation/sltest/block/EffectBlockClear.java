package net.modificationstation.sltest.block;

import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class EffectBlockClear extends TemplateBlock {
    public EffectBlockClear(Identifier id) {
        super(id, Material.WOOD);
    }

    @Override
    public int getTextureId(BlockView view, int x, int y, int z, int side) {
        return (side & 1) == 0 ? OBSIDIAN.textureId : LAVA.textureId;
    }

    @Override
    public int getTexture(int side) {
        return (side & 1) == 0 ? OBSIDIAN.textureId : LAVA.textureId;
    }

    @Override
    public boolean onUse(World level, int x, int y, int z, PlayerEntity player) {
        player.removeAllEffects();
        return true;
    }
}
