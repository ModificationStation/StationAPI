// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            Achievement, Item, Block

public class AchievementList
{

    public AchievementList()
    {
    }

    public static void func_27097_a()
    {
    }

    public static int field_27114_a;
    public static int field_27113_b;
    public static int field_27112_c;
    public static int field_27111_d;
    public static List field_25129_a;
    public static Achievement field_25128_b;
    public static Achievement field_25131_c;
    public static Achievement field_25130_d;
    public static Achievement field_27110_i;
    public static Achievement field_27109_j;
    public static Achievement field_27108_k;
    public static Achievement field_27107_l;
    public static Achievement field_27106_m;
    public static Achievement field_27105_n;
    public static Achievement field_27104_o;
    public static Achievement field_27103_p;
    public static Achievement field_27102_q;
    public static Achievement field_27101_r;
    public static Achievement field_27100_s;
    public static Achievement field_27099_t;
    public static Achievement field_27098_u;

    static 
    {
        field_25129_a = new ArrayList();
        field_25128_b = (new Achievement(0, "openInventory", 0, 0, Item.book, null)).func_27059_a().func_27061_c();
        field_25131_c = (new Achievement(1, "mineWood", 2, 1, Block.wood, field_25128_b)).func_27061_c();
        field_25130_d = (new Achievement(2, "buildWorkBench", 4, -1, Block.workbench, field_25131_c)).func_27061_c();
        field_27110_i = (new Achievement(3, "buildPickaxe", 4, 2, Item.pickaxeWood, field_25130_d)).func_27061_c();
        field_27109_j = (new Achievement(4, "buildFurnace", 3, 4, Block.stoneOvenActive, field_27110_i)).func_27061_c();
        field_27108_k = (new Achievement(5, "acquireIron", 1, 4, Item.ingotIron, field_27109_j)).func_27061_c();
        field_27107_l = (new Achievement(6, "buildHoe", 2, -3, Item.hoeWood, field_25130_d)).func_27061_c();
        field_27106_m = (new Achievement(7, "makeBread", -1, -3, Item.bread, field_27107_l)).func_27061_c();
        field_27105_n = (new Achievement(8, "bakeCake", 0, -5, Item.cake, field_27107_l)).func_27061_c();
        field_27104_o = (new Achievement(9, "buildBetterPickaxe", 6, 2, Item.pickaxeStone, field_27110_i)).func_27061_c();
        field_27103_p = (new Achievement(10, "cookFish", 2, 6, Item.fishCooked, field_27109_j)).func_27061_c();
        field_27102_q = (new Achievement(11, "onARail", 2, 3, Block.minecartTrack, field_27108_k)).func_27060_b().func_27061_c();
        field_27101_r = (new Achievement(12, "buildSword", 6, -1, Item.swordWood, field_25130_d)).func_27061_c();
        field_27100_s = (new Achievement(13, "killEnemy", 8, -1, Item.bone, field_27101_r)).func_27061_c();
        field_27099_t = (new Achievement(14, "killCow", 7, -3, Item.leather, field_27101_r)).func_27061_c();
        field_27098_u = (new Achievement(15, "flyPig", 8, -4, Item.saddle, field_27099_t)).func_27060_b().func_27061_c();
        System.out.println((new StringBuilder()).append(field_25129_a.size()).append(" achievements").toString());
    }
}
