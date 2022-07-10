package net.modificationstation.stationapi.api.vanillafix.block;

import com.google.common.base.Supplier;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.jetbrains.annotations.NotNull;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

public enum Blocks implements Supplier<@NotNull BlockBase> {

    STONE("stone"),
    GRASS_BLOCK("grass_block"),
    DIRT("dirt"),
    COBBLESTONE("cobblestone"),
    PLANKS("planks"),
    SAPLING("sapling"),
    BEDROCK("bedrock"),
    FLOWING_WATER("flowing_water"),
    WATER("water"),
    FLOWING_LAVA("flowing_lava"),
    LAVA("lava"),
    SAND("sand"),
    GRAVEL("gravel"),
    GOLD_ORE("gold_ore"),
    IRON_ORE("iron_ore"),
    COAL_ORE("coal_ore"),
    LOG("log"),
    LEAVES("leaves"),
    SPONGE("sponge"),
    GLASS("glass"),
    LAPIS_ORE("lapis_ore"),
    LAPIS_BLOCK("lapis_block"),
    DISPENSER("dispenser"),
    SANDSTONE("sandstone"),
    NOTE_BLOCK("note_block"),
    BED("bed"),
    POWERED_RAIL("powered_rail"),
    DETECTOR_RAIL("detector_rail"),
    STICKY_PISTON("sticky_piston"),
    COBWEB("cobweb"),
    GRASS("grass"),
    DEAD_BUSH("dead_bush"),
    PISTON("piston"),
    PISTON_HEAD("piston_head"),
    WOOL("wool"),
    MOVING_PISTON("moving_piston"),
    DANDELION("dandelion"),
    ROSE("rose"),
    BROWN_MUSHROOM("brown_mushroom"),
    RED_MUSHROOM("red_mushroom"),
    GOLD_BLOCK("gold_block"),
    IRON_BLOCK("iron_block"),
    DOUBLE_SLAB("double_slab"),
    SLAB("slab"),
    BRICKS("bricks"),
    TNT("tnt"),
    BOOKSHELF("bookshelf"),
    MOSSY_COBBLESTONE("mossy_cobblestone"),
    OBSIDIAN("obsidian"),
    TORCH("torch"),
    FIRE("fire"),
    SPAWNER("spawner"),
    OAK_STAIRS("oak_stairs"),
    CHEST("chest"),
    REDSTONE_WIRE("redstone_wire"),
    DIAMOND_ORE("diamond_ore"),
    DIAMOND_BLOCK("diamond_block"),
    CRAFTING_TABLE("crafting_table"),
    WHEAT("wheat"),
    FARMLAND("farmland"),
    FURNACE("furnace"),
    FURNACE_LIT("furnace_lit"),
    SIGN("sign"),
    OAK_DOOR("oak_door"),
    LADDER("ladder"),
    RAIL("rail"),
    COBBLESTONE_STAIRS("cobblestone_stairs"),
    WALL_SIGN("wall_sign"),
    LEVER("lever"),
    OAK_PRESSURE_PLATE("oak_pressure_plate"),
    IRON_DOOR("iron_door"),
    STONE_PRESSURE_PLATE("stone_pressure_plate"),
    REDSTONE_ORE("redstone_ore"),
    REDSTONE_ORE_LIT("redstone_ore_lit"),
    REDSTONE_TORCH("redstone_torch"),
    REDSTONE_TORCH_LIT("redstone_torch_lit"),
    BUTTON("button"),
    SNOW("snow"),
    ICE("ice"),
    SNOW_BLOCK("snow_block"),
    CACTUS("cactus"),
    CLAY("clay"),
    SUGAR_CANE("sugar_cane"),
    JUKEBOX("jukebox"),
    FENCE("fence"),
    PUMPKIN("pumpkin"),
    NETHERRACK("netherrack"),
    SOUL_SAND("soul_sand"),
    GLOWSTONE("glowstone"),
    PORTAL("portal"),
    JACK_O_LANTERN("jack_o_lantern"),
    CAKE("cake"),
    REPEATER("repeater"),
    REPEATER_LIT("repeater_lit"),
    LOCKED_CHEST("locked_chest"),
    TRAPDOOR("trapdoor");

    @NotNull
    public final Identifier id;

    Blocks(final @NotNull String id) {
        this.id = of(id);
    }

    @Override
    public @NotNull BlockBase get() {
        return BlockRegistry.INSTANCE.get(id).orElseThrow();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
