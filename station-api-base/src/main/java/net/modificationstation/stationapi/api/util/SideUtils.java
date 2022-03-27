package net.modificationstation.stationapi.api.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

/**
 * Utility class to work with side-dependent objects and processes.
 * @author mine_diver
 */
public class SideUtils {

    /**
     * Side-dependent supplier execution.
     * @param clientSupplier the supplier that should be executed on client side.
     * @param serverSupplier the supplier that should be executed on server side.
     * @param <T> the supplier return type.
     * @return the supplier result.
     */
    @API
    public static <T> T get(Supplier<T> clientSupplier, Supplier<T> serverSupplier) {
        EnvType side = FabricLoader.getInstance().getEnvironmentType();
        return switch (side) {
            case CLIENT -> clientSupplier.get();
            case SERVER -> serverSupplier.get();
        };
    }

    /**
     * Side-dependent runnable execution.
     * @param clientRunnable the runnable that should be executed on client side.
     * @param serverRunnable the runnable that should be executed on server side.
     */
    @API
    public static void run(Runnable clientRunnable, Runnable serverRunnable) {
        EnvType side = FabricLoader.getInstance().getEnvironmentType();
        switch (side) {
            case CLIENT -> clientRunnable.run();
            case SERVER -> serverRunnable.run();
            default -> throw new IllegalArgumentException("Unknown side " + side + "!");
        }
    }
}
