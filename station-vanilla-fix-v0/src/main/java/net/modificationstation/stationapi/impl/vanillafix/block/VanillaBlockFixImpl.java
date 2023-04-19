package net.modificationstation.stationapi.impl.vanillafix.block;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.StationBlockItemsBlock;
import net.modificationstation.stationapi.api.event.registry.BlockItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.block.BlockBase.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class VanillaBlockFixImpl {

    public static final Supplier<ReferenceSet<BlockBase>> COLLISION_BLOCKS = Suppliers.memoize(() -> Util.make(new ReferenceOpenHashSet<>(), s -> {
        s.add(BED);
        s.add(CROPS);
        s.add(STANDING_SIGN);
        s.add(WOOD_DOOR);
        s.add(IRON_DOOR);
        s.add(SUGAR_CANES);
        s.add(CAKE);
        s.add(REDSTONE_REPEATER);
    }));

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlocks(BlockRegistryEvent event) {
        BlockRegistry registry = event.registry;

        register(registry, "stone", STONE);
        register(registry, "grass_block", GRASS);
        register(registry, "dirt", DIRT);
        register(registry, "cobblestone", COBBLESTONE);
        register(registry, "oak_planks", WOOD);
        register(registry, "sapling", SAPLING);
        register(registry, "bedrock", BEDROCK);
        register(registry, "flowing_water", FLOWING_WATER);
        register(registry, "water", STILL_WATER);
        register(registry, "flowing_lava", FLOWING_LAVA);
        register(registry, "lava", STILL_LAVA);
        register(registry, "sand", SAND);
        register(registry, "gravel", GRAVEL);
        register(registry, "gold_ore", GOLD_ORE);
        register(registry, "iron_ore", IRON_ORE);
        register(registry, "coal_ore", COAL_ORE);
        register(registry, "log", LOG);
        register(registry, "leaves", LEAVES);
        register(registry, "sponge", SPONGE);
        register(registry, "glass", GLASS);
        register(registry, "lapis_ore", LAPIS_LAZULI_ORE);
        register(registry, "lapis_block", LAPIS_LAZULI_BLOCK);
        register(registry, "dispenser", DISPENSER);
        register(registry, "sandstone", SANDSTONE);
        register(registry, "note_block", NOTEBLOCK);
        register(registry, "red_bed", BED);
        register(registry, "powered_rail", GOLDEN_RAIL);
        register(registry, "detector_rail", DETECTOR_RAIL);
        register(registry, "sticky_piston", STICKY_PISTON);
        register(registry, "cobweb", COBWEB);
        register(registry, "grass", TALLGRASS);
        register(registry, "dead_bush", DEADBUSH);
        register(registry, "piston", PISTON);
        register(registry, "piston_head", PISTON_HEAD);
        register(registry, "wool", WOOL);
        register(registry, "moving_piston", MOVING_PISTON);
        register(registry, "dandelion", DANDELION);
        register(registry, "rose", ROSE);
        register(registry, "brown_mushroom", BROWN_MUSHROOM);
        register(registry, "red_mushroom", RED_MUSHROOM);
        register(registry, "gold_block", GOLD_BLOCK);
        register(registry, "iron_block", IRON_BLOCK);
        register(registry, "double_slab", DOUBLE_STONE_SLAB);
        register(registry, "slab", STONE_SLAB);
        register(registry, "bricks", BRICKS);
        register(registry, "tnt", TNT);
        register(registry, "bookshelf", BOOKSHELF);
        register(registry, "mossy_cobblestone", MOSSY_COBBLESTONE);
        register(registry, "obsidian", OBSIDIAN);
        register(registry, "torch", TORCH);
        register(registry, "fire", FIRE);
        register(registry, "spawner", MOB_SPAWNER);
        register(registry, "oak_stairs", WOOD_STAIRS);
        register(registry, "chest", CHEST);
        register(registry, "redstone_wire", REDSTONE_DUST);
        register(registry, "diamond_ore", DIAMOND_ORE);
        register(registry, "diamond_block", DIAMOND_BLOCK);
        register(registry, "crafting_table", WORKBENCH);
        register(registry, "wheat", CROPS);
        register(registry, "farmland", FARMLAND);
        register(registry, "furnace", FURNACE);
        register(registry, "furnace_lit", FURNACE_LIT);
        register(registry, "oak_sign", STANDING_SIGN);
        register(registry, "oak_door", WOOD_DOOR);
        register(registry, "ladder", LADDER);
        register(registry, "rail", RAIL);
        register(registry, "cobblestone_stairs", COBBLESTONE_STAIRS);
        register(registry, "oak_wall_sign", WALL_SIGN);
        register(registry, "lever", LEVER);
        register(registry, "oak_pressure_plate", WOODEN_PRESSURE_PLATE);
        register(registry, "iron_door", IRON_DOOR);
        register(registry, "stone_pressure_plate", STONE_PRESSURE_PLATE);
        register(registry, "redstone_ore", REDSTONE_ORE);
        register(registry, "redstone_ore_lit", REDSTONE_ORE_LIT);
        register(registry, "redstone_torch", REDSTONE_TORCH);
        register(registry, "redstone_torch_lit", REDSTONE_TORCH_LIT);
        register(registry, "stone_button", BUTTON);
        register(registry, "snow", SNOW);
        register(registry, "ice", ICE);
        register(registry, "snow_block", SNOW_BLOCK);
        register(registry, "cactus", CACTUS);
        register(registry, "clay", CLAY);
        register(registry, "sugar_cane", SUGAR_CANES);
        register(registry, "jukebox", JUKEBOX);
        register(registry, "oak_fence", FENCE);
        register(registry, "carved_pumpkin", PUMPKIN);
        register(registry, "netherrack", NETHERRACK);
        register(registry, "soul_sand", SOUL_SAND);
        register(registry, "glowstone", GLOWSTONE);
        register(registry, "nether_portal", PORTAL);
        register(registry, "jack_o_lantern", JACK_O_LANTERN);
        register(registry, "cake", CAKE);
        register(registry, "repeater", REDSTONE_REPEATER);
        register(registry, "repeater_lit", REDSTONE_REPEATER_LIT);
        register(registry, "locked_chest", LOCKED_CHEST);
        register(registry, "oak_trapdoor", TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    private static void register(Registry<BlockBase> registry, String id, BlockBase block) {
        Registry.register(registry, block.id, of(id), block);
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void disableAutomaticBlockItemRegistration(BlockRegistryEvent event) {
        Consumer<BlockBase> c = StationBlockItemsBlock::disableAutomaticBlockItemRegistration;
        c.accept(BY_ID[0]); // not supposed to have an item form
        COLLISION_BLOCKS.get().forEach(c); // item name collision
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlockItems(BlockItemRegistryEvent event) {
        Consumer<BlockBase> c = block -> Registry.register(ItemRegistry.INSTANCE, BlockRegistry.INSTANCE.getId(block), ItemBase.byId[block.id]);

        c.accept(BlockBase.WOOL);
        c.accept(BlockBase.LOG);
        c.accept(BlockBase.STONE_SLAB);
        c.accept(BlockBase.SAPLING);
        c.accept(BlockBase.LEAVES);
        c.accept(BlockBase.PISTON);
        c.accept(BlockBase.STICKY_PISTON);

        COLLISION_BLOCKS.get().forEach(block -> Registry.register(ItemRegistry.INSTANCE, Objects.requireNonNull(BlockRegistry.INSTANCE.getId(block)).append("_unobtainable"), new Block(block.id - BY_ID.length)));
    }
}
