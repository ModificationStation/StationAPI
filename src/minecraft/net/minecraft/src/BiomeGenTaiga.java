// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BiomeGenBase, SpawnListEntry, EntityWolf, WorldGenTaiga1, 
//            WorldGenTaiga2, WorldGenerator

public class BiomeGenTaiga extends BiomeGenBase
{

    public BiomeGenTaiga()
    {
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityWolf.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(3) == 0)
        {
            return new WorldGenTaiga1();
        } else
        {
            return new WorldGenTaiga2();
        }
    }
}
