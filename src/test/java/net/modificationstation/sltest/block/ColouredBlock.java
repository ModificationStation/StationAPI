package net.modificationstation.sltest.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;

public class ColouredBlock extends BlockBase {

    private final int colour;

    public ColouredBlock(Material material, int colour) {
        super(-1, material);
        this.colour = colour;
        texture = 64;
    }

    @Override
    public int getBaseColour(int i) {
        return colour;
    }

    @Override
    public int getColourMultiplier(BlockView tileView, int x, int y, int z) {
        return colour;
    }
}
