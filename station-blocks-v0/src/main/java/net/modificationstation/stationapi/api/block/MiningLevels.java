package net.modificationstation.stationapi.api.block;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

import java.util.HashMap;

public class MiningLevels {
    //TODO : Add an event to register mining levels

    public static final Identifier WOOD = ModID.MINECRAFT.id("wood");
    public static final Identifier STONE = ModID.MINECRAFT.id("stone");
    public static final Identifier IRON = ModID.MINECRAFT.id("iron");
    public static final Identifier DIAMOND = ModID.MINECRAFT.id("diamond");

    private static final HashMap<Identifier, Integer> miningLevels = new HashMap<>();

    // Add Vanilla mining levels
    static {
        miningLevels.put(ModID.MINECRAFT.id("wood"),0);
        miningLevels.put(ModID.MINECRAFT.id("stone"),1);
        miningLevels.put(ModID.MINECRAFT.id("iron"),2);
        miningLevels.put(ModID.MINECRAFT.id("diamond"),3);
    }

    public static boolean addMiningLevel(Identifier identifier, int miningLevel){
        if(!miningLevels.containsKey(identifier)){
            miningLevels.put(identifier,miningLevel);
            return true;
        }
        return false;
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

}
