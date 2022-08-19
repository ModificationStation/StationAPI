package net.modificationstation.stationapi.api.vanillafix.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class FixedLeavesBase extends TemplateBlockBase {

    public static boolean isTransparent;

    public FixedLeavesBase(Identifier identifier, Material material) {
        super(identifier, material);
    }

    @Override
    public boolean isFullOpaque() {
        return !isTransparent;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isSideRendered(BlockView arg, int i, int j, int k, int l) {
        return (isTransparent || !(BlockBase.BY_ID[arg.getTileId(i, j, k)] instanceof FixedLeavesBase)) && super.isSideRendered(arg, i, j, k, l);
    }
}
