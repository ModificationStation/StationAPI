package net.modificationstation.stationapi.impl.resource;

import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationapi.api.resource.ResourcePack;

/**
 * Interface implemented by mod-provided resource packs.
 */
public interface ModResourcePack extends ResourcePack {
    /**
     * @return The ModMetadata object associated with the mod providing this
     * resource pack.
     */
    ModMetadata getFabricModMetadata();
}
