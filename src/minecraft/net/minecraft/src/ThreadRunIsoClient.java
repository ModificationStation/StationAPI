// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            CanvasIsomPreview

class ThreadRunIsoClient extends Thread
{

    ThreadRunIsoClient(CanvasIsomPreview canvasisompreview)
    {
//        super();
        isoCanvas = canvasisompreview;
    }

    public void run()
    {
        while(CanvasIsomPreview.isRunning(isoCanvas)) 
        {
            isoCanvas.showNextBuffer();
            try
            {
                Thread.sleep(1L);
            }
            catch(Exception exception) { }
        }
    }

    final CanvasIsomPreview isoCanvas; /* synthetic field */
}
