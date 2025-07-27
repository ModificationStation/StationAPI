package net.modificationstation.sltest.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;

public class ColouredBlock extends Block {

    private final int colour;

    public ColouredBlock(Material material, int colour) {
        super(-1, material);
        this.colour = colour;
        textureId = 64;
    }

    @Override
    public int getColor(int i) {
        return colour;
    }

    @Override
    public int getColorMultiplier(BlockView tileView, int x, int y, int z) {
        return colour;
    }
}
