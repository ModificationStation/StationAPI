package net.modificationstation.sltest.block;

import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.sltest.SLTest;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.effect.EffectRegistry;
import net.modificationstation.stationapi.api.effect.StationEffectEntity;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class EffectBlock extends TemplateBlock {
    public EffectBlock(Identifier id) {
        super(id, Material.WOOD);
    }

    @Override
    public int getTextureId(BlockView view, int x, int y, int z, int side) {
        return (side & 1) == 0 ? GOLD_BLOCK.textureId : DIAMOND_BLOCK.textureId;
    }

    @Override
    public int getTexture(int side) {
        return (side & 1) == 0 ? GOLD_BLOCK.textureId : DIAMOND_BLOCK.textureId;
    }

    @Override
    public boolean onUse(World level, int x, int y, int z, PlayerEntity player) {
        player.addEffect(SLTest.NAMESPACE.id("test_effect"));
        return true;
    }
}
