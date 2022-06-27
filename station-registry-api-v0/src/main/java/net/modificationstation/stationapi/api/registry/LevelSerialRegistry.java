package net.modificationstation.stationapi.api.registry;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.PostRegistryRemapEvent;
import org.jetbrains.annotations.NotNull;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * An interface extension of {@link AbstractSerialRegistry} that has different serial ID mappings for each Minecraft world.
 *
 * <p>Such implementation is necessary for block and item registries, because there's no way to provide unique serial IDs
 * for each modded setup without collisions, so we store the unique {@link Identifier} to serial ID mappings of those objects
 * in the game's worlds so they can be shared and used on different modded setups and servers.
 *
 * @param <T> the object's type that's stored in the registry.
 * @author mine_diver
 */
public interface LevelSerialRegistry<T> {

    /**
     * This method writes the {@code Identifier -> Serial ID} mappings of this registry
     * into the level properties located in level.dat file.
     *
     * @param tag the level properties NBT tag.
     */
    private void save(@NotNull CompoundTag tag) {
        //noinspection unchecked
        AbstractSerialRegistry<T> registry = (AbstractSerialRegistry<T>) this;
        registry.forEach((identifier, value) -> tag.put(identifier.toString(), registry.getSerialID(value)));
    }

    /**
     * This method loads the {@code Identifier -> Serial ID} mappings into the registry from level.dat
     * of world that's being loaded.
     *
     * @param tag the level properties NBT tag.
     */
    private void load(@NotNull CompoundTag tag) {
        //noinspection unchecked
        AbstractSerialRegistry<T> registry = (AbstractSerialRegistry<T>) this;
        registry.forEach((identifier, t) -> {
            String id = identifier.toString();
            if (tag.containsKey(id))
                remap(tag.getInt(id), t);
        });
    }

    /**
     * Internal registry method for remapping the given object's serial ID.
     *
     * <p>This is used for actually applying the changes from level.dat of the loading world to the current game.
     *
     * @param newSerialID the new serial ID that the object should be remapped to.
     * @param value the object that should be remapped.
     */
    void remap(int newSerialID, T value);

    /**
     * Writes all LevelSerialRegistries into an NBT tag.
     *
     * @param tag the tag to save all registries to.
     */
    static void saveAll(CompoundTag tag) {
        Registry.REGISTRIES.forEach((identifier, registry) -> {
            if (registry instanceof LevelSerialRegistry lsRegistry) {
                CompoundTag registryTag = new CompoundTag();
                lsRegistry.save(registryTag);
                tag.put(identifier.toString(), registryTag);
            }
        });
    }

    /**
     * Reads all LevelSerialRegistries from an NBT tag.
     *
     * @param tag the tag to load all registries from.
     */
    static void loadAll(CompoundTag tag) {
        Registry.REGISTRIES.forEach((identifier, registry) -> {
            String id = registry.id.toString();
            if (registry instanceof LevelSerialRegistry<?> lsRegistry && tag.containsKey(id)) {
                LOGGER.info("Remapping \"" + id + "\" registry...");
                lsRegistry.load(tag.getCompoundTag(id));
            }
        });
        StationAPI.EVENT_BUS.post(PostRegistryRemapEvent.builder().build());
    }
}
