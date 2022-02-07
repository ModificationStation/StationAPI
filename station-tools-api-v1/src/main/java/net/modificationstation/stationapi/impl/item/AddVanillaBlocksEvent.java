package net.modificationstation.stationapi.impl.item;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.BlockToolLogic;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.registry.Identifier;

public class AddVanillaBlocksEvent {

    @EventListener(priority = ListenerPriority.HIGHEST)
    public void registerBlocks(BlockRegistryEvent event) {

        // PICKAXES
        mineableBy(BlockBase.STONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.COBBLESTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLD_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.IRON_ORE, Tools.PICKAXE, 1);
        mineableBy(BlockBase.COAL_ORE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.LAPIS_LAZULI_ORE, Tools.PICKAXE, 1);
        mineableBy(BlockBase.LAPIS_LAZULI_BLOCK, Tools.PICKAXE, 1);
        mineableBy(BlockBase.DISPENSER, Tools.PICKAXE, 0);
        mineableBy(BlockBase.SANDSTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLDEN_RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.DETECTOR_RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STICKY_PISTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.PISTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.PISTON_HEAD, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GOLD_BLOCK, Tools.PICKAXE, 2);
        mineableBy(BlockBase.IRON_BLOCK, Tools.PICKAXE, 1);
        mineableBy(BlockBase.DOUBLE_STONE_SLAB, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STONE_SLAB, Tools.PICKAXE, 0);
        mineableBy(BlockBase.BRICKS, Tools.PICKAXE, 0);
        mineableBy(BlockBase.MOSSY_COBBLESTONE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.OBSIDIAN, Tools.PICKAXE, 3);
        mineableBy(BlockBase.MOB_SPAWNER, Tools.PICKAXE, 0);
        mineableBy(BlockBase.DIAMOND_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.DIAMOND_BLOCK, Tools.PICKAXE, 2);
        mineableBy(BlockBase.FURNACE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.FURNACE_LIT, Tools.PICKAXE, 0);
        mineableBy(BlockBase.RAIL, Tools.PICKAXE, 0);
        mineableBy(BlockBase.COBBLESTONE_STAIRS, Tools.PICKAXE, 0);
        mineableBy(BlockBase.IRON_DOOR, Tools.PICKAXE, 0);
        mineableBy(BlockBase.STONE_PRESSURE_PLATE, Tools.PICKAXE, 0);
        mineableBy(BlockBase.REDSTONE_ORE, Tools.PICKAXE, 2);
        mineableBy(BlockBase.REDSTONE_ORE_LIT, Tools.PICKAXE, 2);
        mineableBy(BlockBase.BUTTON, Tools.PICKAXE, 0);
        mineableBy(BlockBase.NETHERRACK, Tools.PICKAXE, 0);
        mineableBy(BlockBase.GLOWSTONE, Tools.PICKAXE, 0);

        // SHOVELS
        mineableBy(BlockBase.GRASS, Tools.SHOVEL, 0);
        mineableBy(BlockBase.DIRT, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SAND, Tools.SHOVEL, 0);
        mineableBy(BlockBase.GRAVEL, Tools.SHOVEL, 0);
        mineableBy(BlockBase.FARMLAND, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SNOW, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SNOW_BLOCK, Tools.SHOVEL, 0);
        mineableBy(BlockBase.CLAY, Tools.SHOVEL, 0);
        mineableBy(BlockBase.SOUL_SAND, Tools.SHOVEL, 0);

        // SWORDS
        mineableBy(BlockBase.COBWEB, Tools.SWORD, 0);

        // AXES
        mineableBy(BlockBase.WOOD, Tools.AXE, 0);
        mineableBy(BlockBase.LOG, Tools.AXE, 0);
        mineableBy(BlockBase.NOTEBLOCK, Tools.AXE, 0);
        mineableBy(BlockBase.BOOKSHELF, Tools.AXE, 0);
        mineableBy(BlockBase.CHEST, Tools.AXE, 0);
        mineableBy(BlockBase.WORKBENCH, Tools.AXE, 0);
        mineableBy(BlockBase.STANDING_SIGN, Tools.AXE, 0);
        mineableBy(BlockBase.WOOD_DOOR, Tools.AXE, 0);
        mineableBy(BlockBase.LADDER, Tools.AXE, 0);
        mineableBy(BlockBase.WALL_SIGN, Tools.AXE, 0);
        mineableBy(BlockBase.LEVER, Tools.AXE, 0);
        mineableBy(BlockBase.WOODEN_PRESSURE_PLATE, Tools.AXE, 0);
        mineableBy(BlockBase.JUKEBOX, Tools.AXE, 0);
        mineableBy(BlockBase.FENCE, Tools.AXE, 0);
        mineableBy(BlockBase.PUMPKIN, Tools.AXE, 0);
        mineableBy(BlockBase.JACK_O_LANTERN, Tools.AXE, 0);
        mineableBy(BlockBase.TRAPDOOR, Tools.AXE, 0);

        // SHEARS
        mineableBy(BlockBase.LEAVES, Tools.SHEARS, 0);
        mineableBy(BlockBase.WOOL, Tools.SHEARS, 0);

    }

    @RequiredArgsConstructor
    private enum Tools {
        PICKAXE("pickaxes"),
        AXE("axes"),
        SHOVEL("shovels"),
        SWORD("swords"),
        SHEARS("shears");

        public final String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private void mineableBy(BlockBase blockBase, Tools tool, int level) {
        ((BlockToolLogic) blockBase).mineableBy(Identifier.of("tools/" + tool), level);
    }
}
