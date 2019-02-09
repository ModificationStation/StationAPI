// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.Timer;

// Referenced classes of package net.minecraft.src:
//            GuiStatsListener, NetworkManager

public class GuiStatsComponent extends JComponent
{

    public GuiStatsComponent()
    {
        memoryUse = new int[256];
        updateCounter = 0;
        displayStrings = new String[10];
        setPreferredSize(new Dimension(256, 196));
        setMinimumSize(new Dimension(256, 196));
        setMaximumSize(new Dimension(256, 196));
        (new Timer(500, new GuiStatsListener(this))).start();
        setBackground(Color.BLACK);
    }

    private void updateStats()
    {
        long l = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        displayStrings[0] = (new StringBuilder()).append("Memory use: ").append(l / 1024L / 1024L).append(" mb (").append((Runtime.getRuntime().freeMemory() * 100L) / Runtime.getRuntime().maxMemory()).append("% free)").toString();
        displayStrings[1] = (new StringBuilder()).append("Threads: ").append(NetworkManager.numReadThreads).append(" + ").append(NetworkManager.numWriteThreads).toString();
        memoryUse[updateCounter++ & 0xff] = (int)((l * 100L) / Runtime.getRuntime().maxMemory());
        repaint();
    }

    public void paint(Graphics g)
    {
        g.setColor(new Color(0xffffff));
        g.fillRect(0, 0, 256, 192);
        for(int i = 0; i < 256; i++)
        {
            int k = memoryUse[i + updateCounter & 0xff];
            g.setColor(new Color(k + 28 << 16));
            g.fillRect(i, 100 - k, 1, k);
        }

        g.setColor(Color.BLACK);
        for(int j = 0; j < displayStrings.length; j++)
        {
            String s = displayStrings[j];
            if(s != null)
            {
                g.drawString(s, 32, 116 + j * 16);
            }
        }

    }

    static void update(GuiStatsComponent guistatscomponent)
    {
        guistatscomponent.updateStats();
    }

    private int memoryUse[];
    private int updateCounter;
    private String displayStrings[];
}
