package net.modificationstation.stationapi.api.common.registry;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Identifier implements Comparable<Identifier> {


    private static final Map<String, Identifier> VALUES = new HashMap<>();
    public final ModID modID;
    public final String id;

    private Identifier(ModID modID, String id) {
        this.modID = modID;
        this.id = id;
    }

    @NotNull
    public static Identifier of(@NotNull String identifier) {
        return VALUES.computeIfAbsent(identifier, s -> {
            String[] strings = s.split(":");
            String modid;
            int idIndex;
            switch(strings.length) {
                case 1: {
                    modid = "minecraft";
                    idIndex = 0;
                    break;
                }
                case 2: {
                    modid = strings[0];
                    idIndex = 1;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Invalid identifier string!");
            }
            return new Identifier(ModID.of(modid.trim()), strings[idIndex].trim());
        });
    }

    @NotNull
    public static Identifier of(@NotNull ModID modID, @NotNull String id) {
        return VALUES.computeIfAbsent(modID + ":" + id, s -> new Identifier(modID, id));
    }

    @Override
    public int compareTo(@NotNull Identifier o) {
        return toString().compareTo(o.toString());
    }

    @NotNull
    @Override
    public String toString() {
        return modID + ":" + id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof String) {
            return other.equals(this.toString());
        } else if (other instanceof Identifier) {
            Identifier otherId = (Identifier) other;
            return otherId.id.equals(id) && otherId.modID.equals(modID);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 5;
        result = 29 * result + modID.hashCode();
        result = 29 * result + id.hashCode();
        return result;
    }
}
