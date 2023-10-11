package net.modificationstation.stationapi.api.block;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class MiningLevels {
    // Vanilla Mining Levels
    public static final Identifier WOOD = ModID.MINECRAFT.id("wood");
    public static final Identifier STONE = ModID.MINECRAFT.id("stone");
    public static final Identifier IRON = ModID.MINECRAFT.id("iron");
    public static final Identifier DIAMOND = ModID.MINECRAFT.id("diamond");

    // Mining Level HashMaps
    private static final HashMap<Identifier, Integer> miningLevels = new HashMap<>();
    private static final HashSet<Identifier> staticMiningLevels = new HashSet<>();

    // Add Vanilla mining levels
    static {
        miningLevels.put(ModID.MINECRAFT.id("wood"),0);
        miningLevels.put(ModID.MINECRAFT.id("stone"),1);
        miningLevels.put(ModID.MINECRAFT.id("iron"),2);
        miningLevels.put(ModID.MINECRAFT.id("diamond"),3);
    }

    /**
     * Adds a mining level with a set number
     * @param identifier Identifier of the Mining Level
     * @param miningLevel Mining Level
     * @param isStatic If the mining level is static (cannot be shifted around)
     * @return Whether it was added
     */
    public static boolean addMiningLevel(Identifier identifier, int miningLevel, boolean isStatic){
        if(!doesExist(identifier)){
            miningLevels.put(identifier, miningLevel);
            if(isStatic){
                staticMiningLevels.add(identifier);
            }
            return true;
        }
        return false;
    }

    /**
     * Adds a mining level with a set number which CANNOT be shifted around
     * @param identifier Identifier of the Mining Level
     * @param miningLevel Mining Level
     * @return Whether it was added
     */
    public static boolean addStaticMiningLevel(Identifier identifier, int miningLevel){
        return addMiningLevel(identifier, miningLevel, true);
    }

    /**
     * Inserts a mining level with a set number which CAN be shifted around
     * @param identifier Identifier of the Mining Level
     * @param miningLevel Mining Level
     * @return Whether it was added
     */
    public static boolean addMiningLevel(Identifier identifier, int miningLevel){
        return addMiningLevel(identifier, miningLevel, false);
    }


    /**
     * Adds a mining level before the specified mining level,
     * if the resulting mining level would be negative, it will become 0
     * @param identifier Identifier of the mining level to add
     * @param before Identifier of the mining level to add befor
     * @return Whether it was added
     */
    public static boolean addMiningLevelBefore(Identifier identifier, Identifier before){
        if(!doesExist(before)){
            return false;
        }
        if(getMiningLevel(before) <= 0){
            addMiningLevel(identifier, 0, false);
        }else{
            addMiningLevel(identifier, getMiningLevel(before)-1, false);
        }
        return true;
    }

    public static boolean addMiningLevelAfter(Identifier identifier, Identifier after){
        if(!doesExist(after)){
            return false;
        }
        addMiningLevel(identifier, getMiningLevel(after)+1, false);
        return true;
    }

    public static boolean insertMiningLevelAfter(Identifier identifier, Identifier after){
        if(doesExist(identifier) || !doesExist(after)){
            return false;
        }

        shiftMiningLevelsAfterUp(after);
        addMiningLevel(identifier,getMiningLevel(after)+1, false);
        return true;
    }

    public static boolean insertMiningLevelBefore(Identifier identifier, Identifier before){
        if(doesExist(identifier) || !doesExist(before)){
            return false;
        }

        shiftMiningLevelsUp(before);
        addMiningLevel(identifier,getMiningLevel(before)-1, false);
        return true;
    }

    /**
     * Shifts non-static mining levels which are higher than the specified level up by 1
     * @param identifier Mining Levels higher than this will be shifted
     */
    private static void shiftMiningLevelsAfterUp(Identifier identifier){
        int parentMiningLevel = getMiningLevel(identifier);

        for(var item : miningLevels.entrySet()){
            if(!staticMiningLevels.contains(item.getKey())){
                if(item.getValue() >= parentMiningLevel+1){
                    item.setValue(item.getValue()+1);
                }
            }
        }
    }

    /**
     * Shifts non-static mining levels which are equal or higher than the specified level up by 1
     * @param identifier Mining Levels higher than this will be shifted
     */
    private static void shiftMiningLevelsUp(Identifier identifier){


        /*
        int parentMiningLevel = getMiningLevel(identifier);

        for(var item : miningLevels.entrySet()){
            if(!staticMiningLevels.contains(item.getKey())){
                if(item.getValue() >= parentMiningLevel){
                    item.setValue(item.getValue()+1);
                }
            }
        }
        */
    }

    /**
     * Allows to get mining level int using an Mining Level Identifier (for example "minecraft:iron")
     * @param identifier The Identifier to lookup
     * @return The mining level, will return 0 if it was not found
     */
    public static int getMiningLevel(Identifier identifier){
        return miningLevels.getOrDefault(identifier, 0);
    }

    /**
     * Allows to get mining level int using the mining level Id (for example "iron"), ignoring the mod
     * @param id The id to look for
     * @return The mining level, will return 0 if it was not found
     */
    public static int getMiningLevel(String id){
        for(var item : miningLevels.entrySet()){
            if(item.getKey().id.equals(id)){
                return item.getValue();
            }
        }
        return 0;
    }

    public static boolean doesExist(Identifier identifier){
        return miningLevels.containsKey(identifier);
    }

    public static boolean doesExist(int miningLevel){
        for(var item : miningLevels.values()){
            if(item == miningLevel){
                return true;
            }
        }
        return false;
    }

    public static void printMiningLevels(){
        System.out.println("Mining Level Registry :");
        for(var item : miningLevels.entrySet()){
            System.out.println(item.getKey() + " - " + item.getValue());
        }
    }
}
