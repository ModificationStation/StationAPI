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

    public static void func_27360_a()
    {
    }

    public static void func_25154_a()
    {
        field_25172_A = func_25155_a(field_25172_A, "stat.useItem", 0x1020000, 0, Block.blocksList.length);
        field_25170_B = func_25149_b(field_25170_B, "stat.breakItem", 0x1030000, 0, Block.blocksList.length);
        field_25166_D = true;
        func_25157_c();
    }

    public static void func_25151_b()
    {
        field_25172_A = func_25155_a(field_25172_A, "stat.useItem", 0x1020000, Block.blocksList.length, 32000);
        field_25170_B = func_25149_b(field_25170_B, "stat.breakItem", 0x1030000, Block.blocksList.length, 32000);
        field_25164_E = true;
        func_25157_c();
    }

    public static void func_25157_c()
    {
        if(!field_25166_D || !field_25164_E)
        {
            return;
        }
        HashSet hashset = new HashSet();
        IRecipe irecipe;
        for(Iterator iterator = CraftingManager.getInstance().getRecipeList().iterator(); iterator.hasNext(); hashset.add(Integer.valueOf(irecipe.getRecipeOutput().itemID)))
        {
            irecipe = (IRecipe)iterator.next();
        }

        ItemStack itemstack;
        for(Iterator iterator1 = FurnaceRecipes.smelting().getSmeltingList().values().iterator(); iterator1.hasNext(); hashset.add(Integer.valueOf(itemstack.itemID)))
        {
            itemstack = (ItemStack)iterator1.next();
        }

        field_25158_z = new StatBase[32000];
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
                    Item.itemsList[integer.intValue()].getStatName()
                });
                field_25158_z[integer.intValue()] = (new StatCrafting(0x1010000 + integer.intValue(), s, integer.intValue())).registerStat();
            }
        } while(true);
        replaceAllSimilarBlocks(field_25158_z);
    }

    private static StatBase[] func_25153_a(String s, int i)
    {
        StatBase astatbase[] = new StatBase[256];
        for(int j = 0; j < 256; j++)
        {
            if(Block.blocksList[j] != null && Block.blocksList[j].getEnableStats())
            {
                String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
                    Block.blocksList[j].translateBlockName()
                });
                astatbase[j] = (new StatCrafting(i + j, s1, j)).registerStat();
                field_25185_d.add((StatCrafting)astatbase[j]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static StatBase[] func_25155_a(StatBase astatbase[], String s, int i, int j, int k)
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
                Item.itemsList[l].getStatName()
            });
            astatbase[l] = (new StatCrafting(i + l, s1, l)).registerStat();
            if(l >= Block.blocksList.length)
            {
                field_25186_c.add((StatCrafting)astatbase[l]);
            }
        }

        replaceAllSimilarBlocks(astatbase);
        return astatbase;
    }

    private static StatBase[] func_25149_b(StatBase astatbase[], String s, int i, int j, int k)
    {
        if(astatbase == null)
        {
            astatbase = new StatBase[32000];
        }
        for(int l = j; l < k; l++)
        {
            if(Item.itemsList[l] != null && Item.itemsList[l].isDamagable())
            {
                String s1 = StatCollector.translateToLocalFormatted(s, new Object[] {
                    Item.itemsList[l].getStatName()
                });
                astatbase[l] = (new StatCrafting(i + l, s1, l)).registerStat();
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
            field_25188_a.remove(astatbase[i]);
            field_25185_d.remove(astatbase[i]);
            field_25187_b.remove(astatbase[i]);
            astatbase[i] = astatbase[j];
            return;
        }
    }

    public static StatBase func_27361_a(int i)
    {
        return (StatBase)field_25169_C.get(Integer.valueOf(i));
    }

    protected static Map field_25169_C = new HashMap();
    public static List field_25188_a = new ArrayList();
    public static List field_25187_b = new ArrayList();
    public static List field_25186_c = new ArrayList();
    public static List field_25185_d = new ArrayList();
    public static StatBase startGameStat = (new StatBasic(1000, StatCollector.translateToLocal("stat.startGame"))).func_27082_h().registerStat();
    public static StatBase createWorldStat = (new StatBasic(1001, StatCollector.translateToLocal("stat.createWorld"))).func_27082_h().registerStat();
    public static StatBase loadWorldStat = (new StatBasic(1002, StatCollector.translateToLocal("stat.loadWorld"))).func_27082_h().registerStat();
    public static StatBase joinMultiplayerStat = (new StatBasic(1003, StatCollector.translateToLocal("stat.joinMultiplayer"))).func_27082_h().registerStat();
    public static StatBase leaveGameStat = (new StatBasic(1004, StatCollector.translateToLocal("stat.leaveGame"))).func_27082_h().registerStat();
    public static StatBase minutesPlayedStat;
    public static StatBase distanceWalkedStat;
    public static StatBase distanceSwumStat;
    public static StatBase distanceFallenStat;
    public static StatBase distanceClimbedStat;
    public static StatBase distanceFlownStat;
    public static StatBase distanceDoveStat;
    public static StatBase distanceByMinecartStat;
    public static StatBase distanceByBoatStat;
    public static StatBase distanceByPigStat;
    public static StatBase jumpStat = (new StatBasic(2010, StatCollector.translateToLocal("stat.jump"))).func_27082_h().registerStat();
    public static StatBase dropStat = (new StatBasic(2011, StatCollector.translateToLocal("stat.drop"))).func_27082_h().registerStat();
    public static StatBase damageDealtStat = (new StatBasic(2020, StatCollector.translateToLocal("stat.damageDealt"))).registerStat();
    public static StatBase damageTakenStat = (new StatBasic(2021, StatCollector.translateToLocal("stat.damageTaken"))).registerStat();
    public static StatBase deathsStat = (new StatBasic(2022, StatCollector.translateToLocal("stat.deaths"))).registerStat();
    public static StatBase mobKillsStat = (new StatBasic(2023, StatCollector.translateToLocal("stat.mobKills"))).registerStat();
    public static StatBase playerKillsStat = (new StatBasic(2024, StatCollector.translateToLocal("stat.playerKills"))).registerStat();
    public static StatBase fishCaughtStat = (new StatBasic(2025, StatCollector.translateToLocal("stat.fishCaught"))).registerStat();
    public static StatBase mineBlockStatArray[] = func_25153_a("stat.mineBlock", 0x1000000);
    public static StatBase field_25158_z[];
    public static StatBase field_25172_A[];
    public static StatBase field_25170_B[];
    private static boolean field_25166_D = false;
    private static boolean field_25164_E = false;

    static 
    {
        minutesPlayedStat = (new StatBasic(1100, StatCollector.translateToLocal("stat.playOneMinute"), StatBase.field_27086_j)).func_27082_h().registerStat();
        distanceWalkedStat = (new StatBasic(2000, StatCollector.translateToLocal("stat.walkOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceSwumStat = (new StatBasic(2001, StatCollector.translateToLocal("stat.swimOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceFallenStat = (new StatBasic(2002, StatCollector.translateToLocal("stat.fallOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceClimbedStat = (new StatBasic(2003, StatCollector.translateToLocal("stat.climbOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceFlownStat = (new StatBasic(2004, StatCollector.translateToLocal("stat.flyOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceDoveStat = (new StatBasic(2005, StatCollector.translateToLocal("stat.diveOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceByMinecartStat = (new StatBasic(2006, StatCollector.translateToLocal("stat.minecartOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceByBoatStat = (new StatBasic(2007, StatCollector.translateToLocal("stat.boatOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        distanceByPigStat = (new StatBasic(2008, StatCollector.translateToLocal("stat.pigOneCm"), StatBase.field_27085_k)).func_27082_h().registerStat();
        AchievementList.func_27374_a();
    }
}
