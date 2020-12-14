package net.modificationstation.stationloader.api.common.registry;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.co.benjiweber.expressions.tuples.BiTuple;
import uk.co.benjiweber.expressions.tuples.Tuple;

import java.util.HashMap;
import java.util.Map;

public final class Identifier implements Comparable<Identifier> {


    private static final Map<String, Identifier> VALUES = new HashMap<>();
    @Getter
    private final ModID modID;
    @Getter
    private final String id;

    private Identifier(ModID modID, String id) {
        this.modID = modID;
        this.id = id;
    }

    @NotNull
    public static Identifier of(@NotNull String identifier) {
        return VALUES.computeIfAbsent(identifier, s -> {
            String[] strings = s.split(":");
            ModID modID;
            String id;
            if (strings.length == 1) {
                modID = ModID.of("minecraft");
                id = strings[0];
            } else {
                try {
                    String modid = strings[0];
                    modID = ModID.of(modid.trim());
                    id = s.substring(modid.length() + 1);
                } catch (NullPointerException e) {
                    modID = ModID.of("minecraft");
                    id = s;
                }
            }
            id = id.replace(":", "_").trim();
            BiTuple<ModID, String> tuple = Tuple.tuple(modID, id);
            return new Identifier(tuple.one(), tuple.two());
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
