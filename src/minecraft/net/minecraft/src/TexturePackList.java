// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.util.*;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            TexturePackDefault, GameSettings, TexturePackBase, TexturePackCustom

public class TexturePackList
{

    public TexturePackList(Minecraft minecraft, File file)
    {
        availableTexturePacks = new ArrayList();
        defaultTexturePack = new TexturePackDefault();
        field_6538_d = new HashMap();
        mc = minecraft;
        texturePackDir = new File(file, "texturepacks");
        if(!texturePackDir.exists())
        {
            texturePackDir.mkdirs();
        }
        currentTexturePack = minecraft.gameSettings.skin;
        updateAvaliableTexturePacks();
        selectedTexturePack.func_6482_a();
    }

    public boolean setTexturePack(TexturePackBase texturepackbase)
    {
        if(texturepackbase == selectedTexturePack)
        {
            return false;
        } else
        {
            selectedTexturePack.closeTexturePackFile();
            currentTexturePack = texturepackbase.texturePackFileName;
            selectedTexturePack = texturepackbase;
            mc.gameSettings.skin = currentTexturePack;
            mc.gameSettings.saveOptions();
            selectedTexturePack.func_6482_a();
            return true;
        }
    }

    public void updateAvaliableTexturePacks()
    {
        ArrayList arraylist = new ArrayList();
        selectedTexturePack = null;
        arraylist.add(defaultTexturePack);
        if(texturePackDir.exists() && texturePackDir.isDirectory())
        {
            File afile[] = texturePackDir.listFiles();
            File afile1[] = afile;
            int i = afile1.length;
            for(int j = 0; j < i; j++)
            {
                File file = afile1[j];
                if(!file.isFile() || !file.getName().toLowerCase().endsWith(".zip"))
                {
                    continue;
                }
                String s = (new StringBuilder()).append(file.getName()).append(":").append(file.length()).append(":").append(file.lastModified()).toString();
                try
                {
                    if(!field_6538_d.containsKey(s))
                    {
                        TexturePackCustom texturepackcustom = new TexturePackCustom(file);
                        texturepackcustom.field_6488_d = s;
                        field_6538_d.put(s, texturepackcustom);
                        texturepackcustom.func_6485_a(mc);
                    }
                    TexturePackBase texturepackbase1 = (TexturePackBase)field_6538_d.get(s);
                    if(texturepackbase1.texturePackFileName.equals(currentTexturePack))
                    {
                        selectedTexturePack = texturepackbase1;
                    }
                    arraylist.add(texturepackbase1);
                }
                catch(IOException ioexception)
                {
                    ioexception.printStackTrace();
                }
            }

        }
        if(selectedTexturePack == null)
        {
            selectedTexturePack = defaultTexturePack;
        }
        availableTexturePacks.removeAll(arraylist);
        TexturePackBase texturepackbase;
        for(Iterator iterator = availableTexturePacks.iterator(); iterator.hasNext(); field_6538_d.remove(texturepackbase.field_6488_d))
        {
            texturepackbase = (TexturePackBase)iterator.next();
            texturepackbase.func_6484_b(mc);
        }

        availableTexturePacks = arraylist;
    }

    public List availableTexturePacks()
    {
        return new ArrayList(availableTexturePacks);
    }

    private List availableTexturePacks;
    private TexturePackBase defaultTexturePack;
    public TexturePackBase selectedTexturePack;
    private Map field_6538_d;
    private Minecraft mc;
    private File texturePackDir;
    private String currentTexturePack;
}
