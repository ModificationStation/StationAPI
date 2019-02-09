// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.*;
import java.util.Map;

// Referenced classes of package net.minecraft.src:
//            Session, StatFileWriter, ThreadStatSyncherReceive, ThreadStatSyncherSend

public class StatsSyncher
{

    public StatsSyncher(Session session, StatFileWriter statfilewriter, File file)
    {
        field_27438_a = false;
        field_27437_b = null;
        field_27436_c = null;
        field_27427_l = 0;
        field_27426_m = 0;
        field_27434_e = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.dat").toString());
        field_27433_f = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".dat").toString());
        field_27430_i = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.old").toString());
        field_27429_j = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".old").toString());
        field_27432_g = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append("_unsent.tmp").toString());
        field_27431_h = new File(file, (new StringBuilder()).append("stats_").append(session.username.toLowerCase()).append(".tmp").toString());
        if(!session.username.toLowerCase().equals(session.username))
        {
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.dat").toString(), field_27434_e);
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".dat").toString(), field_27433_f);
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.old").toString(), field_27430_i);
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".old").toString(), field_27429_j);
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append("_unsent.tmp").toString(), field_27432_g);
            func_28214_a(file, (new StringBuilder()).append("stats_").append(session.username).append(".tmp").toString(), field_27431_h);
        }
        field_27435_d = statfilewriter;
        field_27428_k = session;
        if(field_27434_e.exists())
        {
            statfilewriter.func_27179_a(func_27415_a(field_27434_e, field_27432_g, field_27430_i));
        }
        func_27418_a();
    }

    private void func_28214_a(File file, String s, File file1)
    {
        File file2 = new File(file, s);
        if(file2.exists() && !file2.isDirectory() && !file1.exists())
        {
            file2.renameTo(file1);
        }
    }

    private Map func_27415_a(File file, File file1, File file2)
    {
        if(file.exists())
        {
            return func_27408_a(file);
        }
        if(file2.exists())
        {
            return func_27408_a(file2);
        }
        if(file1.exists())
        {
            return func_27408_a(file1);
        } else
        {
            return null;
        }
    }

    private Map func_27408_a(File file)
    {
        BufferedReader bufferedreader = null;
        try
        {
            bufferedreader = new BufferedReader(new FileReader(file));
            String s = "";
            StringBuilder stringbuilder = new StringBuilder();
            while((s = bufferedreader.readLine()) != null) 
            {
                stringbuilder.append(s);
            }
            Map map = StatFileWriter.func_27177_a(stringbuilder.toString());
            return map;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            if(bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch(Exception exception2)
                {
                    exception2.printStackTrace();
                }
            }
        }
        return null;
    }

    private void func_27410_a(Map map, File file, File file1, File file2)
        throws IOException
    {
        PrintWriter printwriter = new PrintWriter(new FileWriter(file1, false));
        try
        {
            printwriter.print(StatFileWriter.func_27185_a(field_27428_k.username, "local", map));
        }
        finally
        {
            printwriter.close();
        }
        if(file2.exists())
        {
            file2.delete();
        }
        if(file.exists())
        {
            file.renameTo(file2);
        }
        file1.renameTo(file);
    }

    public void func_27418_a()
    {
        if(field_27438_a)
        {
            throw new IllegalStateException("Can't get stats from server while StatsSyncher is busy!");
        } else
        {
            field_27427_l = 100;
            field_27438_a = true;
            (new ThreadStatSyncherReceive(this)).start();
            return;
        }
    }

    public void func_27424_a(Map map)
    {
        if(field_27438_a)
        {
            throw new IllegalStateException("Can't save stats while StatsSyncher is busy!");
        } else
        {
            field_27427_l = 100;
            field_27438_a = true;
            (new ThreadStatSyncherSend(this, map)).start();
            return;
        }
    }

    public void syncStatsFileWithMap(Map map)
    {
        for(int i = 30; field_27438_a && --i > 0;)
        {
            try
            {
                Thread.sleep(100L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }

        field_27438_a = true;
        try
        {
            func_27410_a(map, field_27434_e, field_27432_g, field_27430_i);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            field_27438_a = false;
        }
    }

    public boolean func_27420_b()
    {
        return field_27427_l <= 0 && !field_27438_a && field_27436_c == null;
    }

    public void func_27425_c()
    {
        if(field_27427_l > 0)
        {
            field_27427_l--;
        }
        if(field_27426_m > 0)
        {
            field_27426_m--;
        }
        if(field_27436_c != null)
        {
            field_27435_d.func_27187_c(field_27436_c);
            field_27436_c = null;
        }
        if(field_27437_b != null)
        {
            field_27435_d.func_27180_b(field_27437_b);
            field_27437_b = null;
        }
    }

    static Map func_27422_a(StatsSyncher statssyncher)
    {
        return statssyncher.field_27437_b;
    }

    static File func_27423_b(StatsSyncher statssyncher)
    {
        return statssyncher.field_27433_f;
    }

    static File func_27411_c(StatsSyncher statssyncher)
    {
        return statssyncher.field_27431_h;
    }

    static File func_27413_d(StatsSyncher statssyncher)
    {
        return statssyncher.field_27429_j;
    }

    static void func_27412_a(StatsSyncher statssyncher, Map map, File file, File file1, File file2)
        throws IOException
    {
        statssyncher.func_27410_a(map, file, file1, file2);
    }

    static Map func_27421_a(StatsSyncher statssyncher, Map map)
    {
        return statssyncher.field_27437_b = map;
    }

    static Map func_27409_a(StatsSyncher statssyncher, File file, File file1, File file2)
    {
        return statssyncher.func_27415_a(file, file1, file2);
    }

    static boolean func_27416_a(StatsSyncher statssyncher, boolean flag)
    {
        return statssyncher.field_27438_a = flag;
    }

    static File func_27414_e(StatsSyncher statssyncher)
    {
        return statssyncher.field_27434_e;
    }

    static File func_27417_f(StatsSyncher statssyncher)
    {
        return statssyncher.field_27432_g;
    }

    static File func_27419_g(StatsSyncher statssyncher)
    {
        return statssyncher.field_27430_i;
    }

    private volatile boolean field_27438_a;
    private volatile Map field_27437_b;
    private volatile Map field_27436_c;
    private StatFileWriter field_27435_d;
    private File field_27434_e;
    private File field_27433_f;
    private File field_27432_g;
    private File field_27431_h;
    private File field_27430_i;
    private File field_27429_j;
    private Session field_27428_k;
    private int field_27427_l;
    private int field_27426_m;
}
