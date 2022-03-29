package net.modificationstation.stationapi.impl.client.texture;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.modificationstation.stationapi.api.client.texture.SpritesheetHelper;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import uk.co.benjiweber.expressions.tuple.BiTuple;

import java.util.function.ObjIntConsumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GuiItemsHelper implements SpritesheetHelper {

    static final GuiItemsHelper INSTANCE = new GuiItemsHelper();

    private static final Int2ObjectMap<Identifier> GENERATED_IDS = Util.make(new Int2ObjectOpenHashMap<>(), map -> {
        ObjIntConsumer<String> f = (id, index) -> map.put(index, Identifier.of("item/" + id));
        f.accept("leather_helmet", 0);
        f.accept("chainmail_helmet", 1);
        f.accept("iron_helmet", 2);
        f.accept("diamond_helmet", 3);
        f.accept("golden_helmet", 4);
        f.accept("flint_and_steel", 5);
        f.accept("flint", 6);
        f.accept("coal", 7);
        f.accept("string", 8);
        f.accept("wheat_seeds", 9);
        f.accept("apple", 10);
        f.accept("golden_apple", 11);
        f.accept("egg", 12);
        f.accept("sugar", 13);
        f.accept("snowball", 14);
        f.accept("leather_chestplate", 16);
        f.accept("chainmail_chestplate", 17);
        f.accept("iron_chestplate", 18);
        f.accept("diamond_chestplate", 19);
        f.accept("golden_chestplate", 20);
        f.accept("bow", 21);
        f.accept("brick", 22);
        f.accept("iron_ingot", 23);
        f.accept("feather", 24);
        f.accept("wheat", 25);
        f.accept("painting", 26);
        f.accept("sugar_cane", 27);
        f.accept("bone", 28);
        f.accept("cake", 29);
        f.accept("slime_ball", 30);
        f.accept("leather_leggings", 32);
        f.accept("chainmail_leggings", 33);
        f.accept("iron_leggings", 34);
        f.accept("diamond_leggings", 35);
        f.accept("golden_leggings", 36);
        f.accept("arrow", 37);
        f.accept("quiver", 38);
        f.accept("gold_ingot", 39);
        f.accept("gunpowder", 40);
        f.accept("bread", 41);
        f.accept("oak_sign", 42);
        f.accept("oak_door", 43);
        f.accept("iron_door", 44);
        f.accept("bed", 45);
        f.accept("leather_boots", 48);
        f.accept("chainmail_boots", 49);
        f.accept("iron_boots", 50);
        f.accept("diamond_boots", 51);
        f.accept("golden_boots", 52);
        f.accept("stick", 53);
        f.accept("compass", 54);
        f.accept("diamond", 55);
        f.accept("redstone", 56);
        f.accept("clay_ball", 57);
        f.accept("paper", 58);
        f.accept("book", 59);
        f.accept("filled_map", 60);
        f.accept("wooden_sword", 64);
        f.accept("stone_sword", 65);
        f.accept("iron_sword", 66);
        f.accept("diamond_sword", 67);
        f.accept("golden_sword", 68);
        f.accept("fishing_rod", 69);
        f.accept("clock", 70);
        f.accept("bowl", 71);
        f.accept("mushroom_stew", 72);
        f.accept("glowstone_dust", 73);
        f.accept("bucket", 74);
        f.accept("water_bucket", 75);
        f.accept("lava_bucket", 76);
        f.accept("milk_bucket", 77);
        f.accept("ink_sac", 78);
        f.accept("gray_dye", 79);
        f.accept("wooden_shovel", 80);
        f.accept("stone_shovel", 81);
        f.accept("iron_shovel", 82);
        f.accept("diamond_shovel", 83);
        f.accept("golden_shovel", 84);
        f.accept("fishing_rod_cast", 85);
        f.accept("repeater", 86);
        f.accept("porkchop", 87);
        f.accept("cooked_porkchop", 88);
        f.accept("cod", 89);
        f.accept("cooked_cod", 90);
        f.accept("cookie", 92);
        f.accept("shears", 93);
        f.accept("red_dye", 94);
        f.accept("pink_dye", 95);
        f.accept("wooden_pickaxe", 96);
        f.accept("stone_pickaxe", 97);
        f.accept("iron_pickaxe", 98);
        f.accept("diamond_pickaxe", 99);
        f.accept("golden_pickaxe", 100);
        f.accept("leather", 103);
        f.accept("saddle", 104);
        f.accept("green_dye", 110);
        f.accept("lime_dye", 111);
        f.accept("wooden_axe", 112);
        f.accept("stone_axe", 113);
        f.accept("iron_axe", 114);
        f.accept("diamond_axe", 115);
        f.accept("golden_axe", 116);
        f.accept("cocoa_beans", 126);
        f.accept("yellow_dye", 127);
        f.accept("wooden_hoe", 128);
        f.accept("stone_hoe", 129);
        f.accept("iron_hoe", 130);
        f.accept("diamond_hoe", 131);
        f.accept("golden_hoe", 132);
        f.accept("minecart", 135);
        f.accept("oak_boat", 136);
        f.accept("lapis_lazuli", 142);
        f.accept("light_blue_dye", 143);
        f.accept("chest_minecart", 151);
        f.accept("purple_dye", 158);
        f.accept("magenta_dye", 159);
        f.accept("furnace_minecart", 167);
        f.accept("cyan_dye", 174);
        f.accept("orange_dye", 175);
        f.accept("light_gray_dye", 190);
        f.accept("bone_meal", 191);
        f.accept("music_disc_13", 240);
        f.accept("music_disc_cat", 241);
    });

    @Override
    public Identifier generateIdentifier(int textureIndex) {
        return GENERATED_IDS.get(textureIndex);
    }

    @Override
    public BiTuple<Integer, Integer> getResolutionMultiplier(int textureIndex) {
        return DEFAULT_RESOLUTION_MULTIPLIER;
    }
}
