package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.vanillafix.datafixer.TypeReferences;

import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager.SECTIONS;
import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class Schema69420 extends Schema {

    public static final Int2ObjectMap<String> BLOCK_RENAMES = Util.make(new Int2ObjectOpenHashMap<>(), m -> {
        m.defaultReturnValue("minecraft:air");
        m.put(1, "minecraft:stone");
        m.put(2, "minecraft:grass_block");
        m.put(3, "minecraft:dirt");
        m.put(4, "minecraft:cobblestone");
        m.put(5, "minecraft:oak_planks");
        m.put(6, "minecraft:sapling");
        m.put(7, "minecraft:bedrock");
        m.put(8, "minecraft:flowing_water");
        m.put(9, "minecraft:water");
        m.put(10, "minecraft:flowing_lava");
        m.put(11, "minecraft:lava");
        m.put(12, "minecraft:sand");
        m.put(13, "minecraft:gravel");
        m.put(14, "minecraft:gold_ore");
        m.put(15, "minecraft:iron_ore");
        m.put(16, "minecraft:coal_ore");
        m.put(17, "minecraft:log");
        m.put(18, "minecraft:leaves");
        m.put(19, "minecraft:sponge");
        m.put(20, "minecraft:glass");
        m.put(21, "minecraft:lapis_ore");
        m.put(22, "minecraft:lapis_block");
        m.put(23, "minecraft:dispenser");
        m.put(24, "minecraft:sandstone");
        m.put(25, "minecraft:note_block");
        m.put(26, "minecraft:red_bed");
        m.put(27, "minecraft:powered_rail");
        m.put(28, "minecraft:detector_rail");
        m.put(29, "minecraft:sticky_piston");
        m.put(30, "minecraft:cobweb");
        m.put(31, "minecraft:grass");
        m.put(32, "minecraft:dead_bush");
        m.put(33, "minecraft:piston");
        m.put(34, "minecraft:piston_head");
        m.put(35, "minecraft:wool");
        m.put(36, "minecraft:moving_piston");
        m.put(37, "minecraft:dandelion");
        m.put(38, "minecraft:rose");
        m.put(39, "minecraft:brown_mushroom");
        m.put(40, "minecraft:red_mushroom");
        m.put(41, "minecraft:gold_block");
        m.put(42, "minecraft:iron_block");
        m.put(43, "minecraft:double_slab");
        m.put(44, "minecraft:slab");
        m.put(45, "minecraft:bricks");
        m.put(46, "minecraft:tnt");
        m.put(47, "minecraft:bookshelf");
        m.put(48, "minecraft:mossy_cobblestone");
        m.put(49, "minecraft:obsidian");
        m.put(50, "minecraft:torch");
        m.put(51, "minecraft:fire");
        m.put(52, "minecraft:spawner");
        m.put(53, "minecraft:oak_stairs");
        m.put(54, "minecraft:chest");
        m.put(55, "minecraft:redstone_wire");
        m.put(56, "minecraft:diamond_ore");
        m.put(57, "minecraft:diamond_block");
        m.put(58, "minecraft:crafting_table");
        m.put(59, "minecraft:wheat");
        m.put(60, "minecraft:farmland");
        m.put(61, "minecraft:furnace");
        m.put(62, "minecraft:furnace_lit");
        m.put(63, "minecraft:oak_sign");
        m.put(64, "minecraft:oak_door");
        m.put(65, "minecraft:ladder");
        m.put(66, "minecraft:rail");
        m.put(67, "minecraft:cobblestone_stairs");
        m.put(68, "minecraft:oak_wall_sign");
        m.put(69, "minecraft:lever");
        m.put(70, "minecraft:oak_pressure_plate");
        m.put(71, "minecraft:iron_door");
        m.put(72, "minecraft:stone_pressure_plate");
        m.put(73, "minecraft:redstone_ore");
        m.put(74, "minecraft:redstone_ore_lit");
        m.put(75, "minecraft:redstone_torch");
        m.put(76, "minecraft:redstone_torch_lit");
        m.put(77, "minecraft:stone_button");
        m.put(78, "minecraft:snow");
        m.put(79, "minecraft:ice");
        m.put(80, "minecraft:snow_block");
        m.put(81, "minecraft:cactus");
        m.put(82, "minecraft:clay");
        m.put(83, "minecraft:sugar_cane");
        m.put(84, "minecraft:jukebox");
        m.put(85, "minecraft:oak_fence");
        m.put(86, "minecraft:carved_pumpkin");
        m.put(87, "minecraft:netherrack");
        m.put(88, "minecraft:soul_sand");
        m.put(89, "minecraft:glowstone");
        m.put(90, "minecraft:nether_portal");
        m.put(91, "minecraft:jack_o_lantern");
        m.put(92, "minecraft:cake");
        m.put(93, "minecraft:repeater");
        m.put(94, "minecraft:repeater_lit");
        m.put(95, "minecraft:locked_chest");
        m.put(96, "minecraft:oak_trapdoor");
    });
    public static final Int2ObjectMap<String> ITEM_RENAMES = Util.make(new Int2ObjectOpenHashMap<>(), m -> {
        m.defaultReturnValue(BLOCK_RENAMES.defaultReturnValue());
        m.putAll(BLOCK_RENAMES);
        m.put(256, "minecraft:iron_shovel");
        m.put(257, "minecraft:iron_pickaxe");
        m.put(258, "minecraft:iron_axe");
        m.put(259, "minecraft:flint_and_steel");
        m.put(260, "minecraft:apple");
        m.put(261, "minecraft:bow");
        m.put(262, "minecraft:arrow");
        m.put(263, "minecraft:coal");
        m.put(264, "minecraft:diamond");
        m.put(265, "minecraft:iron_ingot");
        m.put(266, "minecraft:gold_ingot");
        m.put(267, "minecraft:iron_sword");
        m.put(268, "minecraft:wooden_sword");
        m.put(269, "minecraft:wooden_shovel");
        m.put(270, "minecraft:wooden_pickaxe");
        m.put(271, "minecraft:wooden_axe");
        m.put(272, "minecraft:stone_sword");
        m.put(273, "minecraft:stone_shovel");
        m.put(274, "minecraft:stone_pickaxe");
        m.put(275, "minecraft:stone_axe");
        m.put(276, "minecraft:diamond_sword");
        m.put(277, "minecraft:diamond_shovel");
        m.put(278, "minecraft:diamond_pickaxe");
        m.put(279, "minecraft:diamond_axe");
        m.put(280, "minecraft:stick");
        m.put(281, "minecraft:bowl");
        m.put(282, "minecraft:mushroom_stew");
        m.put(283, "minecraft:golden_sword");
        m.put(284, "minecraft:golden_shovel");
        m.put(285, "minecraft:golden_pickaxe");
        m.put(286, "minecraft:golden_axe");
        m.put(287, "minecraft:string");
        m.put(288, "minecraft:feather");
        m.put(289, "minecraft:gunpowder");
        m.put(290, "minecraft:wooden_hoe");
        m.put(291, "minecraft:stone_hoe");
        m.put(292, "minecraft:iron_hoe");
        m.put(293, "minecraft:diamond_hoe");
        m.put(294, "minecraft:golden_hoe");
        m.put(295, "minecraft:wheat_seeds");
        m.put(296, "minecraft:wheat");
        m.put(297, "minecraft:bread");
        m.put(298, "minecraft:leather_helmet");
        m.put(299, "minecraft:leather_chestplate");
        m.put(300, "minecraft:leather_leggings");
        m.put(301, "minecraft:leather_boots");
        m.put(302, "minecraft:chainmail_helmet");
        m.put(303, "minecraft:chainmail_chestplate");
        m.put(304, "minecraft:chainmail_leggings");
        m.put(305, "minecraft:chainmail_boots");
        m.put(306, "minecraft:iron_helmet");
        m.put(307, "minecraft:iron_chestplate");
        m.put(308, "minecraft:iron_leggings");
        m.put(309, "minecraft:iron_boots");
        m.put(310, "minecraft:diamond_helmet");
        m.put(311, "minecraft:diamond_chestplate");
        m.put(312, "minecraft:diamond_leggings");
        m.put(313, "minecraft:diamond_boots");
        m.put(314, "minecraft:golden_helmet");
        m.put(315, "minecraft:golden_chestplate");
        m.put(316, "minecraft:golden_leggings");
        m.put(317, "minecraft:golden_boots");
        m.put(318, "minecraft:flint");
        m.put(319, "minecraft:porkchop");
        m.put(320, "minecraft:cooked_porkchop");
        m.put(321, "minecraft:painting");
        m.put(322, "minecraft:golden_apple");
        m.put(323, "minecraft:oak_sign");
        m.put(324, "minecraft:oak_door");
        m.put(325, "minecraft:bucket");
        m.put(326, "minecraft:water_bucket");
        m.put(327, "minecraft:lava_bucket");
        m.put(328, "minecraft:minecart");
        m.put(329, "minecraft:saddle");
        m.put(330, "minecraft:iron_door");
        m.put(331, "minecraft:redstone");
        m.put(332, "minecraft:snowball");
        m.put(333, "minecraft:oak_boat");
        m.put(334, "minecraft:leather");
        m.put(335, "minecraft:milk_bucket");
        m.put(336, "minecraft:brick");
        m.put(337, "minecraft:clay_ball");
        m.put(338, "minecraft:sugar_cane");
        m.put(339, "minecraft:paper");
        m.put(340, "minecraft:book");
        m.put(341, "minecraft:slime_ball");
        m.put(342, "minecraft:chest_minecart");
        m.put(343, "minecraft:furnace_minecart");
        m.put(344, "minecraft:egg");
        m.put(345, "minecraft:compass");
        m.put(346, "minecraft:fishing_rod");
        m.put(347, "minecraft:clock");
        m.put(348, "minecraft:glowstone_dust");
        m.put(349, "minecraft:cod");
        m.put(350, "minecraft:cooked_cod");
        m.put(351, "minecraft:dye");
        m.put(352, "minecraft:bone");
        m.put(353, "minecraft:sugar");
        m.put(354, "minecraft:cake");
        m.put(355, "minecraft:red_bed");
        m.put(356, "minecraft:repeater");
        m.put(357, "minecraft:cookie");
        m.put(358, "minecraft:map");
        m.put(359, "minecraft:shears");
        m.put(2256, "minecraft:music_disc_13");
        m.put(2257, "minecraft:music_disc_cat");
    });

    public Schema69420(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        super.registerTypes(schema, entityTypes, blockEntityTypes);
        schema.registerType(
                false,
                TypeReferences.CHUNK,
                () -> DSL.fields(
                        "Level",
                        DSL.optionalFields(
                                "Entities",
                                DSL.list(TypeReferences.ENTITY.in(schema)),
                                "TileEntities",
                                DSL.list(TypeReferences.BLOCK_ENTITY.in(schema)),
                                SECTIONS,
                                DSL.list(DSL.optionalFields("palette", DSL.list(TypeReferences.BLOCK_STATE.in(schema))))
                        )
                )
        );
        schema.registerType(
                true,
                TypeReferences.ITEM_STACK,
                () -> DSL.fields(
                        STATION_ID,
                        TypeReferences.ITEM_NAME.in(schema)
                )
        );
        schema.registerType(
                false,
                TypeReferences.ITEM_NAME,
                () -> DSL.constType(IdentifierNormalizingSchema.getIdentifierType())
        );
        schema.registerType(
                false,
                TypeReferences.BLOCK_STATE,
                DSL::remainder
        );
    }
}
