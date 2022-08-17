package net.modificationstation.stationapi.api.vanillafix.block.sapling;

import net.minecraft.level.structure.SpruceTree;
import net.minecraft.level.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SpruceSaplingGenerator extends SaplingGenerator {

    @Override
    protected @Nullable Structure getTreeStructure(Random random) {
        return new SpruceTree();
    }
}
