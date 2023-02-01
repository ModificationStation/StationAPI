package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager.SECTIONS;
import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class Schema69420 extends Schema {

    private static final Dynamic<?>[] OLD_STATE_TO_DYNAMIC = new Dynamic[256];

    public static void putState(int oldId, String id, CompoundTag properties) {
        putState(oldId, Util.make(new CompoundTag(), tag -> {
            tag.put("Name", id);
            tag.put("Properties", properties);
        }));
    }

    public static void putState(int oldId, String id) {
        putState(oldId, Util.make(new CompoundTag(), tag -> tag.put("Name", id)));
    }

    public static void putState(int oldId, CompoundTag tag) {
        ITEM_RENAMES.put(oldId, tag.getString("Name"));
        Dynamic<?> dynamic = parseState(tag);
        OLD_STATE_TO_DYNAMIC[oldId] = dynamic;
    }

    public static String lookupStateBlock(int stateId) {
        if (stateId < 0 || stateId >= OLD_STATE_TO_DYNAMIC.length) {
            return "minecraft:air";
        }
        Dynamic<?> dynamic = OLD_STATE_TO_DYNAMIC[stateId];
        return dynamic == null ? "minecraft:air" : dynamic.get("Name").asString("");
    }

    public static Dynamic<?> parseState(CompoundTag tag) {
        return new Dynamic<>(NbtOps.INSTANCE, tag);
    }

    public static Dynamic<?> lookupState(int stateId) {
        Dynamic<?> dynamic = null;
        if (stateId >= 0 && stateId < OLD_STATE_TO_DYNAMIC.length) {
            dynamic = OLD_STATE_TO_DYNAMIC[stateId];
        }
        return dynamic == null ? OLD_STATE_TO_DYNAMIC[0] : dynamic;
    }

    public static final Int2ObjectMap<String> ITEM_RENAMES = Util.make(new Int2ObjectOpenHashMap<>(), m -> {
        m.defaultReturnValue("minecraft:air");
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

    static {
        putState(0, "minecraft:air");
        putState(1, "minecraft:stone");
        putState(2, "minecraft:grass_block");
        putState(3, "minecraft:dirt");
        putState(4, "minecraft:cobblestone");
        putState(5, "minecraft:oak_planks");
        putState(6, "minecraft:sapling");
        putState(7, "minecraft:bedrock");
        putState(8, "minecraft:flowing_water");
        putState(9, "minecraft:water");
        putState(10, "minecraft:flowing_lava");
        putState(11, "minecraft:lava");
        putState(12, "minecraft:sand");
        putState(13, "minecraft:gravel");
        putState(14, "minecraft:gold_ore");
        putState(15, "minecraft:iron_ore");
        putState(16, "minecraft:coal_ore");
        putState(17, "minecraft:log");
        putState(18, "minecraft:leaves");
        putState(19, "minecraft:sponge");
        putState(20, "minecraft:glass");
        putState(21, "minecraft:lapis_ore");
        putState(22, "minecraft:lapis_block");
        putState(23, "minecraft:dispenser");
        putState(24, "minecraft:sandstone");
        putState(25, "minecraft:note_block");
        putState(26, "minecraft:red_bed");
        putState(27, "minecraft:powered_rail");
        putState(28, "minecraft:detector_rail");
        putState(29, "minecraft:sticky_piston");
        putState(30, "minecraft:cobweb");
        putState(31, "minecraft:grass");
        putState(32, "minecraft:dead_bush");
        putState(33, "minecraft:piston");
        putState(34, "minecraft:piston_head");
        putState(35, "minecraft:wool");
        putState(36, "minecraft:moving_piston");
        putState(37, "minecraft:dandelion");
        putState(38, "minecraft:rose");
        putState(39, "minecraft:brown_mushroom");
        putState(40, "minecraft:red_mushroom");
        putState(41, "minecraft:gold_block");
        putState(42, "minecraft:iron_block");
        putState(43, "minecraft:double_slab");
        putState(44, "minecraft:slab");
        putState(45, "minecraft:bricks");
        putState(46, "minecraft:tnt");
        putState(47, "minecraft:bookshelf");
        putState(48, "minecraft:mossy_cobblestone");
        putState(49, "minecraft:obsidian");
        putState(50, "minecraft:torch");
        putState(51, "minecraft:fire");
        putState(52, "minecraft:spawner");
        putState(53, "minecraft:oak_stairs");
        putState(54, "minecraft:chest");
        putState(55, "minecraft:redstone_wire");
        putState(56, "minecraft:diamond_ore");
        putState(57, "minecraft:diamond_block");
        putState(58, "minecraft:crafting_table");
        putState(59, "minecraft:wheat");
        putState(60, "minecraft:farmland");
        putState(61, "minecraft:furnace");
        putState(62, "minecraft:furnace_lit");
        putState(63, "minecraft:oak_sign");
        putState(64, "minecraft:oak_door");
        putState(65, "minecraft:ladder");
        putState(66, "minecraft:rail");
        putState(67, "minecraft:cobblestone_stairs");
        putState(68, "minecraft:oak_wall_sign");
        putState(69, "minecraft:lever");
        putState(70, "minecraft:oak_pressure_plate");
        putState(71, "minecraft:iron_door");
        putState(72, "minecraft:stone_pressure_plate");
        putState(73, "minecraft:redstone_ore");
        putState(74, "minecraft:redstone_ore_lit");
        putState(75, "minecraft:redstone_torch");
        putState(76, "minecraft:redstone_torch_lit");
        putState(77, "minecraft:stone_button");
        putState(78, "minecraft:snow");
        putState(79, "minecraft:ice");
        putState(80, "minecraft:snow_block");
        putState(81, "minecraft:cactus");
        putState(82, "minecraft:clay");
        putState(83, "minecraft:sugar_cane");
        putState(84, "minecraft:jukebox");
        putState(85, "minecraft:oak_fence");
        putState(86, "minecraft:carved_pumpkin");
        putState(87, "minecraft:netherrack");
        putState(88, "minecraft:soul_sand");
        putState(89, "minecraft:glowstone");
        putState(90, "minecraft:nether_portal");
        putState(91, "minecraft:jack_o_lantern");
        putState(92, "minecraft:cake");
        putState(93, "minecraft:repeater");
        putState(94, "minecraft:repeater_lit");
        putState(95, "minecraft:locked_chest");
        putState(96, "minecraft:oak_trapdoor");
    }

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
