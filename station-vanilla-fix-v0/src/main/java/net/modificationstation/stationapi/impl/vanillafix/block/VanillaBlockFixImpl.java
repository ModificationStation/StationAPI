package net.modificationstation.stationapi.impl.vanillafix.block;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.StationBlockItemsBlock;
import net.modificationstation.stationapi.api.event.registry.BlockItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Util;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.LOW;
import static net.minecraft.block.Block.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaBlockFixImpl {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    public static final Supplier<ReferenceSet<Block>> COLLISION_BLOCKS = Suppliers.memoize(() -> Util.make(new ReferenceOpenHashSet<>(), s -> {
        s.add(BED);
        s.add(WHEAT);
        s.add(SIGN);
        s.add(DOOR);
        s.add(IRON_DOOR);
        s.add(SUGAR_CANE);
        s.add(CAKE);
        s.add(REPEATER);
    }));

    @EventListener
    private static void registerBlocks(BlockRegistryEvent event) {
        BlockRegistry registry = event.registry;

        register(registry, "stone", STONE);
        register(registry, "grass_block", GRASS_BLOCK);
        register(registry, "dirt", DIRT);
        register(registry, "cobblestone", COBBLESTONE);
        register(registry, "oak_planks", PLANKS);
        register(registry, "sapling", SAPLING);
        register(registry, "bedrock", BEDROCK);
        register(registry, "flowing_water", FLOWING_WATER);
        register(registry, "water", WATER);
        register(registry, "flowing_lava", FLOWING_LAVA);
        register(registry, "lava", LAVA);
        register(registry, "sand", SAND);
        register(registry, "gravel", GRAVEL);
        register(registry, "gold_ore", GOLD_ORE);
        register(registry, "iron_ore", IRON_ORE);
        register(registry, "coal_ore", COAL_ORE);
        register(registry, "log", LOG);
        register(registry, "leaves", LEAVES);
        register(registry, "sponge", SPONGE);
        register(registry, "glass", GLASS);
        register(registry, "lapis_ore", LAPIS_ORE);
        register(registry, "lapis_block", LAPIS_BLOCK);
        register(registry, "dispenser", DISPENSER);
        register(registry, "sandstone", SANDSTONE);
        register(registry, "note_block", NOTE_BLOCK);
        register(registry, "red_bed", BED);
        register(registry, "powered_rail", POWERED_RAIL);
        register(registry, "detector_rail", DETECTOR_RAIL);
        register(registry, "sticky_piston", STICKY_PISTON);
        register(registry, "cobweb", COBWEB);
        register(registry, "grass", GRASS);
        register(registry, "dead_bush", DEAD_BUSH);
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
        register(registry, "double_slab", DOUBLE_SLAB);
        register(registry, "slab", SLAB);
        register(registry, "bricks", BRICKS);
        register(registry, "tnt", TNT);
        register(registry, "bookshelf", BOOKSHELF);
        register(registry, "mossy_cobblestone", MOSSY_COBBLESTONE);
        register(registry, "obsidian", OBSIDIAN);
        register(registry, "torch", TORCH);
        register(registry, "fire", FIRE);
        register(registry, "spawner", SPAWNER);
        register(registry, "oak_stairs", WOODEN_STAIRS);
        register(registry, "chest", CHEST);
        register(registry, "redstone_wire", REDSTONE_WIRE);
        register(registry, "diamond_ore", DIAMOND_ORE);
        register(registry, "diamond_block", DIAMOND_BLOCK);
        register(registry, "crafting_table", CRAFTING_TABLE);
        register(registry, "wheat", WHEAT);
        register(registry, "farmland", FARMLAND);
        register(registry, "furnace", FURNACE);
        register(registry, "furnace_lit", LIT_FURNACE);
        register(registry, "oak_sign", SIGN);
        register(registry, "oak_door", DOOR);
        register(registry, "ladder", LADDER);
        register(registry, "rail", RAIL);
        register(registry, "cobblestone_stairs", COBBLESTONE_STAIRS);
        register(registry, "oak_wall_sign", WALL_SIGN);
        register(registry, "lever", LEVER);
        register(registry, "oak_pressure_plate", STONE_PRESSURE_PLATE);
        register(registry, "iron_door", IRON_DOOR);
        register(registry, "stone_pressure_plate", WOODEN_PRESSURE_PLATE);
        register(registry, "redstone_ore", REDSTONE_ORE);
        register(registry, "redstone_ore_lit", LIT_REDSTONE_ORE);
        register(registry, "redstone_torch", REDSTONE_TORCH);
        register(registry, "redstone_torch_lit", LIT_REDSTONE_TORCH);
        register(registry, "stone_button", BUTTON);
        register(registry, "snow", SNOW);
        register(registry, "ice", ICE);
        register(registry, "snow_block", SNOW_BLOCK);
        register(registry, "cactus", CACTUS);
        register(registry, "clay", CLAY);
        register(registry, "sugar_cane", SUGAR_CANE);
        register(registry, "jukebox", JUKEBOX);
        register(registry, "oak_fence", FENCE);
        register(registry, "carved_pumpkin", PUMPKIN);
        register(registry, "netherrack", NETHERRACK);
        register(registry, "soul_sand", SOUL_SAND);
        register(registry, "glowstone", GLOWSTONE);
        register(registry, "nether_portal", NETHER_PORTAL);
        register(registry, "jack_o_lantern", JACK_O_LANTERN);
        register(registry, "cake", CAKE);
        register(registry, "repeater", REPEATER);
        register(registry, "repeater_lit", POWERED_REPEATER);
        register(registry, "locked_chest", LOCKED_CHEST);
        register(registry, "oak_trapdoor", TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    private static void register(Registry<Block> registry, String id, Block block) {
        Registry.register(registry, block.id, of(id), block);
    }

    @EventListener(priority = LOW)
    private static void disableAutomaticBlockItemRegistration(BlockRegistryEvent event) {
        Consumer<Block> c = StationBlockItemsBlock::disableAutoItemRegistration;
        c.accept(BLOCKS[0]); // not supposed to have an item form
        COLLISION_BLOCKS.get().forEach(c); // item name collision
    }

    @EventListener
    private static void registerBlockItems(BlockItemRegistryEvent event) {
        Consumer<Block> c = block -> Registry.register(ItemRegistry.INSTANCE, BlockRegistry.INSTANCE.getId(block), Item.ITEMS[block.id]);

        c.accept(Block.WOOL);
        c.accept(Block.LOG);
        c.accept(Block.SLAB);
        c.accept(Block.SAPLING);
        c.accept(Block.LEAVES);
        c.accept(Block.PISTON);
        c.accept(Block.STICKY_PISTON);

        COLLISION_BLOCKS.get().forEach(block -> Registry.register(ItemRegistry.INSTANCE, Objects.requireNonNull(BlockRegistry.INSTANCE.getId(block)).withSuffixedPath("_unobtainable"), new BlockItem(ItemRegistry.SHIFTED_ID.get(block.id))));
    }
}
