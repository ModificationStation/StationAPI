package net.modificationstation.stationapi.impl.resource;

import java.util.function.Consumer;

/**
 * A resource pack provider provides {@link ResourcePackProfile}s, usually to
 * {@link ResourcePackManager}s.
 */
public interface ResourcePackProvider {
    /**
     * Register resource pack profiles created with the {@code factory} to the
     * {@code profileAdder}.
     * 
     * @see ResourcePackProfile#of
     * 
     * @param profileAdder the profile adder that accepts created resource pack profiles
     */
    void register(Consumer<ResourcePackProfile> profileAdder);
}

