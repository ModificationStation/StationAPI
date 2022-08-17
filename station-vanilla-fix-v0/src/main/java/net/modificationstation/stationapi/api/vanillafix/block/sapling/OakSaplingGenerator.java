package net.modificationstation.stationapi.api.vanillafix.block.sapling;

import net.minecraft.level.structure.LargeOak;
import net.minecraft.level.structure.OakTree;
import net.minecraft.level.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class OakSaplingGenerator extends SaplingGenerator {

    @Override
    protected @Nullable Structure getTreeStructure(Random random) {
        return random.nextInt(10) == 0 ? new LargeOak() : new OakTree();
    }
}
