// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            IChunkProvider, MapGenCaves, NoiseGeneratorOctaves, Block, 
//            BiomeGenBase, Chunk, World, WorldChunkManager, 
//            MapGenBase, BlockSand, WorldGenLakes, WorldGenDungeons, 
//            WorldGenClay, WorldGenMinable, WorldGenerator, WorldGenFlowers, 
//            BlockFlower, WorldGenTallGrass, BlockTallGrass, WorldGenDeadBush, 
//            BlockDeadBush, WorldGenReed, WorldGenPumpkin, WorldGenCactus, 
//            WorldGenLiquids, Material, IProgressUpdate

public class ChunkProviderGenerate
    implements IChunkProvider
{

    public ChunkProviderGenerate(World world, long l)
    {
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        field_902_u = new MapGenCaves();
        field_914_i = new int[32][32];
        worldObj = world;
        rand = new Random(l);
        field_912_k = new NoiseGeneratorOctaves(rand, 16);
        field_911_l = new NoiseGeneratorOctaves(rand, 16);
        field_910_m = new NoiseGeneratorOctaves(rand, 8);
        field_909_n = new NoiseGeneratorOctaves(rand, 4);
        field_908_o = new NoiseGeneratorOctaves(rand, 4);
        field_922_a = new NoiseGeneratorOctaves(rand, 10);
        field_921_b = new NoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new NoiseGeneratorOctaves(rand, 8);
    }

    public void generateTerrain(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[], double ad[])
    {
        byte byte0 = 4;
        byte byte1 = 64;
        int k = byte0 + 1;
        byte byte2 = 17;
        int l = byte0 + 1;
        field_4180_q = func_4061_a(field_4180_q, i * byte0, 0, j * byte0, k, byte2, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 16; k1++)
                {
                    double d = 0.125D;
                    double d1 = field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d2 = field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d3 = field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d4 = field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d5 = (field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 8; l1++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 4; i2++)
                        {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 4; k2++)
                            {
                                double d17 = ad[(i1 * 4 + i2) * 16 + (j1 * 4 + k2)];
                                int l2 = 0;
                                if(k1 * 8 + l1 < byte1)
                                {
                                    if(d17 < 0.5D && k1 * 8 + l1 >= byte1 - 1)
                                    {
                                        l2 = Block.ice.blockID;
                                    } else
                                    {
                                        l2 = Block.waterStill.blockID;
                                    }
                                }
                                if(d15 > 0.0D)
                                {
                                    l2 = Block.stone.blockID;
                                }
                                abyte0[j2] = (byte)l2;
                                j2 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }

                }

            }

        }

    }

    public void replaceBlocksForBiome(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        sandNoise = field_909_n.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = field_908_o.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                BiomeGenBase biomegenbase = abiomegenbase[k + l * 16];
                boolean flag = sandNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = gravelNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = biomegenbase.topBlock;
                byte byte2 = biomegenbase.fillerBlock;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if(k1 <= 0 + rand.nextInt(5))
                    {
                        abyte0[l1] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    byte byte3 = abyte0[l1];
                    if(byte3 == 0)
                    {
                        j1 = -1;
                        continue;
                    }
                    if(byte3 != Block.stone.blockID)
                    {
                        continue;
                    }
                    if(j1 == -1)
                    {
                        if(i1 <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Block.stone.blockID;
                        } else
                        if(k1 >= byte0 - 4 && k1 <= byte0 + 1)
                        {
                            byte1 = biomegenbase.topBlock;
                            byte2 = biomegenbase.fillerBlock;
                            if(flag1)
                            {
                                byte1 = 0;
                            }
                            if(flag1)
                            {
                                byte2 = (byte)Block.gravel.blockID;
                            }
                            if(flag)
                            {
                                byte1 = (byte)Block.sand.blockID;
                            }
                            if(flag)
                            {
                                byte2 = (byte)Block.sand.blockID;
                            }
                        }
                        if(k1 < byte0 && byte1 == 0)
                        {
                            byte1 = (byte)Block.waterStill.blockID;
                        }
                        j1 = i1;
                        if(k1 >= byte0 - 1)
                        {
                            abyte0[l1] = byte1;
                        } else
                        {
                            abyte0[l1] = byte2;
                        }
                        continue;
                    }
                    if(j1 <= 0)
                    {
                        continue;
                    }
                    j1--;
                    abyte0[l1] = byte2;
                    if(j1 == 0 && byte2 == Block.sand.blockID)
                    {
                        j1 = rand.nextInt(4);
                        byte2 = (byte)Block.sandStone.blockID;
                    }
                }

            }

        }

    }

    public Chunk prepareChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        double ad[] = worldObj.getWorldChunkManager().temperature;
        generateTerrain(i, j, abyte0, biomesForGeneration, ad);
        replaceBlocksForBiome(i, j, abyte0, biomesForGeneration);
        field_902_u.func_867_a(this, worldObj, i, j, abyte0);
        chunk.func_1024_c();
        return chunk;
    }

    private double[] func_4061_a(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double ad1[] = worldObj.getWorldChunkManager().temperature;
        double ad2[] = worldObj.getWorldChunkManager().humidity;
        field_4182_g = field_922_a.func_4109_a(field_4182_g, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        field_4181_h = field_921_b.func_4109_a(field_4181_h, i, k, l, j1, 200D, 200D, 0.5D);
        field_4185_d = field_910_m.generateNoiseOctaves(field_4185_d, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        field_4184_e = field_912_k.generateNoiseOctaves(field_4184_e, i, j, k, l, i1, j1, d, d1, d);
        field_4183_f = field_911_l.generateNoiseOctaves(field_4183_f, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / l;
        for(int j2 = 0; j2 < l; j2++)
        {
            int k2 = j2 * i2 + i2 / 2;
            for(int l2 = 0; l2 < j1; l2++)
            {
                int i3 = l2 * i2 + i2 / 2;
                double d2 = ad1[k2 * 16 + i3];
                double d3 = ad2[k2 * 16 + i3] * d2;
                double d4 = 1.0D - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (field_4182_g[l1] + 256D) / 512D;
                d5 *= d4;
                if(d5 > 1.0D)
                {
                    d5 = 1.0D;
                }
                double d6 = field_4181_h[l1] / 8000D;
                if(d6 < 0.0D)
                {
                    d6 = -d6 * 0.29999999999999999D;
                }
                d6 = d6 * 3D - 2D;
                if(d6 < 0.0D)
                {
                    d6 /= 2D;
                    if(d6 < -1D)
                    {
                        d6 = -1D;
                    }
                    d6 /= 1.3999999999999999D;
                    d6 /= 2D;
                    d5 = 0.0D;
                } else
                {
                    if(d6 > 1.0D)
                    {
                        d6 = 1.0D;
                    }
                    d6 /= 8D;
                }
                if(d5 < 0.0D)
                {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)i1) / 16D;
                double d7 = (double)i1 / 2D + d6 * 4D;
                l1++;
                for(int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (((double)j3 - d7) * 12D) / d5;
                    if(d9 < 0.0D)
                    {
                        d9 *= 4D;
                    }
                    double d10 = field_4184_e[k1] / 512D;
                    double d11 = field_4183_f[k1] / 512D;
                    double d12 = (field_4185_d[k1] / 10D + 1.0D) / 2D;
                    if(d12 < 0.0D)
                    {
                        d8 = d10;
                    } else
                    if(d12 > 1.0D)
                    {
                        d8 = d11;
                    } else
                    {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= d9;
                    if(j3 > i1 - 4)
                    {
                        double d13 = (float)(j3 - (i1 - 4)) / 3F;
                        d8 = d8 * (1.0D - d13) + -10D * d13;
                    }
                    ad[k1] = d8;
                    k1++;
                }

            }

        }

        return ad;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getRandomSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getRandomSeed());
        double d = 0.25D;
        if(rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int l4 = rand.nextInt(128);
            int i8 = l + rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, l4, i8);
        }
        if(rand.nextInt(8) == 0)
        {
            int j1 = k + rand.nextInt(16) + 8;
            int i5 = rand.nextInt(rand.nextInt(120) + 8);
            int j8 = l + rand.nextInt(16) + 8;
            if(i5 < 64 || rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j1, i5, j8);
            }
        }
        for(int k1 = 0; k1 < 8; k1++)
        {
            int j5 = k + rand.nextInt(16) + 8;
            int k8 = rand.nextInt(128);
            int j11 = l + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, j5, k8, j11);
        }

        for(int i2 = 0; i2 < 10; i2++)
        {
            int k5 = k + rand.nextInt(16);
            int l8 = rand.nextInt(128);
            int k11 = l + rand.nextInt(16);
            (new WorldGenClay(32)).generate(worldObj, rand, k5, l8, k11);
        }

        for(int j2 = 0; j2 < 20; j2++)
        {
            int l5 = k + rand.nextInt(16);
            int i9 = rand.nextInt(128);
            int l11 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, l5, i9, l11);
        }

        for(int k2 = 0; k2 < 10; k2++)
        {
            int i6 = k + rand.nextInt(16);
            int j9 = rand.nextInt(128);
            int i12 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, i6, j9, i12);
        }

        for(int i3 = 0; i3 < 20; i3++)
        {
            int j6 = k + rand.nextInt(16);
            int k9 = rand.nextInt(128);
            int j12 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, j6, k9, j12);
        }

        for(int j3 = 0; j3 < 20; j3++)
        {
            int k6 = k + rand.nextInt(16);
            int l9 = rand.nextInt(64);
            int k12 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, k6, l9, k12);
        }

        for(int k3 = 0; k3 < 2; k3++)
        {
            int l6 = k + rand.nextInt(16);
            int i10 = rand.nextInt(32);
            int l12 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, l6, i10, l12);
        }

        for(int l3 = 0; l3 < 8; l3++)
        {
            int i7 = k + rand.nextInt(16);
            int j10 = rand.nextInt(16);
            int i13 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, i7, j10, i13);
        }

        for(int i4 = 0; i4 < 1; i4++)
        {
            int j7 = k + rand.nextInt(16);
            int k10 = rand.nextInt(16);
            int j13 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, j7, k10, j13);
        }

        for(int j4 = 0; j4 < 1; j4++)
        {
            int k7 = k + rand.nextInt(16);
            int l10 = rand.nextInt(16) + rand.nextInt(16);
            int k13 = l + rand.nextInt(16);
            (new WorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, k7, l10, k13);
        }

        d = 0.5D;
        int k4 = (int)((mobSpawnerNoise.func_806_a((double)k * d, (double)l * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int l7 = 0;
        if(rand.nextInt(10) == 0)
        {
            l7++;
        }
        if(biomegenbase == BiomeGenBase.forest)
        {
            l7 += k4 + 5;
        }
        if(biomegenbase == BiomeGenBase.rainforest)
        {
            l7 += k4 + 5;
        }
        if(biomegenbase == BiomeGenBase.seasonalForest)
        {
            l7 += k4 + 2;
        }
        if(biomegenbase == BiomeGenBase.taiga)
        {
            l7 += k4 + 5;
        }
        if(biomegenbase == BiomeGenBase.desert)
        {
            l7 -= 20;
        }
        if(biomegenbase == BiomeGenBase.tundra)
        {
            l7 -= 20;
        }
        if(biomegenbase == BiomeGenBase.plains)
        {
            l7 -= 20;
        }
        for(int i11 = 0; i11 < l7; i11++)
        {
            int l13 = k + rand.nextInt(16) + 8;
            int j14 = l + rand.nextInt(16) + 8;
            WorldGenerator worldgenerator = biomegenbase.getRandomWorldGenForTrees(rand);
            worldgenerator.func_517_a(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(worldObj, rand, l13, worldObj.getHeightValue(l13, j14), j14);
        }

        byte byte0 = 0;
        if(biomegenbase == BiomeGenBase.forest)
        {
            byte0 = 2;
        }
        if(biomegenbase == BiomeGenBase.seasonalForest)
        {
            byte0 = 4;
        }
        if(biomegenbase == BiomeGenBase.taiga)
        {
            byte0 = 2;
        }
        if(biomegenbase == BiomeGenBase.plains)
        {
            byte0 = 3;
        }
        for(int i14 = 0; i14 < byte0; i14++)
        {
            int k14 = k + rand.nextInt(16) + 8;
            int l16 = rand.nextInt(128);
            int k19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, k14, l16, k19);
        }

        byte byte1 = 0;
        if(biomegenbase == BiomeGenBase.forest)
        {
            byte1 = 2;
        }
        if(biomegenbase == BiomeGenBase.rainforest)
        {
            byte1 = 10;
        }
        if(biomegenbase == BiomeGenBase.seasonalForest)
        {
            byte1 = 2;
        }
        if(biomegenbase == BiomeGenBase.taiga)
        {
            byte1 = 1;
        }
        if(biomegenbase == BiomeGenBase.plains)
        {
            byte1 = 10;
        }
        for(int l14 = 0; l14 < byte1; l14++)
        {
            byte byte2 = 1;
            if(biomegenbase == BiomeGenBase.rainforest && rand.nextInt(3) != 0)
            {
                byte2 = 2;
            }
            int l19 = k + rand.nextInt(16) + 8;
            int k22 = rand.nextInt(128);
            int j24 = l + rand.nextInt(16) + 8;
            (new WorldGenTallGrass(Block.tallGrass.blockID, byte2)).generate(worldObj, rand, l19, k22, j24);
        }

        byte1 = 0;
        if(biomegenbase == BiomeGenBase.desert)
        {
            byte1 = 2;
        }
        for(int i15 = 0; i15 < byte1; i15++)
        {
            int i17 = k + rand.nextInt(16) + 8;
            int i20 = rand.nextInt(128);
            int l22 = l + rand.nextInt(16) + 8;
            (new WorldGenDeadBush(Block.deadBush.blockID)).generate(worldObj, rand, i17, i20, l22);
        }

        if(rand.nextInt(2) == 0)
        {
            int j15 = k + rand.nextInt(16) + 8;
            int j17 = rand.nextInt(128);
            int j20 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, j15, j17, j20);
        }
        if(rand.nextInt(4) == 0)
        {
            int k15 = k + rand.nextInt(16) + 8;
            int k17 = rand.nextInt(128);
            int k20 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, k15, k17, k20);
        }
        if(rand.nextInt(8) == 0)
        {
            int l15 = k + rand.nextInt(16) + 8;
            int l17 = rand.nextInt(128);
            int l20 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, l15, l17, l20);
        }
        for(int i16 = 0; i16 < 10; i16++)
        {
            int i18 = k + rand.nextInt(16) + 8;
            int i21 = rand.nextInt(128);
            int i23 = l + rand.nextInt(16) + 8;
            (new WorldGenReed()).generate(worldObj, rand, i18, i21, i23);
        }

        if(rand.nextInt(32) == 0)
        {
            int j16 = k + rand.nextInt(16) + 8;
            int j18 = rand.nextInt(128);
            int j21 = l + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, j16, j18, j21);
        }
        int k16 = 0;
        if(biomegenbase == BiomeGenBase.desert)
        {
            k16 += 10;
        }
        for(int k18 = 0; k18 < k16; k18++)
        {
            int k21 = k + rand.nextInt(16) + 8;
            int j23 = rand.nextInt(128);
            int k24 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, k21, j23, k24);
        }

        for(int l18 = 0; l18 < 50; l18++)
        {
            int l21 = k + rand.nextInt(16) + 8;
            int k23 = rand.nextInt(rand.nextInt(120) + 8);
            int l24 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, l21, k23, l24);
        }

        for(int i19 = 0; i19 < 20; i19++)
        {
            int i22 = k + rand.nextInt(16) + 8;
            int l23 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int i25 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, i22, l23, i25);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures(generatedTemperatures, k + 8, l + 8, 16, 16);
        for(int j19 = k + 8; j19 < k + 8 + 16; j19++)
        {
            for(int j22 = l + 8; j22 < l + 8 + 16; j22++)
            {
                int i24 = j19 - (k + 8);
                int j25 = j22 - (l + 8);
                int k25 = worldObj.findTopSolidBlock(j19, j22);
                double d1 = generatedTemperatures[i24 * 16 + j25] - ((double)(k25 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && k25 > 0 && k25 < 128 && worldObj.isAirBlock(j19, k25, j22) && worldObj.getBlockMaterial(j19, k25 - 1, j22).getIsSolid() && worldObj.getBlockMaterial(j19, k25 - 1, j22) != Material.ice)
                {
                    worldObj.setBlockWithNotify(j19, k25, j22, Block.snow.blockID);
                }
            }

        }

        BlockSand.fallInstantly = false;
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        return true;
    }

    public boolean unload100OldestChunks()
    {
        return false;
    }

    public boolean canSave()
    {
        return true;
    }

    public String makeString()
    {
        return "RandomLevelSource";
    }

    private Random rand;
    private NoiseGeneratorOctaves field_912_k;
    private NoiseGeneratorOctaves field_911_l;
    private NoiseGeneratorOctaves field_910_m;
    private NoiseGeneratorOctaves field_909_n;
    private NoiseGeneratorOctaves field_908_o;
    public NoiseGeneratorOctaves field_922_a;
    public NoiseGeneratorOctaves field_921_b;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private MapGenBase field_902_u;
    private BiomeGenBase biomesForGeneration[];
    double field_4185_d[];
    double field_4184_e[];
    double field_4183_f[];
    double field_4182_g[];
    double field_4181_h[];
    int field_914_i[][];
    private double generatedTemperatures[];
}
