package net.modificationstation.stationapi.impl.common.item;

import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.common.util.OreDict;

public class OreDictInit {

    public void setupVanilla() {
        // Basic Blocks
        OreDict.INSTANCE.addBlockIgnoreDamage("stone", BlockBase.STONE);
        OreDict.INSTANCE.addBlockIgnoreDamage("dirt", BlockBase.DIRT);
        OreDict.INSTANCE.addBlockIgnoreDamage("grass", BlockBase.GRASS);
        OreDict.INSTANCE.addBlockIgnoreDamage("cobblestone", BlockBase.COBBLESTONE);
        OreDict.INSTANCE.addBlockIgnoreDamage("bedrock", BlockBase.BEDROCK);
        OreDict.INSTANCE.addBlockIgnoreDamage("sand", BlockBase.SAND);
        OreDict.INSTANCE.addBlockIgnoreDamage("gravel", BlockBase.GRAVEL);
        OreDict.INSTANCE.addBlockIgnoreDamage("sponge", BlockBase.SPONGE);
        OreDict.INSTANCE.addBlockIgnoreDamage("glass", BlockBase.GLASS);
        OreDict.INSTANCE.addBlockIgnoreDamage("sandstone", BlockBase.SANDSTONE);
        OreDict.INSTANCE.addBlockIgnoreDamage("cobweb", BlockBase.COBWEB);
        OreDict.INSTANCE.addBlockIgnoreDamage("wool", BlockBase.WOOL);

        // Ores
        OreDict.INSTANCE.addBlockIgnoreDamage("oreCoal", BlockBase.COAL_ORE);
        OreDict.INSTANCE.addBlockIgnoreDamage("oreIron", BlockBase.IRON_ORE);
        OreDict.INSTANCE.addBlockIgnoreDamage("oreGold", BlockBase.GOLD_ORE);
        OreDict.INSTANCE.addBlockIgnoreDamage("oreDiamond", BlockBase.DIAMOND_ORE);
        OreDict.INSTANCE.addBlockIgnoreDamage("oreLapis", BlockBase.LAPIS_LAZULI_ORE);
        OreDict.INSTANCE.addBlockIgnoreDamage("blockIron", BlockBase.IRON_BLOCK);
        OreDict.INSTANCE.addBlockIgnoreDamage("blockGold", BlockBase.GOLD_BLOCK);
        OreDict.INSTANCE.addBlockIgnoreDamage("blockDiamond", BlockBase.DIAMOND_BLOCK);
        OreDict.INSTANCE.addBlockIgnoreDamage("blockLapis", BlockBase.LAPIS_LAZULI_BLOCK);

        // Plants
        OreDict.INSTANCE.addBlockIgnoreDamage("logWood", BlockBase.LOG);
        OreDict.INSTANCE.addBlockIgnoreDamage("plankWood", BlockBase.WOOD);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantLeaves", BlockBase.LEAVES);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantSapling", BlockBase.SAPLING);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantGrass", BlockBase.TALLGRASS);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantBushDead", BlockBase.DEADBUSH);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantDandelion", BlockBase.DANDELION);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantRose", BlockBase.ROSE);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantFlower", BlockBase.DANDELION);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantFlower", BlockBase.ROSE);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantMushroomBrown", BlockBase.BROWN_MUSHROOM);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantMushroomRed", BlockBase.RED_MUSHROOM);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantMushroom", BlockBase.BROWN_MUSHROOM);
        OreDict.INSTANCE.addBlockIgnoreDamage("plantMushroom", BlockBase.RED_MUSHROOM);

        // Machines
        OreDict.INSTANCE.addBlockIgnoreDamage("dispenser", BlockBase.DISPENSER);
        OreDict.INSTANCE.addBlockIgnoreDamage("noteblock", BlockBase.NOTEBLOCK);
        OreDict.INSTANCE.addBlockIgnoreDamage("railPowered", BlockBase.GOLDEN_RAIL);
        OreDict.INSTANCE.addBlockIgnoreDamage("railDetector", BlockBase.DETECTOR_RAIL);
        OreDict.INSTANCE.addBlockIgnoreDamage("pistonSticky", BlockBase.STICKY_PISTON);
        OreDict.INSTANCE.addBlockIgnoreDamage("piston", BlockBase.PISTON);
    }
}
