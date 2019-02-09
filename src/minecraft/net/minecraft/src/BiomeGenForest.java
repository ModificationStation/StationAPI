// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BiomeGenBase, SpawnListEntry, EntityWolf, WorldGenForest, 
//            WorldGenBigTree, WorldGenTrees, WorldGenerator

public class BiomeGenForest extends BiomeGenBase
{

    public BiomeGenForest()
    {
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityWolf.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(5) == 0)
        {
            return new WorldGenForest();
        }
        if(random.nextInt(3) == 0)
        {
            return new WorldGenBigTree();
        } else
        {
            return new WorldGenTrees();
        }
    }
}
