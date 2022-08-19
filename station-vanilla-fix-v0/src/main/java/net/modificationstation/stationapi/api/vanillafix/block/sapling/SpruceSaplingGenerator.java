package net.modificationstation.stationapi.api.vanillafix.block.sapling;

import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedSpruceTree;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SpruceSaplingGenerator extends SaplingGenerator {

    @Override
    protected @Nullable Structure getTreeStructure(Random random) {
        return new FixedSpruceTree();
    }
}
