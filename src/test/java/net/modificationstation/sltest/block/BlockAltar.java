package net.modificationstation.sltest.block;

import net.minecraft.block.material.Material;
import net.modificationstation.sltest.texture.TextureListener;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;

public class BlockAltar extends TemplateBlock {

    public BlockAltar(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public int getTexture(int side) {
        return TextureListener.altarTextures[side];
    }
}
