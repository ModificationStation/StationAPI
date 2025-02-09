package net.modificationstation.stationapi.impl.vanillafix.block;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.StationBlockItemsBlock;
import net.modificationstation.stationapi.api.event.registry.BlockItemRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Util;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.mine_diver.unsafeevents.listener.ListenerPriority.LOW;
import static net.minecraft.block.Block.*;
import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaBlockFixImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
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
        event.register(block -> block.id, Namespace.MINECRAFT)
                .accept("stone", STONE)
                .accept("grass_block", GRASS_BLOCK)
                .accept("dirt", DIRT)
                .accept("cobblestone", COBBLESTONE)
                .accept("oak_planks", PLANKS)
                .accept("sapling", SAPLING)
                .accept("bedrock", BEDROCK)
                .accept("flowing_water", FLOWING_WATER)
                .accept("water", WATER)
                .accept("flowing_lava", FLOWING_LAVA)
                .accept("lava", LAVA)
                .accept("sand", SAND)
                .accept("gravel", GRAVEL)
                .accept("gold_ore", GOLD_ORE)
                .accept("iron_ore", IRON_ORE)
                .accept("coal_ore", COAL_ORE)
                .accept("log", LOG)
                .accept("leaves", LEAVES)
                .accept("sponge", SPONGE)
                .accept("glass", GLASS)
                .accept("lapis_ore", LAPIS_ORE)
                .accept("lapis_block", LAPIS_BLOCK)
                .accept("dispenser", DISPENSER)
                .accept("sandstone", SANDSTONE)
                .accept("note_block", NOTE_BLOCK)
                .accept("red_bed", BED)
                .accept("powered_rail", POWERED_RAIL)
                .accept("detector_rail", DETECTOR_RAIL)
                .accept("sticky_piston", STICKY_PISTON)
                .accept("cobweb", COBWEB)
                .accept("grass", GRASS)
                .accept("dead_bush", DEAD_BUSH)
                .accept("piston", PISTON)
                .accept("piston_head", PISTON_HEAD)
                .accept("wool", WOOL)
                .accept("moving_piston", MOVING_PISTON)
                .accept("dandelion", DANDELION)
                .accept("rose", ROSE)
                .accept("brown_mushroom", BROWN_MUSHROOM)
                .accept("red_mushroom", RED_MUSHROOM)
                .accept("gold_block", GOLD_BLOCK)
                .accept("iron_block", IRON_BLOCK)
                .accept("double_slab", DOUBLE_SLAB)
                .accept("slab", SLAB)
                .accept("bricks", BRICKS)
                .accept("tnt", TNT)
                .accept("bookshelf", BOOKSHELF)
                .accept("mossy_cobblestone", MOSSY_COBBLESTONE)
                .accept("obsidian", OBSIDIAN)
                .accept("torch", TORCH)
                .accept("fire", FIRE)
                .accept("spawner", SPAWNER)
                .accept("oak_stairs", WOODEN_STAIRS)
                .accept("chest", CHEST)
                .accept("redstone_wire", REDSTONE_WIRE)
                .accept("diamond_ore", DIAMOND_ORE)
                .accept("diamond_block", DIAMOND_BLOCK)
                .accept("crafting_table", CRAFTING_TABLE)
                .accept("wheat", WHEAT)
                .accept("farmland", FARMLAND)
                .accept("furnace", FURNACE)
                .accept("furnace_lit", LIT_FURNACE)
                .accept("oak_sign", SIGN)
                .accept("oak_door", DOOR)
                .accept("ladder", LADDER)
                .accept("rail", RAIL)
                .accept("cobblestone_stairs", COBBLESTONE_STAIRS)
                .accept("oak_wall_sign", WALL_SIGN)
                .accept("lever", LEVER)
                .accept("oak_pressure_plate", STONE_PRESSURE_PLATE)
                .accept("iron_door", IRON_DOOR)
                .accept("stone_pressure_plate", WOODEN_PRESSURE_PLATE)
                .accept("redstone_ore", REDSTONE_ORE)
                .accept("redstone_ore_lit", LIT_REDSTONE_ORE)
                .accept("redstone_torch", REDSTONE_TORCH)
                .accept("redstone_torch_lit", LIT_REDSTONE_TORCH)
                .accept("stone_button", BUTTON)
                .accept("snow", SNOW)
                .accept("ice", ICE)
                .accept("snow_block", SNOW_BLOCK)
                .accept("cactus", CACTUS)
                .accept("clay", CLAY)
                .accept("sugar_cane", SUGAR_CANE)
                .accept("jukebox", JUKEBOX)
                .accept("oak_fence", FENCE)
                .accept("carved_pumpkin", PUMPKIN)
                .accept("netherrack", NETHERRACK)
                .accept("soul_sand", SOUL_SAND)
                .accept("glowstone", GLOWSTONE)
                .accept("nether_portal", NETHER_PORTAL)
                .accept("jack_o_lantern", JACK_O_LANTERN)
                .accept("cake", CAKE)
                .accept("repeater", REPEATER)
                .accept("repeater_lit", POWERED_REPEATER)
                .accept("locked_chest", LOCKED_CHEST)
                .accept("oak_trapdoor", TRAPDOOR);

        LOGGER.info("Added vanilla blocks to the registry.");
    }

    @EventListener(priority = LOW)
    private static void disableAutomaticBlockItemRegistration(BlockRegistryEvent event) {
        Consumer<Block> c = StationBlockItemsBlock::disableAutoItemRegistration;
        c.accept(BLOCKS[0]); // not supposed to have an item form
        COLLISION_BLOCKS.get().forEach(c); // item name collision
    }

    @EventListener
    private static void registerBlockItems(BlockItemRegistryEvent event) {
        Consumer<Block> c = block -> event.register(BlockRegistry.INSTANCE.getId(block), Item.ITEMS[block.id]);

        c.accept(Block.WOOL);
        c.accept(Block.LOG);
        c.accept(Block.SLAB);
        c.accept(Block.SAPLING);
        c.accept(Block.LEAVES);
        c.accept(Block.PISTON);
        c.accept(Block.STICKY_PISTON);

        COLLISION_BLOCKS.get().forEach(block -> event.register(Objects.requireNonNull(BlockRegistry.INSTANCE.getId(block)).withSuffixedPath("_unobtainable"), new BlockItem(ItemRegistry.SHIFTED_ID.get(block.id))));
    }
}
