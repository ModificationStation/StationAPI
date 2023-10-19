package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.*;
import lombok.val;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.resource.DataResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.RecipeSerializer;
import net.modificationstation.stationapi.api.recipe.RecipeType;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.RecipeSerializerRegistry;
import net.modificationstation.stationapi.api.registry.RecipeTypeRegistry;
import net.modificationstation.stationapi.api.resource.JsonDataLoader;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.JsonHelper;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.Map;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerStatic = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class RecipeManager extends JsonDataLoader {
    private static final Gson GSON = new Gson();

    public RecipeManager() {
        super(GSON, MODID + "/recipes");
    }

    @EventListener
    private void registerToManager(DataResourceReloaderRegisterEvent event) {
        event.resourceManager.registerReloader(this);
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        System.out.println(RecipeSerializer.SHAPELESS.toString());
        RecipeTypeRegistry.INSTANCE.iterator().forEachRemaining(recipeType -> recipeType.removeRecipes(StationRecipe::isDataDriven));
        prepared.forEach((identifier, value) -> {
            try {
                StationRecipe<?> recipe = RecipeManager.deserialize(identifier, JsonHelper.asObject(value, "top element"));
                //noinspection unchecked
                ((RecipeType<StationRecipe<?>>) recipe.getType()).addRecipe(recipe);
            } catch (JsonParseException | IllegalArgumentException runtimeException) {
                LOGGER.error("Parsing error loading recipe {}", identifier, runtimeException);
            }
        });
        LOGGER.info("Loaded {} recipes", prepared.size());
    }

    /**
     * Reads a recipe from a JSON object.
     *
     * @implNote Even though a recipe's {@linkplain Recipe#getSerializer() serializer}
     * is stored in a {@code type} field in the JSON format and referred so in this
     * method, its registry has key {@code minecraft:root/minecraft:recipe_serializer}
     * and is thus named.
     *
     * @throws com.google.gson.JsonParseException if the recipe JSON is invalid
     * @return the read recipe
     * @see RecipeSerializer#read
     *
     * @param id the recipe's ID
     * @param json the recipe JSON
     */
    public static StationRecipe<?> deserialize(Identifier id, JsonObject json) {
        val type = JsonHelper.getString(json, "type");
        return RecipeSerializerRegistry.INSTANCE.getOrEmpty(Identifier.of(type))
                .orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported recipe type '" + type + "'"))
                .read(id, json);
    }
}
