package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.oredict.OreDictRegisterEvent;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.common.util.OreDict;

/**
 * @author calmilamsy
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class OreDictBlockInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockOreDict(OreDictRegisterEvent event) {
        // Basic Blocks
        addBlock0Damage("stone", BlockBase.STONE);
        addBlock0Damage("dirt", BlockBase.DIRT);
        addBlock0Damage("grass", BlockBase.GRASS);
        addBlock0Damage("cobblestone", BlockBase.COBBLESTONE);
        addBlock0Damage("bedrock", BlockBase.BEDROCK);
        addBlock0Damage("sand", BlockBase.SAND);
        addBlock0Damage("gravel", BlockBase.GRAVEL);
        addBlock0Damage("sponge", BlockBase.SPONGE);
        addBlock0Damage("glass", BlockBase.GLASS);
        addBlock0Damage("sandstone", BlockBase.SANDSTONE);
        addBlock0Damage("cobweb", BlockBase.COBWEB);
        addBlock0Damage("wool", BlockBase.WOOL);
        addBlock0Damage("bricks", BlockBase.BRICKS);
        addBlock0Damage("bookshelf", BlockBase.BOOKSHELF);
        addBlock0Damage("cobblestoneMossy", BlockBase.MOSSY_COBBLESTONE);
        addBlock0Damage("obsidian", BlockBase.OBSIDIAN);
        addBlock0Damage("ice", BlockBase.ICE);
        addBlock0Damage("blockSnow", BlockBase.SNOW_BLOCK);
        addBlock0Damage("blockClay", BlockBase.CLAY);
        addBlock0Damage("netherrack", BlockBase.NETHERRACK);
        addBlock0Damage("sandSoul", BlockBase.SOUL_SAND);
        addBlock0Damage("blockGlowstone", BlockBase.GLOWSTONE);

        // Blocks With Fancy Models
        addBlock0Damage("slabStoneDouble", BlockBase.DOUBLE_STONE_SLAB);
        addBlock0Damage("slabStone", BlockBase.STONE_SLAB);
        addBlock0Damage("torch", BlockBase.TORCH);
        addBlock0Damage("fire", BlockBase.FIRE);
        addBlock0Damage("spawnerMob", BlockBase.MOB_SPAWNER);
        addBlock0Damage("stairsWood", BlockBase.WOOD_STAIRS);
        addBlock0Damage("stairsCobblestone", BlockBase.COBBLESTONE_STAIRS);
        addBlockIgnoreDamage("farmland", BlockBase.FARMLAND);
        addBlockIgnoreDamage("door", BlockBase.WOOD_DOOR);
        addBlockIgnoreDamage("door", BlockBase.IRON_DOOR);
        addBlockIgnoreDamage("doorWood", BlockBase.WOOD_DOOR);
        addBlockIgnoreDamage("doorIron", BlockBase.IRON_DOOR);
        addBlockIgnoreDamage("ladder", BlockBase.LADDER);
        addBlockIgnoreDamage("ladderWood", BlockBase.LADDER);
        addBlock0Damage("snow", BlockBase.SNOW);
        addBlock0Damage("fence", BlockBase.FENCE);
        addBlock0Damage("fenceWood", BlockBase.FENCE);
        addBlockIgnoreDamage("cake", BlockBase.CAKE);
        addBlockIgnoreDamage("trapdoor", BlockBase.TRAPDOOR);
        addBlockIgnoreDamage("trapdoorWood", BlockBase.TRAPDOOR);

        // Blocks With GUIs/Inventories
        addBlockIgnoreDamage("chest", BlockBase.CHEST);
        addBlock0Damage("chestWood", BlockBase.CHEST);
        addBlockIgnoreDamage("workbench", BlockBase.WORKBENCH);
        addBlockIgnoreDamage("furnace", BlockBase.FURNACE);
        addBlockIgnoreDamage("furnaceOff", BlockBase.FURNACE);
        addBlockIgnoreDamage("furnace", BlockBase.FURNACE_LIT);
        addBlockIgnoreDamage("furnaceOn", BlockBase.FURNACE_LIT);
        addBlockIgnoreDamage("sign", BlockBase.STANDING_SIGN);
        addBlockIgnoreDamage("sign", BlockBase.WALL_SIGN);
        addBlockIgnoreDamage("signStanding", BlockBase.STANDING_SIGN);
        addBlockIgnoreDamage("signWall", BlockBase.WALL_SIGN);
        addBlockIgnoreDamage("jukebox", BlockBase.JUKEBOX);
        addBlockIgnoreDamage("chestLocked", BlockBase.LOCKED_CHEST);

        // Ores
        addBlock0Damage("oreCoal", BlockBase.COAL_ORE);
        addBlock0Damage("oreIron", BlockBase.IRON_ORE);
        addBlock0Damage("oreGold", BlockBase.GOLD_ORE);
        addBlock0Damage("oreDiamond", BlockBase.DIAMOND_ORE);
        addBlock0Damage("oreLapis", BlockBase.LAPIS_LAZULI_ORE);
        addBlock0Damage("blockIron", BlockBase.IRON_BLOCK);
        addBlock0Damage("blockGold", BlockBase.GOLD_BLOCK);
        addBlock0Damage("blockDiamond", BlockBase.DIAMOND_BLOCK);
        addBlock0Damage("blockLapis", BlockBase.LAPIS_LAZULI_BLOCK);

        addBlock0Damage("oreRedstone", BlockBase.REDSTONE_ORE);
        addBlock0Damage("oreRedstone", BlockBase.REDSTONE_ORE_LIT);
        addBlock0Damage("oreRedstoneLit", BlockBase.REDSTONE_ORE_LIT);

        // Plants
        addBlockIgnoreDamage("logWood", BlockBase.LOG);
        addBlock("logWoodOak", BlockBase.LOG, 0);
        addBlock("logWoodSpruce", BlockBase.LOG, 1);
        addBlock("logWoodBirch", BlockBase.LOG, 2);
        addBlockIgnoreDamage("plankWood", BlockBase.WOOD);
        addBlock("plankWoodOak", BlockBase.WOOD, 0);
        addBlock("plankWoodSpruce", BlockBase.WOOD, 1);
        addBlock("plankWoodBirch", BlockBase.WOOD, 2);
        addBlockIgnoreDamage("plantLeaves", BlockBase.LEAVES);
        addBlock("plantLeavesOak", BlockBase.WOOD, 0);
        addBlock("plantLeavesSpruce", BlockBase.WOOD, 1);
        addBlock("plantLeavesBirch", BlockBase.WOOD, 2);
        addBlockIgnoreDamage("plantSapling", BlockBase.SAPLING);
        addBlock("plantSaplingOak", BlockBase.WOOD, 0);
        addBlock("plantSaplingSpruce", BlockBase.WOOD, 1);
        addBlock("plantSaplingBirch", BlockBase.WOOD, 2);
        addBlock0Damage("plantGrass", BlockBase.TALLGRASS);
        addBlock0Damage("plantBushDead", BlockBase.DEADBUSH);
        addBlock0Damage("plantDandelion", BlockBase.DANDELION);
        addBlock0Damage("plantRose", BlockBase.ROSE);
        addBlock0Damage("plantFlower", BlockBase.DANDELION);
        addBlock0Damage("plantFlower", BlockBase.ROSE);
        addBlock0Damage("plantMushroomBrown", BlockBase.BROWN_MUSHROOM);
        addBlock0Damage("plantMushroomRed", BlockBase.RED_MUSHROOM);
        addBlock0Damage("plantMushroom", BlockBase.BROWN_MUSHROOM);
        addBlock0Damage("plantMushroom", BlockBase.RED_MUSHROOM);
        addBlockIgnoreDamage("plantWheat", BlockBase.CROPS);
        addBlock0Damage("plantCactus", BlockBase.CACTUS);
        addBlock0Damage("plantCaneSugar", BlockBase.SUGAR_CANES);
        addBlock0Damage("pumpkin", BlockBase.PUMPKIN);

        // Railway Stuff
        addBlockIgnoreDamage("rail", BlockBase.RAIL);
        addBlockIgnoreDamage("railPowered", BlockBase.GOLDEN_RAIL);
        addBlockIgnoreDamage("railDetector", BlockBase.DETECTOR_RAIL);

        // Machines
        addBlock0Damage("dispenser", BlockBase.DISPENSER);
        addBlock0Damage("noteblock", BlockBase.NOTEBLOCK);
        addBlock0Damage("railPowered", BlockBase.GOLDEN_RAIL);
        addBlock0Damage("railDetector", BlockBase.DETECTOR_RAIL);
        addBlock0Damage("pistonSticky", BlockBase.STICKY_PISTON);
        addBlock0Damage("piston", BlockBase.PISTON);
        addBlock0Damage("tnt", BlockBase.TNT);
        addBlockIgnoreDamage("redstoneDustPlaced", BlockBase.REDSTONE_DUST);
        addBlockIgnoreDamage("lever", BlockBase.LEVER);
        addBlock0Damage("pressureplateWood", BlockBase.WOODEN_PRESSURE_PLATE);
        addBlock0Damage("pressureplateStone", BlockBase.STONE_PRESSURE_PLATE);
        addBlock0Damage("pressureplate", BlockBase.WOODEN_PRESSURE_PLATE);
        addBlock0Damage("pressureplate", BlockBase.STONE_PRESSURE_PLATE);
        addBlockIgnoreDamage("torchRedstone", BlockBase.REDSTONE_TORCH);
        addBlockIgnoreDamage("torchRedstoneOff", BlockBase.REDSTONE_TORCH);
        addBlockIgnoreDamage("torchRedstone", BlockBase.REDSTONE_TORCH_LIT);
        addBlockIgnoreDamage("torchRedstoneOn", BlockBase.REDSTONE_TORCH_LIT);
        addBlockIgnoreDamage("buttonStone", BlockBase.BUTTON);
        addBlockIgnoreDamage("repeaterRedstone", BlockBase.REDSTONE_REPEATER);
        addBlockIgnoreDamage("repeaterRedstone", BlockBase.REDSTONE_REPEATER_LIT);
        addBlockIgnoreDamage("repeaterRedstoneOff", BlockBase.REDSTONE_REPEATER);
        addBlockIgnoreDamage("repeaterRedstoneOn", BlockBase.REDSTONE_REPEATER_LIT);

        StationAPI.LOGGER.info("Registered vanilla block oredict.");
    }

    private static void addBlockIgnoreDamage(String oreDictString, BlockBase blockBase) {
        OreDict.INSTANCE.addBlockIgnoreDamage(oreDictString, blockBase);
    }

    //TODO: Figure out if referencing ItemInstance before ItemRegister is fine or bad.
    //TODO: Bad. Decidedly.
    private static void addBlock0Damage(String oreDictString, BlockBase blockBase) {
        OreDict.INSTANCE.addItemInstance(oreDictString, new ItemInstance(blockBase, 1, 0));
    }

    private static void addBlock(String oreDictString, BlockBase blockBase, int damage) {
        OreDict.INSTANCE.addItemInstance(oreDictString, new ItemInstance(blockBase, 1, damage));
    }
}
