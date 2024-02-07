package net.modificationstation.stationapi.impl.client.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.objects.Object2ReferenceAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceSortedMap;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.option.GameOptionsEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingEvent;
import net.modificationstation.stationapi.api.client.option.StationKeyBinding;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class StationKeyBindingsManager {
    private static final Path
            CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("stationapi"),
            OPTIONS_PATH = CONFIG_PATH.resolve("options.json");
    private static final Object2ReferenceSortedMap<Identifier, StationKeyBinding> KEY_BINDINGS = new Object2ReferenceAVLTreeMap<>();

    public static void registerKeyBinding(StationKeyBinding keyBinding) {
        KEY_BINDINGS.put(keyBinding.id, keyBinding);
    }

    @EventListener
    private static void onGameOptionsLoad(GameOptionsEvent.Load event) {
        if (Files.notExists(OPTIONS_PATH)) return;
        try {
            JsonObject options = JsonHelper.deserialize(Files.newBufferedReader(OPTIONS_PATH));
            if (!options.has("key_bindings") || !(options.get("key_bindings") instanceof JsonObject keyBindingEntries)) return;
            for (Map.Entry<String, JsonElement> keyBindingEntry : keyBindingEntries.entrySet()) {
                Identifier identifier = Identifier.of(keyBindingEntry.getKey());
                StationKeyBinding keyBinding = KEY_BINDINGS.get(identifier);
                if (keyBinding == null || !(keyBindingEntry.getValue() instanceof JsonPrimitive key)) continue;
                try {
                    keyBinding.code = key.getAsInt();
                } catch (NumberFormatException e) {
                    StationAPI.LOGGER.error("Couldn't parse the code value for " + keyBindingEntry.getKey() + " key binding!", e);
                }
            }
        } catch (IOException e) {
            StationAPI.LOGGER.error("Couldn't open StationAPI's options.json file!", e);
        } catch (JsonParseException e) {
            StationAPI.LOGGER.error("Couldn't parse StationAPI's options.json file!", e);
        }
    }

    @EventListener
    private static void onGameOptionsSave(GameOptionsEvent.Save event) {
        try {
            Files.createDirectories(OPTIONS_PATH.getParent());
            try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(OPTIONS_PATH.toFile()))) {
                jsonWriter.setIndent("  ");
                jsonWriter.beginObject();
                jsonWriter.name("key_bindings");
                jsonWriter.beginObject();
                for (Map.Entry<Identifier, StationKeyBinding> entry : KEY_BINDINGS.entrySet()) {
                    Identifier identifier = entry.getKey();
                    StationKeyBinding keyBinding = entry.getValue();
                    jsonWriter.name(identifier.toString()).value(keyBinding.code);
                }
                jsonWriter.endObject();
                jsonWriter.endObject();
                jsonWriter.flush();
            }
        } catch (IOException e) {
            StationAPI.LOGGER.error("Couldn't save StationAPI's options.json file!", e);
        }
    }

    @EventListener
    private static void onKeyBindingLoad(KeyBindingEvent.Load event) {
        if (event.keyBinding instanceof StationKeyBinding) event.cancel();
    }

    @EventListener
    private static void onKeyBindingSave(KeyBindingEvent.Save event) {
        if (event.keyBinding instanceof StationKeyBinding) event.cancel();
    }
}
