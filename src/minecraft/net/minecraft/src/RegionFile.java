// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;

// Referenced classes of package net.minecraft.src:
//            RegionFileChunkBuffer

public class RegionFile
{

    public RegionFile(File file)
    {
        field_22214_h = 0L;
        fileName = file;
        debugln((new StringBuilder()).append("REGION LOAD ").append(fileName).toString());
        sizeDelta = 0;
        try
        {
            if(file.exists())
            {
                field_22214_h = file.lastModified();
            }
            dataFile = new RandomAccessFile(file, "rw");
            if(dataFile.length() < 4096L)
            {
                for(int i = 0; i < 1024; i++)
                {
                    dataFile.writeInt(0);
                }

                for(int j = 0; j < 1024; j++)
                {
                    dataFile.writeInt(0);
                }

                sizeDelta += 8192;
            }
            if((dataFile.length() & 4095L) != 0L)
            {
                for(int k = 0; (long)k < (dataFile.length() & 4095L); k++)
                {
                    dataFile.write(0);
                }

            }
            int l = (int)dataFile.length() / 4096;
            sectorFree = new ArrayList(l);
            for(int i1 = 0; i1 < l; i1++)
            {
                sectorFree.add(Boolean.valueOf(true));
            }

            sectorFree.set(0, Boolean.valueOf(false));
            sectorFree.set(1, Boolean.valueOf(false));
            dataFile.seek(0L);
            for(int j1 = 0; j1 < 1024; j1++)
            {
                int l1 = dataFile.readInt();
                offsets[j1] = l1;
                if(l1 == 0 || (l1 >> 8) + (l1 & 0xff) > sectorFree.size())
                {
                    continue;
                }
                for(int j2 = 0; j2 < (l1 & 0xff); j2++)
                {
                    sectorFree.set((l1 >> 8) + j2, Boolean.valueOf(false));
                }

            }

            for(int k1 = 0; k1 < 1024; k1++)
            {
                int i2 = dataFile.readInt();
                field_22217_e[k1] = i2;
            }

        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public synchronized int func_22209_a()
    {
        int i = sizeDelta;
        sizeDelta = 0;
        return i;
    }

    private void func_22211_a(String s)
    {
    }

    private void debugln(String s)
    {
        func_22211_a((new StringBuilder()).append(s).append("\n").toString());
    }

    private void func_22199_a(String s, int i, int j, String s1)
    {
        func_22211_a((new StringBuilder()).append("REGION ").append(s).append(" ").append(fileName.getName()).append("[").append(i).append(",").append(j).append("] = ").append(s1).toString());
    }

    private void func_22197_a(String s, int i, int j, int k, String s1)
    {
        func_22211_a((new StringBuilder()).append("REGION ").append(s).append(" ").append(fileName.getName()).append("[").append(i).append(",").append(j).append("] ").append(k).append("B = ").append(s1).toString());
    }

    private void debugln(String s, int i, int j, String s1)
    {
        func_22199_a(s, i, j, (new StringBuilder()).append(s1).append("\n").toString());
    }

    public synchronized DataInputStream getChunkDataInputStream(int i, int j)
    {
        if(outOfBounds(i, j))
        {
            debugln("READ", i, j, "out of bounds");
            return null;
        }
        try
        {
            int k = getOffset(i, j);
            if(k == 0)
            {
                return null;
            }
            int l = k >> 8;
            int i1 = k & 0xff;
            if(l + i1 > sectorFree.size())
            {
                debugln("READ", i, j, "invalid sector");
                return null;
            }
            dataFile.seek(l * 4096);
            int j1 = dataFile.readInt();
            if(j1 > 4096 * i1)
            {
                debugln("READ", i, j, (new StringBuilder()).append("invalid length: ").append(j1).append(" > 4096 * ").append(i1).toString());
                return null;
            }
            byte byte0 = dataFile.readByte();
            if(byte0 == 1)
            {
                byte abyte0[] = new byte[j1 - 1];
                dataFile.read(abyte0);
                DataInputStream datainputstream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte0)));
                return datainputstream;
            }
            if(byte0 == 2)
            {
                byte abyte1[] = new byte[j1 - 1];
                dataFile.read(abyte1);
                DataInputStream datainputstream1 = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte1)));
                return datainputstream1;
            } else
            {
                debugln("READ", i, j, (new StringBuilder()).append("unknown version ").append(byte0).toString());
                return null;
            }
        }
        catch(IOException ioexception)
        {
            debugln("READ", i, j, "exception");
        }
        return null;
    }

    public DataOutputStream getChunkDataOutputStream(int i, int j)
    {
        if(outOfBounds(i, j))
        {
            return null;
        } else
        {
            return new DataOutputStream(new DeflaterOutputStream(new RegionFileChunkBuffer(this, i, j)));
        }
    }

    protected synchronized void write(int i, int j, byte abyte0[], int k)
    {
        try
        {
            int l = getOffset(i, j);
            int i1 = l >> 8;
            int l1 = l & 0xff;
            int i2 = (k + 5) / 4096 + 1;
            if(i2 >= 256)
            {
                return;
            }
            if(i1 != 0 && l1 == i2)
            {
                func_22197_a("SAVE", i, j, k, "rewrite");
                write(i1, abyte0, k);
            } else
            {
                for(int j2 = 0; j2 < l1; j2++)
                {
                    sectorFree.set(i1 + j2, Boolean.valueOf(true));
                }

                int k2 = sectorFree.indexOf(Boolean.valueOf(true));
                int l2 = 0;
                if(k2 != -1)
                {
                    int i3 = k2;
                    do
                    {
                        if(i3 >= sectorFree.size())
                        {
                            break;
                        }
                        if(l2 != 0)
                        {
                            if(((Boolean)sectorFree.get(i3)).booleanValue())
                            {
                                l2++;
                            } else
                            {
                                l2 = 0;
                            }
                        } else
                        if(((Boolean)sectorFree.get(i3)).booleanValue())
                        {
                            k2 = i3;
                            l2 = 1;
                        }
                        if(l2 >= i2)
                        {
                            break;
                        }
                        i3++;
                    } while(true);
                }
                if(l2 >= i2)
                {
                    func_22197_a("SAVE", i, j, k, "reuse");
                    int j1 = k2;
                    setOffset(i, j, j1 << 8 | i2);
                    for(int j3 = 0; j3 < i2; j3++)
                    {
                        sectorFree.set(j1 + j3, Boolean.valueOf(false));
                    }

                    write(j1, abyte0, k);
                } else
                {
                    func_22197_a("SAVE", i, j, k, "grow");
                    dataFile.seek(dataFile.length());
                    int k1 = sectorFree.size();
                    for(int k3 = 0; k3 < i2; k3++)
                    {
                        dataFile.write(emptySector);
                        sectorFree.add(Boolean.valueOf(false));
                    }

                    sizeDelta += 4096 * i2;
                    write(k1, abyte0, k);
                    setOffset(i, j, k1 << 8 | i2);
                }
            }
            func_22208_b(i, j, (int)(System.currentTimeMillis() / 1000L));
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private void write(int i, byte abyte0[], int j)
        throws IOException
    {
        debugln((new StringBuilder()).append(" ").append(i).toString());
        dataFile.seek(i * 4096);
        dataFile.writeInt(j + 1);
        dataFile.writeByte(2);
        dataFile.write(abyte0, 0, j);
    }

    private boolean outOfBounds(int i, int j)
    {
        return i < 0 || i >= 32 || j < 0 || j >= 32;
    }

    private int getOffset(int i, int j)
    {
        return offsets[i + j * 32];
    }

    public boolean func_22202_c(int i, int j)
    {
        return getOffset(i, j) != 0;
    }

    private void setOffset(int i, int j, int k)
        throws IOException
    {
        offsets[i + j * 32] = k;
        dataFile.seek((i + j * 32) * 4);
        dataFile.writeInt(k);
    }

    private void func_22208_b(int i, int j, int k)
        throws IOException
    {
        field_22217_e[i + j * 32] = k;
        dataFile.seek(4096 + (i + j * 32) * 4);
        dataFile.writeInt(k);
    }

    public void func_22196_b()
        throws IOException
    {
        dataFile.close();
    }

    private static final byte emptySector[] = new byte[4096];
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int offsets[] = new int[1024];
    private final int field_22217_e[] = new int[1024];
    private ArrayList sectorFree;
    private int sizeDelta;
    private long field_22214_h;

}
