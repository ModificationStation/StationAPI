package net.modificationstation.stationapi.api.util;

import org.jetbrains.annotations.NotNull;

/**
 * Some null trickery.
 * @author mine_diver
 */
public class Null {

    /**
     * Returns null with {@link NotNull} annotation applied in order to fool IntelliJ into thinking that it's not actually a null.
     * Useful when you want your entrypoint fields to be final but don't want IntelliJ to scream at you for using a final field that is set to null.
     * @param <V> is used to avoid casting.
     * @return null.
     */
    //@NotNull
    public static <V> V get() {
        //noinspection ConstantConditions
        return null;
    }
}
