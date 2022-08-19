package net.modificationstation.stationapi.api.vanillafix.block.sapling;

import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.vanillafix.level.structure.FixedBirchTree;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BirchSaplingGenerator extends SaplingGenerator {

    @Override
    protected @Nullable Structure getTreeStructure(Random random) {
        return new FixedBirchTree();
    }
}
