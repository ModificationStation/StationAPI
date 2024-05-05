package net.modificationstation.stationapi.api.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.modificationstation.stationapi.impl.config.EventStorage;
import net.modificationstation.stationapi.impl.config.GCCore;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.config.object.ConfigCategory;
import org.jetbrains.annotations.Nullable;
import uk.co.benjiweber.expressions.tuple.BiTuple;

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
    public static void reloadConfig(Identifier configID, @Nullable String overrideConfigJson) throws SyntaxError {
        reloadConfig(configID, Jankson.builder().build().load(overrideConfigJson));
    }

    /**
     * Force a config reload, or load your own config json! Can be partial.
     * @param configID Should be an identifier formatted like mymodid:mygconfigvalue
     * @param overrideConfigJson Optional config override JSON. Leave as null to do a plain config reload. JSON can be partial, and missing values from the JSON will be kept.
     */
    @SuppressWarnings("deprecation")
    public static void reloadConfig(Identifier configID, @Nullable JsonObject overrideConfigJson) {
        AtomicReference<Identifier> mod = new AtomicReference<>();
        GCCore.MOD_CONFIGS.keySet().forEach(modContainer -> {
            if (modContainer.toString().equals(configID.toString())) {
                mod.set(modContainer);
            }
        });
        if (mod.get() != null) {
            BiTuple<EntrypointContainer<Object>, ConfigCategory> category = GCCore.MOD_CONFIGS.get(mod.get());
            GCCore.loadModConfig(category.one().getEntrypoint(), category.one().getProvider(), category.two().parentField, mod.get(), overrideConfigJson);
            GCCore.saveConfig(category.one(), category.two(), EventStorage.EventSource.MOD_SAVE);
        }
    }

    /**
     * Force a config reload.
     * @param configID Should be an identifier formatted like mymodid:mygconfigvalue
     */
    public static void reloadConfig(Identifier configID) {
        reloadConfig(configID, (JsonObject) null);
    }

}
