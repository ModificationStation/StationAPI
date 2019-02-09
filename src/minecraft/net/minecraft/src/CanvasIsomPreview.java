// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            OsMap, EnumOS1, IsoImageBuffer, World, 
//            SaveHandler, ThreadRunIsoClient, TerrainTextureManager, ChunkCoordinates

public class CanvasIsomPreview extends Canvas
    implements KeyListener, MouseListener, MouseMotionListener, Runnable
{

    public File getMinecraftDir()
    {
        if(dataFolder == null)
        {
            dataFolder = getAppDir("minecraft");
        }
        return dataFolder;
    }

    public File getAppDir(String s)
    {
        String s1 = System.getProperty("user.home", ".");
        File file;
        switch(OsMap.field_1193_a[getOs().ordinal()])
        {
        case 1: // '\001'
        case 2: // '\002'
            file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            break;

        case 3: // '\003'
            String s2 = System.getenv("APPDATA");
            if(s2 != null)
            {
                file = new File(s2, (new StringBuilder()).append(".").append(s).append('/').toString());
            } else
            {
                file = new File(s1, (new StringBuilder()).append('.').append(s).append('/').toString());
            }
            break;

        case 4: // '\004'
            file = new File(s1, (new StringBuilder()).append("Library/Application Support/").append(s).toString());
            break;

        default:
            file = new File(s1, (new StringBuilder()).append(s).append('/').toString());
            break;
        }
        if(!file.exists() && !file.mkdirs())
        {
            throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        } else
        {
            return file;
        }
    }

    private static EnumOS1 getOs()
    {
        String s = System.getProperty("os.name").toLowerCase();
        if(s.contains("win"))
        {
            return EnumOS1.windows;
        }
        if(s.contains("mac"))
        {
            return EnumOS1.macos;
        }
        if(s.contains("solaris"))
        {
            return EnumOS1.solaris;
        }
        if(s.contains("sunos"))
        {
            return EnumOS1.solaris;
        }
        if(s.contains("linux"))
        {
            return EnumOS1.linux;
        }
        if(s.contains("unix"))
        {
            return EnumOS1.linux;
        } else
        {
            return EnumOS1.unknown;
        }
    }

    public CanvasIsomPreview()
    {
        field_1793_a = 0;
        zoomLevel = 2;
        displayHelpText = true;
        running = true;
        imageBufferList = Collections.synchronizedList(new LinkedList());
        imageBuffers = new IsoImageBuffer[64][64];
        dataFolder = getMinecraftDir();
        for(int i = 0; i < 64; i++)
        {
            for(int j = 0; j < 64; j++)
            {
                imageBuffers[i][j] = new IsoImageBuffer(null, i, j);
            }

        }

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        setBackground(Color.red);
    }

    public void loadWorld(String s)
    {
        field_1785_i = field_1784_j = 0;
        worldObj = new World(new SaveHandler(new File(dataFolder, "saves"), s, false), s, (new Random()).nextLong());
        worldObj.skylightSubtracted = 0;
        synchronized(imageBufferList)
        {
            imageBufferList.clear();
            for(int i = 0; i < 64; i++)
            {
                for(int j = 0; j < 64; j++)
                {
                    imageBuffers[i][j].func_888_a(worldObj, i, j);
                }

            }

        }
    }

    private void setTimeOfDay(int i)
    {
        synchronized(imageBufferList)
        {
            worldObj.skylightSubtracted = i;
            imageBufferList.clear();
            for(int j = 0; j < 64; j++)
            {
                for(int k = 0; k < 64; k++)
                {
                    imageBuffers[j][k].func_888_a(worldObj, j, k);
                }

            }

        }
    }

    public void func_1272_b()
    {
        (new ThreadRunIsoClient(this)).start();
        for(int i = 0; i < 8; i++)
        {
            (new Thread(this)).start();
        }

    }

    public void exit()
    {
        running = false;
    }

    private IsoImageBuffer getImageBuffer(int i, int j)
    {
        int k = i & 0x3f;
        int l = j & 0x3f;
        IsoImageBuffer isoimagebuffer = imageBuffers[k][l];
        if(isoimagebuffer.field_1354_c == i && isoimagebuffer.field_1353_d == j)
        {
            return isoimagebuffer;
        }
        synchronized(imageBufferList)
        {
            imageBufferList.remove(isoimagebuffer);
        }
        isoimagebuffer.func_889_a(i, j);
        return isoimagebuffer;
    }

    public void run()
    {
        TerrainTextureManager terraintexturemanager = new TerrainTextureManager();
        while(running) 
        {
            IsoImageBuffer isoimagebuffer = null;
            synchronized(imageBufferList)
            {
                if(imageBufferList.size() > 0)
                {
                    isoimagebuffer = (IsoImageBuffer)imageBufferList.remove(0);
                }
            }
            if(isoimagebuffer != null)
            {
                if(field_1793_a - isoimagebuffer.field_1350_g < 2)
                {
                    terraintexturemanager.func_799_a(isoimagebuffer);
                    repaint();
                } else
                {
                    isoimagebuffer.field_1349_h = false;
                }
            }
            try
            {
                Thread.sleep(2L);
            }
            catch(InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
    }

    public void update(Graphics g)
    {
    }

    public void paint(Graphics g)
    {
    }

    public void showNextBuffer()
    {
        BufferStrategy bufferstrategy = getBufferStrategy();
        if(bufferstrategy == null)
        {
            createBufferStrategy(2);
            return;
        } else
        {
            drawScreen((Graphics2D)bufferstrategy.getDrawGraphics());
            bufferstrategy.show();
            return;
        }
    }

    public void drawScreen(Graphics2D graphics2d)
    {
        field_1793_a++;
        java.awt.geom.AffineTransform affinetransform = graphics2d.getTransform();
        graphics2d.setClip(0, 0, getWidth(), getHeight());
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2d.translate(getWidth() / 2, getHeight() / 2);
        graphics2d.scale(zoomLevel, zoomLevel);
        graphics2d.translate(field_1785_i, field_1784_j);
        if(worldObj != null)
        {
            ChunkCoordinates chunkcoordinates = worldObj.getSpawnPoint();
            graphics2d.translate(-(chunkcoordinates.x + chunkcoordinates.z), -(-chunkcoordinates.x + chunkcoordinates.z) + 64);
        }
        Rectangle rectangle = graphics2d.getClipBounds();
        graphics2d.setColor(new Color(0xff101020));
        graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        byte byte0 = 16;
        byte byte1 = 3;
        int i = rectangle.x / byte0 / 2 - 2 - byte1;
        int j = (rectangle.x + rectangle.width) / byte0 / 2 + 1 + byte1;
        int k = rectangle.y / byte0 - 1 - byte1 * 2;
        int l = (rectangle.y + rectangle.height + 16 + 128) / byte0 + 1 + byte1 * 2;
        for(int i1 = k; i1 <= l; i1++)
        {
            for(int k1 = i; k1 <= j; k1++)
            {
                int l1 = k1 - (i1 >> 1);
                int i2 = k1 + (i1 + 1 >> 1);
                IsoImageBuffer isoimagebuffer = getImageBuffer(l1, i2);
                isoimagebuffer.field_1350_g = field_1793_a;
                if(!isoimagebuffer.field_1352_e)
                {
                    if(!isoimagebuffer.field_1349_h)
                    {
                        isoimagebuffer.field_1349_h = true;
                        imageBufferList.add(isoimagebuffer);
                    }
                    continue;
                }
                isoimagebuffer.field_1349_h = false;
                if(!isoimagebuffer.field_1351_f)
                {
                    int j2 = k1 * byte0 * 2 + (i1 & 1) * byte0;
                    int k2 = i1 * byte0 - 128 - 16;
                    graphics2d.drawImage(isoimagebuffer.field_1348_a, j2, k2, null);
                }
            }

        }

        if(displayHelpText)
        {
            graphics2d.setTransform(affinetransform);
            int j1 = getHeight() - 32 - 4;
            graphics2d.setColor(new Color(0x80000000, true));
            graphics2d.fillRect(4, getHeight() - 32 - 4, getWidth() - 8, 32);
            graphics2d.setColor(Color.WHITE);
            String s = "F1 - F5: load levels   |   0-9: Set time of day   |   Space: return to spawn   |   Double click: zoom   |   Escape: hide this text";
            graphics2d.drawString(s, getWidth() / 2 - graphics2d.getFontMetrics().stringWidth(s) / 2, j1 + 20);
        }
        graphics2d.dispose();
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
        int i = mouseevent.getX() / zoomLevel;
        int j = mouseevent.getY() / zoomLevel;
        field_1785_i += i - xPosition;
        field_1784_j += j - yPosition;
        xPosition = i;
        yPosition = j;
        repaint();
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getClickCount() == 2)
        {
            zoomLevel = 3 - zoomLevel;
            repaint();
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
        int i = mouseevent.getX() / zoomLevel;
        int j = mouseevent.getY() / zoomLevel;
        xPosition = i;
        yPosition = j;
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void keyPressed(KeyEvent keyevent)
    {
        if(keyevent.getKeyCode() == 48)
        {
            setTimeOfDay(11);
        }
        if(keyevent.getKeyCode() == 49)
        {
            setTimeOfDay(10);
        }
        if(keyevent.getKeyCode() == 50)
        {
            setTimeOfDay(9);
        }
        if(keyevent.getKeyCode() == 51)
        {
            setTimeOfDay(7);
        }
        if(keyevent.getKeyCode() == 52)
        {
            setTimeOfDay(6);
        }
        if(keyevent.getKeyCode() == 53)
        {
            setTimeOfDay(5);
        }
        if(keyevent.getKeyCode() == 54)
        {
            setTimeOfDay(3);
        }
        if(keyevent.getKeyCode() == 55)
        {
            setTimeOfDay(2);
        }
        if(keyevent.getKeyCode() == 56)
        {
            setTimeOfDay(1);
        }
        if(keyevent.getKeyCode() == 57)
        {
            setTimeOfDay(0);
        }
        if(keyevent.getKeyCode() == 112)
        {
            loadWorld("World1");
        }
        if(keyevent.getKeyCode() == 113)
        {
            loadWorld("World2");
        }
        if(keyevent.getKeyCode() == 114)
        {
            loadWorld("World3");
        }
        if(keyevent.getKeyCode() == 115)
        {
            loadWorld("World4");
        }
        if(keyevent.getKeyCode() == 116)
        {
            loadWorld("World5");
        }
        if(keyevent.getKeyCode() == 32)
        {
            field_1785_i = field_1784_j = 0;
        }
        if(keyevent.getKeyCode() == 27)
        {
            displayHelpText = !displayHelpText;
        }
        repaint();
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    static boolean isRunning(CanvasIsomPreview canvasisompreview)
    {
        return canvasisompreview.running;
    }

    private int field_1793_a;
    private int zoomLevel;
    private boolean displayHelpText;
    private World worldObj;
    private File dataFolder;
    private boolean running;
    private java.util.List imageBufferList;
    private IsoImageBuffer imageBuffers[][];
    private int field_1785_i;
    private int field_1784_j;
    private int xPosition;
    private int yPosition;
}
