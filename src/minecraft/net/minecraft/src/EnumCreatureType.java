// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IMob, Material, EntityAnimal, EntityWaterMob

public enum EnumCreatureType
{
    monster("monster", 0, net.minecraft.src.IMob.class, 70, Material.air, false),
    creature("creature", 1, net.minecraft.src.EntityAnimal.class, 15, Material.air, true),
    waterCreature("waterCreature", 2, net.minecraft.src.EntityWaterMob.class, 5, Material.water, true);
/*
    public static EnumCreatureType[] values()
    {
        return (EnumCreatureType[])field_6518_e.clone();
    }

    public static EnumCreatureType valueOf(String s)
    {
        return (EnumCreatureType)Enum.valueOf(net.minecraft.src.EnumCreatureType.class, s);
    }
*/
    private EnumCreatureType(String s, int i, Class class1, int j, Material material, boolean flag)
    {
//        super(s, i);
        creatureClass = class1;
        maxNumberOfCreature = j;
        creatureMaterial = material;
        isPeacefulCreature = flag;
    }

    public Class getCreatureClass()
    {
        return creatureClass;
    }

    public int getMaxNumberOfCreature()
    {
        return maxNumberOfCreature;
    }

    public Material getCreatureMaterial()
    {
        return creatureMaterial;
    }

    public boolean getPeacefulCreature()
    {
        return isPeacefulCreature;
    }
/*
    public static final EnumCreatureType monster;
    public static final EnumCreatureType creature;
    public static final EnumCreatureType waterCreature;
*/
    private final Class creatureClass;
    private final int maxNumberOfCreature;
    private final Material creatureMaterial;
    private final boolean isPeacefulCreature;
    private static final EnumCreatureType field_6518_e[]; /* synthetic field */

    static 
    {
/*
        monster = new EnumCreatureType("monster", 0, net.minecraft.src.IMob.class, 70, Material.air, false);
        creature = new EnumCreatureType("creature", 1, net.minecraft.src.EntityAnimal.class, 15, Material.air, true);
        waterCreature = new EnumCreatureType("waterCreature", 2, net.minecraft.src.EntityWaterMob.class, 5, Material.water, true);
*/
        field_6518_e = (new EnumCreatureType[] {
            monster, creature, waterCreature
        });
    }
}
