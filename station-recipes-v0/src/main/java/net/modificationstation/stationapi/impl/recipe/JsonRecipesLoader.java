package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.Gson;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.resource.Filters;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.exception.MissingModException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class JsonRecipesLoader {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void loadJsonRecipes(PreInitEvent event) {
        LOGGER.info("Searching for JSON recipes...");
        String recipePath = MODID + "/recipes";
        ResourceManager.DATA.find(recipePath, Filters.FileType.JSON).forEach(JsonRecipesLoader::registerRecipe);
        ResourceManager.ASSETS.find(recipePath, Filters.FileType.JSON).forEach(recipe -> {
            LOGGER.warn("Found a recipe (" + recipe + ") under assets directory, which is deprecated for recipes!");
            registerRecipe(recipe);
        });
    }

    private static void registerRecipe(URL recipe) {
        try {
            String rawId = new Gson().fromJson(new InputStreamReader(recipe.openStream()), JsonRecipeType.class).getType();
            Identifier recipeId;
            try {
                recipeId = Identifier.of(rawId);
            } catch (MissingModException e) {
                LOGGER.warn("Found an unknown recipe type " + rawId + ". Ignoring.");
                return;
            }
            JsonRecipesRegistry.INSTANCE.computeIfAbsent(recipeId, identifier -> new HashSet<>()).add(recipe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
