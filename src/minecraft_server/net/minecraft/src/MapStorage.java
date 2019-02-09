// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

// Referenced classes of package net.minecraft.src:
//            MapDataBase, ISaveHandler, CompressedStreamTools, NBTTagCompound, 
//            NBTBase, NBTTagShort

public class MapStorage
{

    public MapStorage(ISaveHandler isavehandler)
    {
        field_28179_b = new HashMap();
        field_28182_c = new ArrayList();
        field_28181_d = new HashMap();
        field_28180_a = isavehandler;
        func_28174_b();
    }

    public MapDataBase func_28178_a(Class class1, String s)
    {
        MapDataBase mapdatabase = (MapDataBase)field_28179_b.get(s);
        if(mapdatabase != null)
        {
            return mapdatabase;
        }
        if(field_28180_a != null)
        {
            try
            {
                File file = field_28180_a.func_28111_b(s);
                if(file != null && file.exists())
                {
                    try
                    {
                        mapdatabase = (MapDataBase)class1.getConstructor(new Class[] {
                            java.lang.String.class
                        }).newInstance(new Object[] {
                            s
                        });
                    }
                    catch(Exception exception1)
                    {
                        throw new RuntimeException((new StringBuilder()).append("Failed to instantiate ").append(class1.toString()).toString(), exception1);
                    }
                    FileInputStream fileinputstream = new FileInputStream(file);
                    NBTTagCompound nbttagcompound = CompressedStreamTools.func_770_a(fileinputstream);
                    fileinputstream.close();
                    mapdatabase.func_28148_a(nbttagcompound.getCompoundTag("data"));
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        if(mapdatabase != null)
        {
            field_28179_b.put(s, mapdatabase);
            field_28182_c.add(mapdatabase);
        }
        return mapdatabase;
    }

    public void func_28177_a(String s, MapDataBase mapdatabase)
    {
        if(mapdatabase == null)
        {
            throw new RuntimeException("Can't set null data");
        }
        if(field_28179_b.containsKey(s))
        {
            field_28182_c.remove(field_28179_b.remove(s));
        }
        field_28179_b.put(s, mapdatabase);
        field_28182_c.add(mapdatabase);
    }

    public void func_28176_a()
    {
        for(int i = 0; i < field_28182_c.size(); i++)
        {
            MapDataBase mapdatabase = (MapDataBase)field_28182_c.get(i);
            if(mapdatabase.func_28150_b())
            {
                func_28175_a(mapdatabase);
                mapdatabase.func_28149_a(false);
            }
        }

    }

    private void func_28175_a(MapDataBase mapdatabase)
    {
        if(field_28180_a == null)
        {
            return;
        }
        try
        {
            File file = field_28180_a.func_28111_b(mapdatabase.field_28152_a);
            if(file != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                mapdatabase.func_28147_b(nbttagcompound);
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setCompoundTag("data", nbttagcompound);
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                CompressedStreamTools.writeGzippedCompoundToOutputStream(nbttagcompound1, fileoutputstream);
                fileoutputstream.close();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_28174_b()
    {
        try
        {
            field_28181_d.clear();
            if(field_28180_a == null)
            {
                return;
            }
            File file = field_28180_a.func_28111_b("idcounts");
            if(file != null && file.exists())
            {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_774_a(datainputstream);
                datainputstream.close();
                Iterator iterator = nbttagcompound.func_28107_c().iterator();
                do
                {
                    if(!iterator.hasNext())
                    {
                        break;
                    }
                    NBTBase nbtbase = (NBTBase)iterator.next();
                    if(nbtbase instanceof NBTTagShort)
                    {
                        NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
                        String s = nbttagshort.getKey();
                        short word0 = nbttagshort.shortValue;
                        field_28181_d.put(s, Short.valueOf(word0));
                    }
                } while(true);
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public int func_28173_a(String s)
    {
        Short short1 = (Short)field_28181_d.get(s);
        if(short1 == null)
        {
            short1 = Short.valueOf((short)0);
        } else
        {
            Short short2 = short1;
            Short short3 = short1 = Short.valueOf((short)(short1.shortValue() + 1));
            Short _tmp = short2;
        }
        field_28181_d.put(s, short1);
        if(field_28180_a == null)
        {
            return short1.shortValue();
        }
        try
        {
            File file = field_28180_a.func_28111_b("idcounts");
            if(file != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                String s1;
                short word0;
                for(Iterator iterator = field_28181_d.keySet().iterator(); iterator.hasNext(); nbttagcompound.setShort(s1, word0))
                {
                    s1 = (String)iterator.next();
                    word0 = ((Short)field_28181_d.get(s1)).shortValue();
                }

                DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
                CompressedStreamTools.func_771_a(nbttagcompound, dataoutputstream);
                dataoutputstream.close();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return short1.shortValue();
    }

    private ISaveHandler field_28180_a;
    private Map field_28179_b;
    private List field_28182_c;
    private Map field_28181_d;
}
