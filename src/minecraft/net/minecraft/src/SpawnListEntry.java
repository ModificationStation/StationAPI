// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


public class SpawnListEntry
{

    public SpawnListEntry(Class class1, int i)
    {
        entityClass = class1;
        spawnRarityRate = i;
    }

    public Class entityClass;
    public int spawnRarityRate;
}
