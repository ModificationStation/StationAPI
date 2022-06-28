package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class ColouredBlock extends TemplateBlockBase {

    private final int colour;

    public ColouredBlock(Identifier identifier, Material material, int colour) {
        super(identifier, material);
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
