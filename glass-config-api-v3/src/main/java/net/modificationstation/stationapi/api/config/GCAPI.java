package net.modificationstation.stationapi.api.config;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.config.ConfigRootEntry;
import net.modificationstation.stationapi.impl.config.EventStorage;
import net.modificationstation.stationapi.impl.config.GCCore;
import net.modificationstation.stationapi.impl.config.GlassYamlFile;
import net.modificationstation.stationapi.impl.config.object.ConfigCategoryHandler;
import org.jetbrains.annotations.Nullable;
import uk.co.benjiweber.expressions.tuple.BiTuple;
import uk.co.benjiweber.expressions.tuple.QuadTuple;
import uk.co.benjiweber.expressions.tuple.TriTuple;

import java.io.*;
import java.util.concurrent.atomic.*;

/**
 * Use this instead of GCCore!
 */
public class GCAPI {

    /**
     * Force a config reload, or load your own config json! Can be partial.
     * @param configID Should be an identifier formatted like mymodid:mygconfigvalue
     * @param overrideConfigJson Optional config override JSON. Leave as null to do a plain config reload. JSON can be partial, and missing values from the JSON will be kept.
     */
    public static void reloadConfig(Identifier configID, @Nullable String overrideConfigJson) throws IOException {
        reloadConfig(configID, new GlassYamlFile(overrideConfigJson));
    }

    /**
     * Force a config reload, or load your own config json! Can be partial.
     * @param configID Should be an identifier formatted like mymodid:mygconfigvalue
     * @param overrideConfigJson Optional config override JSON. Leave as null to do a plain config reload. JSON can be partial, and missing values from the JSON will be kept.
     */
    @SuppressWarnings("deprecation")
    public static void reloadConfig(Identifier configID, @Nullable GlassYamlFile overrideConfigJson) {
        AtomicReference<Identifier> mod = new AtomicReference<>();
        GCCore.MOD_CONFIGS.keySet().forEach(modContainer -> {
            if (modContainer.equals(configID)) {
                ConfigRootEntry category = GCCore.MOD_CONFIGS.get(mod.get());
                GCCore.loadModConfig(category.configRoot(), category.modContainer(), category.configCategoryHandler().parentField, mod.get(), overrideConfigJson);
                GCCore.saveConfig(category.modContainer(), category.configCategoryHandler(), EventStorage.EventSource.MOD_SAVE);
            }
        });
    }

    /**
     * Force a config reload.
     * @param configID Should be an identifier formatted like mymodid:mygconfigvalue
     */
    public static void reloadConfig(Identifier configID) {
        reloadConfig(configID, (GlassYamlFile) null);
    }

}
