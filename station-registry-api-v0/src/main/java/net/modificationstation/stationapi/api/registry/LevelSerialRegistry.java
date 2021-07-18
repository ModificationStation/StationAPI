package net.modificationstation.stationapi.api.registry;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.PostRegistryRemapEvent;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract extension of {@link AbstractSerialRegistry} that has different serial ID mappings for each Minecraft world.
 *
 * <p>Such implementation is necessary for block and item registries, because there's no way to provide unique serial IDs
 * for each modded setup without collisions, so we store the unique {@link Identifier} to serial ID mappings of those objects
 * in the game's worlds so they can be shared and used on different modded setups and servers.
 *
 * @param <T> the object's type that's stored in the registry.
 * @author mine_diver
 */
public abstract class LevelSerialRegistry<T> extends AbstractSerialRegistry<T> {

    /**
     * Default registry constructor.
     * @param identifier registry's identifier.
     */
    public LevelSerialRegistry(@NotNull Identifier identifier) {
        super(identifier);
    }

    public LevelSerialRegistry(@NotNull Identifier identifier, boolean shiftSerialIDOnRegister) {
        super(identifier, shiftSerialIDOnRegister);
    }

    /**
     * This method writes the {@code Identifier -> Serial ID} mappings of this registry
     * into the level properties located in level.dat file.
     *
     * @param tag the level properties NBT tag.
     */
    public void save(@NotNull CompoundTag tag) {
        forEach((identifier, value) -> tag.put(identifier.toString(), getSerialID(value)));
    }

    /**
     * This method loads the {@code Identifier -> Serial ID} mappings into the registry from level.dat
     * of world that's being loaded.
     *
     * @param tag the level properties NBT tag.
     */
    public void load(@NotNull CompoundTag tag) {
        forEach((identifier, t) -> {
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
    protected abstract void remap(int newSerialID, @NotNull T value);

    /**
     * Writes all LevelSerialRegistries into an NBT tag.
     *
     * @param tag the tag to save all registries to.
     */
    public static void saveAll(CompoundTag tag) {
        Registry.REGISTRIES.forEach((identifier, registry) -> {
            if (registry instanceof LevelSerialRegistry) {
                CompoundTag registryTag = new CompoundTag();
                ((LevelSerialRegistry<?>) registry).save(registryTag);
                tag.put(identifier.toString(), registryTag);
            }
        });
    }

    /**
     * Reads all LevelSerialRegistries from an NBT tag.
     *
     * @param tag the tag to load all registries from.
     */
    public static void loadAll(CompoundTag tag) {
        Registry.REGISTRIES.forEach((identifier, registry) -> {
            String id = registry.id.toString();
            if (registry instanceof LevelSerialRegistry<?> && tag.containsKey(id))
                ((LevelSerialRegistry<?>) registry).load(tag.getCompoundTag(id));
        });
        StationAPI.EVENT_BUS.post(new PostRegistryRemapEvent());
    }
}
