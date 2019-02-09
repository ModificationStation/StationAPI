// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import net.minecraft.server.MinecraftServer;

// Referenced classes of package net.minecraft.src:
//            ServerGUI

class ServerGuiCommandListener
    implements ActionListener
{

    ServerGuiCommandListener(ServerGUI servergui, JTextField jtextfield)
    {
//        super();
        mcServerGui = servergui;
        textField = jtextfield;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = textField.getText().trim();
        if(s.length() > 0)
        {
            ServerGUI.getMinecraftServer(mcServerGui).addCommand(s, mcServerGui);
        }
        textField.setText("");
    }

    final JTextField textField; /* synthetic field */
    final ServerGUI mcServerGui; /* synthetic field */
}
