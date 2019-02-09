// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            IProgressUpdate

public interface ISaveFormat
{

    public abstract boolean isOldSaveType(String s);

    public abstract boolean converMapToMCRegion(String s, IProgressUpdate iprogressupdate);
}
