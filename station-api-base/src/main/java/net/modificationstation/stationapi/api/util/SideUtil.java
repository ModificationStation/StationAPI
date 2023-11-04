package net.modificationstation.stationapi.api.util;

import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

/**
 * Utility class to work with side-dependent objects and processes.
 * @author mine_diver
 */
public class SideUtil {
    /**
     * Side-dependent object choice.
     * @param clientObject the object to be returned on the client side.
     * @param serverObject the object to be returned on the server side.
     * @param <T> the type of the object to be chosen.
     * @return the object corresponding to the current side.
     */
    public static <T> T choose(T clientObject, T serverObject) {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> clientObject;
            case SERVER -> serverObject;
        };
    }

    /**
     * Side-dependent supplier execution.
     * @param clientSupplier the supplier that should be executed on client side.
     * @param serverSupplier the supplier that should be executed on server side.
     * @param <T> the supplier return type.
     * @return the supplier result.
     */
    @API
    public static <T> T get(Supplier<T> clientSupplier, Supplier<T> serverSupplier) {
        return choose(clientSupplier, serverSupplier).get();
    }

    /**
     * Side-dependent runnable execution.
     * @param clientRunnable the runnable that should be executed on client side.
     * @param serverRunnable the runnable that should be executed on server side.
     */
    @API
    public static void run(Runnable clientRunnable, Runnable serverRunnable) {
        choose(clientRunnable, serverRunnable).run();
    }
}
