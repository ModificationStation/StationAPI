// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            IChunkProvider, MapGenCavesHell, NoiseGeneratorOctaves, Block, 
//            MapGenBase, Chunk, BlockSand, WorldGenHellLava, 
//            WorldGenFire, WorldGenGlowStone1, WorldGenGlowStone2, WorldGenFlowers, 
//            BlockFlower, World, IProgressUpdate

public class ChunkProviderHell
    implements IChunkProvider
{

    public ChunkProviderHell(World world, long l)
    {
        field_4162_p = new double[256];
        field_4161_q = new double[256];
        field_4160_r = new double[256];
        field_4159_s = new MapGenCavesHell();
        worldObj = world;
        hellRNG = new Random(l);
        field_4169_i = new NoiseGeneratorOctaves(hellRNG, 16);
        field_4168_j = new NoiseGeneratorOctaves(hellRNG, 16);
        field_4167_k = new NoiseGeneratorOctaves(hellRNG, 8);
        field_4166_l = new NoiseGeneratorOctaves(hellRNG, 4);
        field_4165_m = new NoiseGeneratorOctaves(hellRNG, 4);
        field_4177_a = new NoiseGeneratorOctaves(hellRNG, 10);
        field_4176_b = new NoiseGeneratorOctaves(hellRNG, 16);
    }

    public void func_4059_a(int i, int j, byte abyte0[])
    {
        byte byte0 = 4;
        byte byte1 = 32;
        int k = byte0 + 1;
        byte byte2 = 17;
        int l = byte0 + 1;
        field_4163_o = func_4057_a(field_4163_o, i * byte0, 0, j * byte0, k, byte2, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 16; k1++)
                {
                    double d = 0.125D;
                    double d1 = field_4163_o[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d2 = field_4163_o[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d3 = field_4163_o[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d4 = field_4163_o[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d5 = (field_4163_o[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4163_o[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4163_o[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4163_o[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d4) * d;
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
                                int l2 = 0;
                                if(k1 * 8 + l1 < byte1)
                                {
                                    l2 = Block.lavaStill.blockID;
                                }
                                if(d15 > 0.0D)
                                {
                                    l2 = Block.netherrack.blockID;
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

    public void func_4058_b(int i, int j, byte abyte0[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        field_4162_p = field_4166_l.generateNoiseOctaves(field_4162_p, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        field_4161_q = field_4166_l.generateNoiseOctaves(field_4161_q, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
        field_4160_r = field_4165_m.generateNoiseOctaves(field_4160_r, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                boolean flag = field_4162_p[k + l * 16] + hellRNG.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = field_4161_q[k + l * 16] + hellRNG.nextDouble() * 0.20000000000000001D > 0.0D;
                int i1 = (int)(field_4160_r[k + l * 16] / 3D + 3D + hellRNG.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = (byte)Block.netherrack.blockID;
                byte byte2 = (byte)Block.netherrack.blockID;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if(k1 >= 127 - hellRNG.nextInt(5))
                    {
                        abyte0[l1] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    if(k1 <= 0 + hellRNG.nextInt(5))
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
                    if(byte3 != Block.netherrack.blockID)
                    {
                        continue;
                    }
                    if(j1 == -1)
                    {
                        if(i1 <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Block.netherrack.blockID;
                        } else
                        if(k1 >= byte0 - 4 && k1 <= byte0 + 1)
                        {
                            byte1 = (byte)Block.netherrack.blockID;
                            byte2 = (byte)Block.netherrack.blockID;
                            if(flag1)
                            {
                                byte1 = (byte)Block.gravel.blockID;
                            }
                            if(flag1)
                            {
                                byte2 = (byte)Block.netherrack.blockID;
                            }
                            if(flag)
                            {
                                byte1 = (byte)Block.slowSand.blockID;
                            }
                            if(flag)
                            {
                                byte2 = (byte)Block.slowSand.blockID;
                            }
                        }
                        if(k1 < byte0 && byte1 == 0)
                        {
                            byte1 = (byte)Block.lavaStill.blockID;
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
                    if(j1 > 0)
                    {
                        j1--;
                        abyte0[l1] = byte2;
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
        hellRNG.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        func_4059_a(i, j, abyte0);
        func_4058_b(i, j, abyte0);
        field_4159_s.func_867_a(this, worldObj, i, j, abyte0);
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        return chunk;
    }

    private double[] func_4057_a(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 2053.2359999999999D;
        field_4172_f = field_4177_a.generateNoiseOctaves(field_4172_f, i, j, k, l, 1, j1, 1.0D, 0.0D, 1.0D);
        field_4171_g = field_4176_b.generateNoiseOctaves(field_4171_g, i, j, k, l, 1, j1, 100D, 0.0D, 100D);
        field_4175_c = field_4167_k.generateNoiseOctaves(field_4175_c, i, j, k, l, i1, j1, d / 80D, d1 / 60D, d / 80D);
        field_4174_d = field_4169_i.generateNoiseOctaves(field_4174_d, i, j, k, l, i1, j1, d, d1, d);
        field_4173_e = field_4168_j.generateNoiseOctaves(field_4173_e, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        double ad1[] = new double[i1];
        for(int i2 = 0; i2 < i1; i2++)
        {
            ad1[i2] = Math.cos(((double)i2 * 3.1415926535897931D * 6D) / (double)i1) * 2D;
            double d2 = i2;
            if(i2 > i1 / 2)
            {
                d2 = i1 - 1 - i2;
            }
            if(d2 < 4D)
            {
                d2 = 4D - d2;
                ad1[i2] -= d2 * d2 * d2 * 10D;
            }
        }

        for(int j2 = 0; j2 < l; j2++)
        {
            for(int k2 = 0; k2 < j1; k2++)
            {
                double d3 = (field_4172_f[l1] + 256D) / 512D;
                if(d3 > 1.0D)
                {
                    d3 = 1.0D;
                }
                double d4 = 0.0D;
                double d5 = field_4171_g[l1] / 8000D;
                if(d5 < 0.0D)
                {
                    d5 = -d5;
                }
                d5 = d5 * 3D - 3D;
                if(d5 < 0.0D)
                {
                    d5 /= 2D;
                    if(d5 < -1D)
                    {
                        d5 = -1D;
                    }
                    d5 /= 1.3999999999999999D;
                    d5 /= 2D;
                    d3 = 0.0D;
                } else
                {
                    if(d5 > 1.0D)
                    {
                        d5 = 1.0D;
                    }
                    d5 /= 6D;
                }
                d3 += 0.5D;
                d5 = (d5 * (double)i1) / 16D;
                l1++;
                for(int l2 = 0; l2 < i1; l2++)
                {
                    double d6 = 0.0D;
                    double d7 = ad1[l2];
                    double d8 = field_4174_d[k1] / 512D;
                    double d9 = field_4173_e[k1] / 512D;
                    double d10 = (field_4175_c[k1] / 10D + 1.0D) / 2D;
                    if(d10 < 0.0D)
                    {
                        d6 = d8;
                    } else
                    if(d10 > 1.0D)
                    {
                        d6 = d9;
                    } else
                    {
                        d6 = d8 + (d9 - d8) * d10;
                    }
                    d6 -= d7;
                    if(l2 > i1 - 4)
                    {
                        double d11 = (float)(l2 - (i1 - 4)) / 3F;
                        d6 = d6 * (1.0D - d11) + -10D * d11;
                    }
                    if((double)l2 < d4)
                    {
                        double d12 = (d4 - (double)l2) / 4D;
                        if(d12 < 0.0D)
                        {
                            d12 = 0.0D;
                        }
                        if(d12 > 1.0D)
                        {
                            d12 = 1.0D;
                        }
                        d6 = d6 * (1.0D - d12) + -10D * d12;
                    }
                    ad[k1] = d6;
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
        for(int i1 = 0; i1 < 8; i1++)
        {
            int k1 = k + hellRNG.nextInt(16) + 8;
            int i3 = hellRNG.nextInt(120) + 4;
            int k4 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenHellLava(Block.lavaMoving.blockID)).generate(worldObj, hellRNG, k1, i3, k4);
        }

        int j1 = hellRNG.nextInt(hellRNG.nextInt(10) + 1) + 1;
        for(int l1 = 0; l1 < j1; l1++)
        {
            int j3 = k + hellRNG.nextInt(16) + 8;
            int l4 = hellRNG.nextInt(120) + 4;
            int i6 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenFire()).generate(worldObj, hellRNG, j3, l4, i6);
        }

        j1 = hellRNG.nextInt(hellRNG.nextInt(10) + 1);
        for(int i2 = 0; i2 < j1; i2++)
        {
            int k3 = k + hellRNG.nextInt(16) + 8;
            int i5 = hellRNG.nextInt(120) + 4;
            int j6 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone1()).generate(worldObj, hellRNG, k3, i5, j6);
        }

        for(int j2 = 0; j2 < 10; j2++)
        {
            int l3 = k + hellRNG.nextInt(16) + 8;
            int j5 = hellRNG.nextInt(128);
            int k6 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone2()).generate(worldObj, hellRNG, l3, j5, k6);
        }

        if(hellRNG.nextInt(1) == 0)
        {
            int k2 = k + hellRNG.nextInt(16) + 8;
            int i4 = hellRNG.nextInt(128);
            int k5 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, hellRNG, k2, i4, k5);
        }
        if(hellRNG.nextInt(1) == 0)
        {
            int l2 = k + hellRNG.nextInt(16) + 8;
            int j4 = hellRNG.nextInt(128);
            int l5 = l + hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, hellRNG, l2, j4, l5);
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
        return "HellRandomLevelSource";
    }

    private Random hellRNG;
    private NoiseGeneratorOctaves field_4169_i;
    private NoiseGeneratorOctaves field_4168_j;
    private NoiseGeneratorOctaves field_4167_k;
    private NoiseGeneratorOctaves field_4166_l;
    private NoiseGeneratorOctaves field_4165_m;
    public NoiseGeneratorOctaves field_4177_a;
    public NoiseGeneratorOctaves field_4176_b;
    private World worldObj;
    private double field_4163_o[];
    private double field_4162_p[];
    private double field_4161_q[];
    private double field_4160_r[];
    private MapGenBase field_4159_s;
    double field_4175_c[];
    double field_4174_d[];
    double field_4173_e[];
    double field_4172_f[];
    double field_4171_g[];
}
