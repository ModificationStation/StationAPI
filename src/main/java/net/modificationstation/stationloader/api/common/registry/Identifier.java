package net.modificationstation.stationloader.api.common.registry;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.co.benjiweber.expressions.tuples.BiTuple;
import uk.co.benjiweber.expressions.tuples.Tuple;

import java.util.HashMap;
import java.util.Map;

public final class Identifier implements Comparable<Identifier> {

    @NotNull
    public static Identifier of(@NotNull ModID modid, @NotNull String id) {
        return of(modid.toString(), id);
    }

    @NotNull
    public static Identifier of(@NotNull String namespace, @NotNull String id) {
        return of(Tuple.tuple(namespace, id));
    }

    @NotNull
    public static Identifier of(@NotNull BiTuple<String, String> identifier) {
        return of(identifier.one() + ":" + identifier.two());
    }

    @NotNull
    public static Identifier of(@NotNull String identifier) {
        return VALUES.computeIfAbsent(identifier, Identifier::new);
    }

    private Identifier(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    private Identifier(BiTuple<String, String> identifier) {
        this(identifier.one(), identifier.two());
    }

    private Identifier(String identifier) {
        this(separate(identifier));
    }

    @NotNull
    public static BiTuple<String, String> separate(String identifier) {
        String[] strings = identifier.split(":");
        String namespace, id;
        switch(strings.length) {
            case 1: {
                namespace = "minecraft";
                id = strings[0].trim();
                break;
            }
            case 2: {
                namespace = strings[0].trim();
                id = strings[1].trim();
                break;
            }
            default: {
                throw new IllegalArgumentException("Wrong string identifier!");
            }
        }
        return Tuple.tuple(namespace, id);
    }

    @Override
    public int compareTo(@NotNull Identifier o) {
        return toString().compareTo(o.toString());
    }

    @NotNull
    @Override
    public String toString() {
        return namespace + ":" + id;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof String) {
            return other.equals(this.toString());
        } else if (other instanceof Identifier) {
            Identifier otherId = (Identifier) other;
            return otherId.id.equals(id) && otherId.namespace.equals(namespace);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = 5;
        result = 29 * result + namespace.hashCode();
        result = 29 * result + id.hashCode();
        return result;
    }

    @Getter
    private final String
            namespace,
            id;

    private static final Map<String, Identifier> VALUES = new HashMap<>();
}
