// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            EntityPlayer

public class MovementInput
{

    public MovementInput()
    {
        moveStrafe = 0.0F;
        moveForward = 0.0F;
        field_1177_c = false;
        jump = false;
        sneak = false;
    }

    public void updatePlayerMoveState(EntityPlayer entityplayer)
    {
    }

    public void resetKeyState()
    {
    }

    public void checkKeyForMovementInput(int i, boolean flag)
    {
    }

    public float moveStrafe;
    public float moveForward;
    public boolean field_1177_c;
    public boolean jump;
    public boolean sneak;
}
