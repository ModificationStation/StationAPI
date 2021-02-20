package net.modificationstation.stationapi.impl.common.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.recipe.JsonRecipeParserRegister;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.impl.common.item.JsonItemKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class JsonRecipeParserInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerJsonRecipeParsers(JsonRecipeParserRegister event) {
        event.registry.registerValue(Identifier.of("crafting_shaped"), recipe -> {
            JsonElement rawJson;
            try {
                rawJson = JsonParser.parseReader(new BufferedReader(new InputStreamReader(recipe.openStream())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonCraftingShaped json = new Gson().fromJson(rawJson, JsonCraftingShaped.class);
            Set<Map.Entry<String, JsonElement>> rawKeys = rawJson.getAsJsonObject().getAsJsonObject("key").entrySet();
            String[] pattern = json.getPattern();
            Object[] keys = new Object[rawKeys.size() * 2 + pattern.length];
            int i = 0;
            for (; i < pattern.length; i++)
                keys[i] = pattern[i];
            for (Map.Entry<String, JsonElement> key : rawKeys) {
                keys[i] = key.getKey().charAt(0);
                keys[i + 1] = new Gson().fromJson(key.getValue(), JsonItemKey.class).getItemInstance();
                i += 2;
            }
            net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.addShapedRecipe(json.getResult().getItemInstance(), keys);
        });
        event.registry.registerValue(Identifier.of("crafting_shapeless"), recipe -> {
            JsonCraftingShapeless json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonCraftingShapeless.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonItemKey[] ingredients = json.getIngredients();
            Object[] iteminstances = new Object[json.getIngredients().length];
            for (int i = 0; i < ingredients.length; i++)
                iteminstances[i] = ingredients[i].getItemInstance();
            net.modificationstation.stationapi.api.common.recipe.CraftingRegistry.INSTANCE.addShapelessRecipe(json.getResult().getItemInstance(), iteminstances);
        });
        event.registry.registerValue(Identifier.of("smelting"), recipe -> {
            JsonSmelting json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonSmelting.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            net.modificationstation.stationapi.api.common.recipe.SmeltingRegistry.INSTANCE.addSmeltingRecipe(json.getIngredient().getItemInstance(), json.getResult().getItemInstance());
        });
    }
}
