// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            Block, CraftingManager, IRecipe, ItemStack, 
//            FurnaceRecipes, StatBase, Item, StatCollector, 
//            StatCrafting, BlockFlower, BlockGrass, StatBasic, 
//            AchievementList

public class StatList
{

    public StatList()
    {
    }

    public static void func_27092_a()
    {
    }

    public static void func_25088_a()
    {
        field_25107_A = func_25090_a(field_25107_A, "stat.useItem", 0x1020000, 0, Block.blocksList.length);
        field_25105_B = func_25087_b(field_25105_B, "stat.breakItem", 0x1030000, 0, Block.blocksList.length);
        field_25101_D = true;
        func_25091_c();
    }

    public static void func_25086_b()
    {
        field_25107_A = func_25090_a(field_25107_A, "stat.useItem", 0x1020000, Block.blocksList.length, 32000);
        field_25105_B = func_25087_b(field_25105_B, "stat.breakItem", 0x1030000, Block.blocksList.length, 32000);
        field_25099_E = true;
        func_25091_c();
    }

    public static void func_25091_c()
    {
        if(!field_25101_D || !field_25099_E)
        {
            return;
        }
        HashSet hashset = new HashSet();
        IRecipe irecipe;
        for(Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext(); hashset.add(Integer.valueOf(irecipe.func_25077_b().itemID)))
        {
            irecipe = (IRecipe)iterator.next();
        }

        ItemStack itemstack;
        for(Iterator iterator1 = FurnaceRecipes.smelting().getSmeltingList().values().iterator(); iterator1.hasNext(); hashset.add(Integer.valueOf(itemstack.itemID)))
        {
            itemstack = (ItemStack)iterator1.next();
        }

        field_25093_z = new StatBase[32000];
        Iterator iterator2 = hashset.iterator();
        do
        {
            if(!iterator2.hasNext())
            {
                break;
            }
            Integer integer = (Integer)iterator2.next();
            if(Item.itemsList[integer.intValue()] != null)
            {
                String s = StatCollector.translateToLocalFormatted("stat.craftItem", new Object[] {
                    Item.itemsList[integer.intValue()].func_25006_i()
                });
                field_25093_z[integer.intValue()] = (new StatCrafting(0x1010000 + integer.intValue(), s, integer.intValue())).func_27053_d();
            }
        } while(true);
        replaceAllSimilarBlocks(field_25093_z);
    }

    private static StatBase[] func_25089_a(String s, int i)
    {
        StatBase astatbase[] = new StatBase[256];
        for(int j = 0; j < 256; j++)
        {
            if(Block.blocksList[j] != null && Block.blocksList[j].getEnableStats())
            {
                String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
                    Block.blocksList[j].getNameLocalizedForStats()
                });
                astatbase[j] = (new StatCrafting(i + j, s1, j)).func_27053_d();
                field_25120_d.add((StatCrafting)astatbase[j]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static StatBase[] func_25090_a(StatBase astatbase[], String s, int i, int j, int k)
    {
        if(astatbase == null)
        {
            astatbase = new StatBase[32000];
        }
        for(int l = j; l < k; l++)
        {
            if(Item.itemsList[l] == null)
            {
                continue;
            }
            String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
                Item.itemsList[l].func_25006_i()
            });
            astatbase[l] = (new StatCrafting(i + l, s1, l)).func_27053_d();
            if(l >= Block.blocksList.length)
            {
                field_25121_c.add((StatCrafting)astatbase[l]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static StatBase[] func_25087_b(StatBase astatbase[], String s, int i, int j, int k)
    {
        if(astatbase == null)
        {
            astatbase = new StatBase[32000];
        }
        for(int l = j; l < k; l++)
        {
            if(Item.itemsList[l] != null && Item.itemsList[l].func_25005_e())
            {
                String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
                    Item.itemsList[l].func_25006_i()
                });
                astatbase[l] = (new StatCrafting(i + l, s1, l)).func_27053_d();
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static void replaceAllSimilarBlocks(StatBase astatbase[])
    {
        replaceSimilarBlocks(astatbase, Block.waterStill.blockID, Block.waterMoving.blockID);
        replaceSimilarBlocks(astatbase, Block.lavaStill.blockID, Block.lavaStill.blockID);
        replaceSimilarBlocks(astatbase, Block.pumpkinLantern.blockID, Block.pumpkin.blockID);
        replaceSimilarBlocks(astatbase, Block.stoneOvenActive.blockID, Block.stoneOvenIdle.blockID);
        replaceSimilarBlocks(astatbase, Block.oreRedstoneGlowing.blockID, Block.oreRedstone.blockID);
        replaceSimilarBlocks(astatbase, Block.redstoneRepeaterActive.blockID, Block.redstoneRepeaterIdle.blockID);
        replaceSimilarBlocks(astatbase, Block.torchRedstoneActive.blockID, Block.torchRedstoneIdle.blockID);
        replaceSimilarBlocks(astatbase, Block.mushroomRed.blockID, Block.mushroomBrown.blockID);
        replaceSimilarBlocks(astatbase, Block.stairDouble.blockID, Block.stairSingle.blockID);
        replaceSimilarBlocks(astatbase, Block.grass.blockID, Block.dirt.blockID);
        replaceSimilarBlocks(astatbase, Block.tilledField.blockID, Block.dirt.blockID);
    }

    private static void replaceSimilarBlocks(StatBase astatbase[], int i, int j)
    {
        if(astatbase[i] != null && astatbase[j] == null)
        {
            astatbase[j] = astatbase[i];
            return;
        } else
        {
            field_25123_a.remove(astatbase[i]);
            field_25120_d.remove(astatbase[i]);
            field_25122_b.remove(astatbase[i]);
            astatbase[i] = astatbase[j];
            return;
        }
    }

    protected static Map field_25104_C = new HashMap();
    public static List field_25123_a = new ArrayList();
    public static List field_25122_b = new ArrayList();
    public static List field_25121_c = new ArrayList();
    public static List field_25120_d = new ArrayList();
    public static StatBase field_25119_e = (new StatBasic(1000, StatCollector.translateToLocal("stat.startGame"))).func_27052_e().func_27053_d();
    public static StatBase field_25118_f = (new StatBasic(1001, StatCollector.translateToLocal("stat.createWorld"))).func_27052_e().func_27053_d();
    public static StatBase field_25117_g = (new StatBasic(1002, StatCollector.translateToLocal("stat.loadWorld"))).func_27052_e().func_27053_d();
    public static StatBase field_25116_h = (new StatBasic(1003, StatCollector.translateToLocal("stat.joinMultiplayer"))).func_27052_e().func_27053_d();
    public static StatBase field_25115_i = (new StatBasic(1004, StatCollector.translateToLocal("stat.leaveGame"))).func_27052_e().func_27053_d();
    public static StatBase field_25114_j;
    public static StatBase field_25113_k;
    public static StatBase field_25112_l;
    public static StatBase field_25111_m;
    public static StatBase field_25110_n;
    public static StatBase field_25109_o;
    public static StatBase field_25108_p;
    public static StatBase field_27095_r;
    public static StatBase field_27094_s;
    public static StatBase field_27093_t;
    public static StatBase field_25106_q = (new StatBasic(2010, StatCollector.translateToLocal("stat.jump"))).func_27052_e().func_27053_d();
    public static StatBase field_25103_r = (new StatBasic(2011, StatCollector.translateToLocal("stat.drop"))).func_27052_e().func_27053_d();
    public static StatBase field_25102_s = (new StatBasic(2020, StatCollector.translateToLocal("stat.damageDealt"))).func_27053_d();
    public static StatBase field_25100_t = (new StatBasic(2021, StatCollector.translateToLocal("stat.damageTaken"))).func_27053_d();
    public static StatBase field_25098_u = (new StatBasic(2022, StatCollector.translateToLocal("stat.deaths"))).func_27053_d();
    public static StatBase field_25097_v = (new StatBasic(2023, StatCollector.translateToLocal("stat.mobKills"))).func_27053_d();
    public static StatBase field_25096_w = (new StatBasic(2024, StatCollector.translateToLocal("stat.playerKills"))).func_27053_d();
    public static StatBase fishCaughtStat = (new StatBasic(2025, StatCollector.translateToLocal("stat.fishCaught"))).func_27053_d();
    public static StatBase mineBlockStatArray[] = func_25089_a("stat.mineBlock", 0x1000000);
    public static StatBase field_25093_z[];
    public static StatBase field_25107_A[];
    public static StatBase field_25105_B[];
    private static boolean field_25101_D = false;
    private static boolean field_25099_E = false;

    static 
    {
        field_25114_j = (new StatBasic(1100, StatCollector.translateToLocal("stat.playOneMinute"), StatBase.field_27055_j)).func_27052_e().func_27053_d();
        field_25113_k = (new StatBasic(2000, StatCollector.translateToLocal("stat.walkOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_25112_l = (new StatBasic(2001, StatCollector.translateToLocal("stat.swimOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_25111_m = (new StatBasic(2002, StatCollector.translateToLocal("stat.fallOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_25110_n = (new StatBasic(2003, StatCollector.translateToLocal("stat.climbOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_25109_o = (new StatBasic(2004, StatCollector.translateToLocal("stat.flyOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_25108_p = (new StatBasic(2005, StatCollector.translateToLocal("stat.diveOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_27095_r = (new StatBasic(2006, StatCollector.translateToLocal("stat.minecartOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_27094_s = (new StatBasic(2007, StatCollector.translateToLocal("stat.boatOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        field_27093_t = (new StatBasic(2008, StatCollector.translateToLocal("stat.pigOneCm"), StatBase.field_27054_k)).func_27052_e().func_27053_d();
        AchievementList.func_27097_a();
    }
}
