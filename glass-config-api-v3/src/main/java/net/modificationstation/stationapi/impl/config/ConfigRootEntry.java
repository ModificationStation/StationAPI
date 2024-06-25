package net.modificationstation.stationapi.impl.config;

import net.fabricmc.loader.api.ModContainer;
import net.modificationstation.stationapi.api.config.ConfigRoot;
import net.modificationstation.stationapi.impl.config.object.ConfigCategoryHandler;

public record ConfigRootEntry(
        ModContainer modContainer,
        ConfigRoot configRoot,
        Object configObject,
        ConfigCategoryHandler configCategoryHandler
) {}
