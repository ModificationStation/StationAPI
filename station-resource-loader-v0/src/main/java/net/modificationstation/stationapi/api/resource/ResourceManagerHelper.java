package net.modificationstation.stationapi.api.resource;

import net.fabricmc.loader.api.ModContainer;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.resource.loader.ResourceManagerHelperImpl;
import org.jetbrains.annotations.ApiStatus;

/**
 * Helper for working with {@link ResourceManager} instances, and other resource loader generalities.
 */
@ApiStatus.NonExtendable
public interface ResourceManagerHelper {

    /**
     * Register a resource reload listener for a given resource manager type.
     *
     * @param listener The resource reload listener.
     */
    void registerReloadListener(IdentifiableResourceReloadListener listener);

    /**
     * Get the ResourceManagerHelper instance for a given resource type.
     *
     * @param type The given resource type.
     * @return The ResourceManagerHelper instance.
     */
    static ResourceManagerHelper get(ResourceType type) {
        return ResourceManagerHelperImpl.get(type);
    }

    /**
     * Registers a built-in resource pack.
     *
     * <p>A built-in resource pack is an extra resource pack provided by your mod which is not always active, it's similar to the "Programmer Art" resource pack.
     *
     * <p>Why and when to use it? A built-in resource pack should be used to provide extra assets/data that should be optional with your mod but still directly provided by it.
     * For example, it could provide textures of your mod in another resolution, or could allow to provide different styles of your assets.
     *
     * <p>The path in which the resource pack is located is in the mod JAR file under the {@code "resourcepacks/<id path>"} directory. {@code id path} being the path specified
     * in the identifier of this built-in resource pack.
     *
     * @param id             the identifier of the resource pack
     * @param container      the mod container
     * @param activationType the activation type of the resource pack
     * @return {@code true} if successfully registered the resource pack, else {@code false}
     */
    static boolean registerBuiltinResourcePack(Identifier id, ModContainer container, ResourcePackActivationType activationType) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.path, container, activationType);
    }

    /**
     * Registers a built-in resource pack.
     *
     * <p>A built-in resource pack is an extra resource pack provided by your mod which is not always active, it's similar to the "Programmer Art" resource pack.
     *
     * <p>Why and when to use it? A built-in resource pack should be used to provide extra assets/data that should be optional with your mod but still directly provided by it.
     * For example, it could provide textures of your mod in another resolution, or could allow to provide different styles of your assets.
     *
     * <p>The path in which the resource pack is located is in the mod JAR file under the {@code "resourcepacks/<id path>"} directory. {@code id path} being the path specified
     * in the identifier of this built-in resource pack.
     *
     * @param id             the identifier of the resource pack
     * @param container      the mod container
     * @param displayName    the display name of the resource pack, should include mod name for clarity
     * @param activationType the activation type of the resource pack
     * @return {@code true} if successfully registered the resource pack, else {@code false}
     */
    static boolean registerBuiltinResourcePack(Identifier id, ModContainer container, String displayName, ResourcePackActivationType activationType) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.path, container, displayName, activationType);
    }
}
