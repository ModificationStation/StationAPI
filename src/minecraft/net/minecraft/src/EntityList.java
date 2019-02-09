// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            World, Entity, NBTTagCompound, EntityArrow, 
//            EntitySnowball, EntityItem, EntityPainting, EntityLiving, 
//            EntityMob, EntityCreeper, EntitySkeleton, EntitySpider, 
//            EntityGiantZombie, EntityZombie, EntitySlime, EntityGhast, 
//            EntityPigZombie, EntityPig, EntitySheep, EntityCow, 
//            EntityChicken, EntitySquid, EntityWolf, EntityTNTPrimed, 
//            EntityFallingSand, EntityMinecart, EntityBoat

public class EntityList
{

    public EntityList()
    {
    }

    private static void addMapping(Class class1, String s, int i)
    {
        stringToClassMapping.put(s, class1);
        classToStringMapping.put(class1, s);
        IDtoClassMapping.put(Integer.valueOf(i), class1);
        classToIDMapping.put(class1, Integer.valueOf(i));
    }

    public static Entity createEntityInWorld(String s, World world)
    {
        Entity entity = null;
        try
        {
            Class class1 = (Class)stringToClassMapping.get(s);
            if(class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[] {
                    net.minecraft.src.World.class
                }).newInstance(new Object[] {
                    world
                });
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return entity;
    }

    public static Entity createEntityFromNBT(NBTTagCompound nbttagcompound, World world)
    {
        Entity entity = null;
        try
        {
            Class class1 = (Class)stringToClassMapping.get(nbttagcompound.getString("id"));
            if(class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[] {
                    net.minecraft.src.World.class
                }).newInstance(new Object[] {
                    world
                });
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(entity != null)
        {
            entity.readFromNBT(nbttagcompound);
        } else
        {
            System.out.println((new StringBuilder()).append("Skipping Entity with id ").append(nbttagcompound.getString("id")).toString());
        }
        return entity;
    }

    public static Entity createEntity(int i, World world)
    {
        Entity entity = null;
        try
        {
            Class class1 = (Class)IDtoClassMapping.get(Integer.valueOf(i));
            if(class1 != null)
            {
                entity = (Entity)class1.getConstructor(new Class[] {
                    net.minecraft.src.World.class
                }).newInstance(new Object[] {
                    world
                });
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(entity == null)
        {
            System.out.println((new StringBuilder()).append("Skipping Entity with id ").append(i).toString());
        }
        return entity;
    }

    public static int getEntityID(Entity entity)
    {
        return ((Integer)classToIDMapping.get(entity.getClass())).intValue();
    }

    public static String getEntityString(Entity entity)
    {
        return (String)classToStringMapping.get(entity.getClass());
    }

    private static Map stringToClassMapping = new HashMap();
    private static Map classToStringMapping = new HashMap();
    private static Map IDtoClassMapping = new HashMap();
    private static Map classToIDMapping = new HashMap();

    static 
    {
        addMapping(net.minecraft.src.EntityArrow.class, "Arrow", 10);
        addMapping(net.minecraft.src.EntitySnowball.class, "Snowball", 11);
        addMapping(net.minecraft.src.EntityItem.class, "Item", 1);
        addMapping(net.minecraft.src.EntityPainting.class, "Painting", 9);
        addMapping(net.minecraft.src.EntityLiving.class, "Mob", 48);
        addMapping(net.minecraft.src.EntityMob.class, "Monster", 49);
        addMapping(net.minecraft.src.EntityCreeper.class, "Creeper", 50);
        addMapping(net.minecraft.src.EntitySkeleton.class, "Skeleton", 51);
        addMapping(net.minecraft.src.EntitySpider.class, "Spider", 52);
        addMapping(net.minecraft.src.EntityGiantZombie.class, "Giant", 53);
        addMapping(net.minecraft.src.EntityZombie.class, "Zombie", 54);
        addMapping(net.minecraft.src.EntitySlime.class, "Slime", 55);
        addMapping(net.minecraft.src.EntityGhast.class, "Ghast", 56);
        addMapping(net.minecraft.src.EntityPigZombie.class, "PigZombie", 57);
        addMapping(net.minecraft.src.EntityPig.class, "Pig", 90);
        addMapping(net.minecraft.src.EntitySheep.class, "Sheep", 91);
        addMapping(net.minecraft.src.EntityCow.class, "Cow", 92);
        addMapping(net.minecraft.src.EntityChicken.class, "Chicken", 93);
        addMapping(net.minecraft.src.EntitySquid.class, "Squid", 94);
        addMapping(net.minecraft.src.EntityWolf.class, "Wolf", 95);
        addMapping(net.minecraft.src.EntityTNTPrimed.class, "PrimedTnt", 20);
        addMapping(net.minecraft.src.EntityFallingSand.class, "FallingSand", 21);
        addMapping(net.minecraft.src.EntityMinecart.class, "Minecart", 40);
        addMapping(net.minecraft.src.EntityBoat.class, "Boat", 41);
    }
}
