// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            MaterialTransparent, MapColor, MaterialLiquid, MaterialLogic, 
//            MaterialPortal

public class Material
{

    public Material(MapColor mapcolor)
    {
        field_31061_G = true;
        field_28131_A = mapcolor;
    }

    public boolean getIsLiquid()
    {
        return false;
    }

    public boolean isSolid()
    {
        return true;
    }

    public boolean getCanBlockGrass()
    {
        return true;
    }

    public boolean getIsSolid()
    {
        return true;
    }

    private Material func_28129_i()
    {
        isTranslucent = true;
        return this;
    }

    private Material func_31058_n()
    {
        field_31061_G = false;
        return this;
    }

    private Material setBurning()
    {
        canBurn = true;
        return this;
    }

    public boolean getBurning()
    {
        return canBurn;
    }

    public Material func_27089_f()
    {
        field_27091_A = true;
        return this;
    }

    public boolean func_27090_g()
    {
        return field_27091_A;
    }

    public boolean getIsOpaque()
    {
        if(isTranslucent)
        {
            return false;
        } else
        {
            return getIsSolid();
        }
    }

    public boolean func_31055_i()
    {
        return field_31061_G;
    }

    public int getMaterialMobility()
    {
        return mobilityFlag;
    }

    protected Material setNoPushMobility()
    {
        mobilityFlag = 1;
        return this;
    }

    protected Material setImmovableMobility()
    {
        mobilityFlag = 2;
        return this;
    }

    public static final Material air;
    public static final Material grass;
    public static final Material ground;
    public static final Material wood;
    public static final Material rock;
    public static final Material iron;
    public static final Material water;
    public static final Material lava;
    public static final Material leaves;
    public static final Material plants;
    public static final Material sponge;
    public static final Material cloth;
    public static final Material fire;
    public static final Material sand;
    public static final Material circuits;
    public static final Material glass;
    public static final Material tnt;
    public static final Material wug;
    public static final Material ice;
    public static final Material snow;
    public static final Material builtSnow;
    public static final Material cactus;
    public static final Material clay;
    public static final Material pumpkin;
    public static final Material portal;
    public static final Material cakeMaterial;
    public static final Material web;
    public static final Material piston;
    private boolean canBurn;
    private boolean field_27091_A;
    private boolean isTranslucent;
    public final MapColor field_28131_A;
    private boolean field_31061_G;
    private int mobilityFlag;

    static 
    {
        air = new MaterialTransparent(MapColor.field_28199_b);
        grass = new Material(MapColor.field_28198_c);
        ground = new Material(MapColor.field_28189_l);
        wood = (new Material(MapColor.field_28186_o)).setBurning();
        rock = (new Material(MapColor.field_28188_m)).func_31058_n();
        iron = (new Material(MapColor.field_28193_h)).func_31058_n();
        water = (new MaterialLiquid(MapColor.field_28187_n)).setNoPushMobility();
        lava = (new MaterialLiquid(MapColor.field_28195_f)).setNoPushMobility();
        leaves = (new Material(MapColor.field_28192_i)).setBurning().func_28129_i().setNoPushMobility();
        plants = (new MaterialLogic(MapColor.field_28192_i)).setNoPushMobility();
        sponge = new Material(MapColor.field_28196_e);
        cloth = (new Material(MapColor.field_28196_e)).setBurning();
        fire = (new MaterialTransparent(MapColor.field_28199_b)).setNoPushMobility();
        sand = new Material(MapColor.field_28197_d);
        circuits = (new MaterialLogic(MapColor.field_28199_b)).setNoPushMobility();
        glass = (new Material(MapColor.field_28199_b)).func_28129_i();
        tnt = (new Material(MapColor.field_28195_f)).setBurning().func_28129_i();
        wug = (new Material(MapColor.field_28192_i)).setNoPushMobility();
        ice = (new Material(MapColor.field_28194_g)).func_28129_i();
        snow = (new MaterialLogic(MapColor.field_28191_j)).func_27089_f().func_28129_i().func_31058_n().setNoPushMobility();
        builtSnow = (new Material(MapColor.field_28191_j)).func_31058_n();
        cactus = (new Material(MapColor.field_28192_i)).func_28129_i().setNoPushMobility();
        clay = new Material(MapColor.field_28190_k);
        pumpkin = (new Material(MapColor.field_28192_i)).setNoPushMobility();
        portal = (new MaterialPortal(MapColor.field_28199_b)).setImmovableMobility();
        cakeMaterial = (new Material(MapColor.field_28199_b)).setNoPushMobility();
        web = (new Material(MapColor.field_28196_e)).func_31058_n().setNoPushMobility();
        piston = (new Material(MapColor.field_28188_m)).setImmovableMobility();
    }
}
