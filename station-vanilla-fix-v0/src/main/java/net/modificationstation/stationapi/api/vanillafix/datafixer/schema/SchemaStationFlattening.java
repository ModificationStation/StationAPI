package net.modificationstation.stationapi.api.vanillafix.datafixer.schema;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.datafixer.TypeReferences;
import net.modificationstation.stationapi.api.nbt.NbtOps;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.impl.level.StationFlatteningWorldManager.SECTIONS;
import static net.modificationstation.stationapi.impl.vanillafix.datafixer.VanillaDataFixerImpl.STATION_ID;

public class SchemaStationFlattening extends Schema {

    private static final Dynamic<?>[] OLD_ID_TO_BLOCKSTATE = new Dynamic[256];
    private static final Object2IntOpenHashMap<String> BLOCK_TO_OLD_ID = Util.make(new Object2IntOpenHashMap<>(256), map -> map.defaultReturnValue(0));
    private static final String[] OLD_ID_TO_ITEM = new String[32000];
    private static final Object2IntOpenHashMap<String> ITEM_TO_OLD_ID = Util.make(new Object2IntOpenHashMap<>(512), map -> map.defaultReturnValue(0));

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
        String id = tag.getString("Name");
        BLOCK_TO_OLD_ID.put(id, oldId);
        Dynamic<?> dynamic = new Dynamic<>(NbtOps.INSTANCE, tag);
        OLD_ID_TO_BLOCKSTATE[oldId] = dynamic;
        putItem(oldId, id);
    }

    public static Dynamic<?> lookupState(int stateId) {
        Dynamic<?> dynamic = null;
        if (stateId >= 0 && stateId < OLD_ID_TO_BLOCKSTATE.length) {
            dynamic = OLD_ID_TO_BLOCKSTATE[stateId];
        }
        return dynamic == null ? OLD_ID_TO_BLOCKSTATE[0] : dynamic;
    }

    public static String lookupBlockId(int id) {
        if (id < 0 || id >= OLD_ID_TO_BLOCKSTATE.length) {
            return "minecraft:air";
        }
        Dynamic<?> dynamic = OLD_ID_TO_BLOCKSTATE[id];
        return dynamic == null ? "minecraft:air" : dynamic.get("Name").asString("");
    }

    public static <T> int lookupOldBlockId(Dynamic<T> dynamic) {
        return BLOCK_TO_OLD_ID.getInt(dynamic.get("Name").asString(""));
    }

    public static void putItem(int oldId, String id) {
        OLD_ID_TO_ITEM[oldId] = id;
        ITEM_TO_OLD_ID.put(id, oldId);
    }

    public static String lookupItem(int oldId) {
        if (oldId < 0 || oldId >= OLD_ID_TO_ITEM.length)
            return "minecraft:air";
        String item = OLD_ID_TO_ITEM[oldId];
        return item == null ? "minecraft:air" : item;
    }

    public static int lookupOldItemId(String id) {
        return ITEM_TO_OLD_ID.getInt(id);
    }

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
        putItem(256, "minecraft:iron_shovel");
        putItem(257, "minecraft:iron_pickaxe");
        putItem(258, "minecraft:iron_axe");
        putItem(259, "minecraft:flint_and_steel");
        putItem(260, "minecraft:apple");
        putItem(261, "minecraft:bow");
        putItem(262, "minecraft:arrow");
        putItem(263, "minecraft:coal");
        putItem(264, "minecraft:diamond");
        putItem(265, "minecraft:iron_ingot");
        putItem(266, "minecraft:gold_ingot");
        putItem(267, "minecraft:iron_sword");
        putItem(268, "minecraft:wooden_sword");
        putItem(269, "minecraft:wooden_shovel");
        putItem(270, "minecraft:wooden_pickaxe");
        putItem(271, "minecraft:wooden_axe");
        putItem(272, "minecraft:stone_sword");
        putItem(273, "minecraft:stone_shovel");
        putItem(274, "minecraft:stone_pickaxe");
        putItem(275, "minecraft:stone_axe");
        putItem(276, "minecraft:diamond_sword");
        putItem(277, "minecraft:diamond_shovel");
        putItem(278, "minecraft:diamond_pickaxe");
        putItem(279, "minecraft:diamond_axe");
        putItem(280, "minecraft:stick");
        putItem(281, "minecraft:bowl");
        putItem(282, "minecraft:mushroom_stew");
        putItem(283, "minecraft:golden_sword");
        putItem(284, "minecraft:golden_shovel");
        putItem(285, "minecraft:golden_pickaxe");
        putItem(286, "minecraft:golden_axe");
        putItem(287, "minecraft:string");
        putItem(288, "minecraft:feather");
        putItem(289, "minecraft:gunpowder");
        putItem(290, "minecraft:wooden_hoe");
        putItem(291, "minecraft:stone_hoe");
        putItem(292, "minecraft:iron_hoe");
        putItem(293, "minecraft:diamond_hoe");
        putItem(294, "minecraft:golden_hoe");
        putItem(295, "minecraft:wheat_seeds");
        putItem(296, "minecraft:wheat");
        putItem(297, "minecraft:bread");
        putItem(298, "minecraft:leather_helmet");
        putItem(299, "minecraft:leather_chestplate");
        putItem(300, "minecraft:leather_leggings");
        putItem(301, "minecraft:leather_boots");
        putItem(302, "minecraft:chainmail_helmet");
        putItem(303, "minecraft:chainmail_chestplate");
        putItem(304, "minecraft:chainmail_leggings");
        putItem(305, "minecraft:chainmail_boots");
        putItem(306, "minecraft:iron_helmet");
        putItem(307, "minecraft:iron_chestplate");
        putItem(308, "minecraft:iron_leggings");
        putItem(309, "minecraft:iron_boots");
        putItem(310, "minecraft:diamond_helmet");
        putItem(311, "minecraft:diamond_chestplate");
        putItem(312, "minecraft:diamond_leggings");
        putItem(313, "minecraft:diamond_boots");
        putItem(314, "minecraft:golden_helmet");
        putItem(315, "minecraft:golden_chestplate");
        putItem(316, "minecraft:golden_leggings");
        putItem(317, "minecraft:golden_boots");
        putItem(318, "minecraft:flint");
        putItem(319, "minecraft:porkchop");
        putItem(320, "minecraft:cooked_porkchop");
        putItem(321, "minecraft:painting");
        putItem(322, "minecraft:golden_apple");
        putItem(323, "minecraft:oak_sign");
        putItem(324, "minecraft:oak_door");
        putItem(325, "minecraft:bucket");
        putItem(326, "minecraft:water_bucket");
        putItem(327, "minecraft:lava_bucket");
        putItem(328, "minecraft:minecart");
        putItem(329, "minecraft:saddle");
        putItem(330, "minecraft:iron_door");
        putItem(331, "minecraft:redstone");
        putItem(332, "minecraft:snowball");
        putItem(333, "minecraft:oak_boat");
        putItem(334, "minecraft:leather");
        putItem(335, "minecraft:milk_bucket");
        putItem(336, "minecraft:brick");
        putItem(337, "minecraft:clay_ball");
        putItem(338, "minecraft:sugar_cane");
        putItem(339, "minecraft:paper");
        putItem(340, "minecraft:book");
        putItem(341, "minecraft:slime_ball");
        putItem(342, "minecraft:chest_minecart");
        putItem(343, "minecraft:furnace_minecart");
        putItem(344, "minecraft:egg");
        putItem(345, "minecraft:compass");
        putItem(346, "minecraft:fishing_rod");
        putItem(347, "minecraft:clock");
        putItem(348, "minecraft:glowstone_dust");
        putItem(349, "minecraft:cod");
        putItem(350, "minecraft:cooked_cod");
        putItem(351, "minecraft:dye");
        putItem(352, "minecraft:bone");
        putItem(353, "minecraft:sugar");
        putItem(354, "minecraft:cake");
        putItem(355, "minecraft:red_bed");
        putItem(356, "minecraft:repeater");
        putItem(357, "minecraft:cookie");
        putItem(358, "minecraft:map");
        putItem(359, "minecraft:shears");
        putItem(2256, "minecraft:music_disc_13");
        putItem(2257, "minecraft:music_disc_cat");
    }

    public SchemaStationFlattening(int versionKey, Schema parent) {
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
                                DSL.list(DSL.optionalFields("block_states", DSL.optionalFields("palette", DSL.list(TypeReferences.BLOCK_STATE.in(schema)))))
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
