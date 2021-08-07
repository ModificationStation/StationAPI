package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.Gson;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.io.*;
import java.util.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class JsonRecipesLoader {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void loadJsonRecipes(PreInitEvent event) {
        LOGGER.info("Searching for JSON recipes...");
        ResourceManager.findResources(MODID + "/recipes", file -> file.endsWith(".json")).forEach(recipe -> {
            try {
                String rawId = new Gson().fromJson(new InputStreamReader(recipe.openStream()), JsonRecipeType.class).getType();
                try {
                    Identifier recipeId = Identifier.of(rawId);
                    JsonRecipesRegistry.INSTANCE.computeIfAbsent(recipeId, identifier -> new HashSet<>()).add(recipe);
                } catch (IllegalArgumentException e) {
                    LOGGER.warn("Found an unknown recipe type " + rawId + ". Ignoring.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
