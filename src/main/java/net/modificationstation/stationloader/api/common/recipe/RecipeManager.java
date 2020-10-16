package net.modificationstation.stationloader.api.common.recipe;

import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface RecipeManager extends HasHandler<RecipeManager> {

    RecipeManager INSTANCE = new RecipeManager() {

        private RecipeManager handler;

        @Override
        public void setHandler(RecipeManager handler) {
            this.handler = handler;
        }

        @Override
        public void addJsonRecipe(String recipe) {
            checkAccess(handler);
            handler.addJsonRecipe(recipe);
        }

        @Override
        public Set<String> addOrGetRecipeType(String type, Consumer<String> register) {
            checkAccess(handler);
            return handler.addOrGetRecipeType(type, register);
        }
    };

    void addJsonRecipe(String recipe);

    Set<String> addOrGetRecipeType(String type, Consumer<String> register);

    Map<String, Map<String, Map.Entry<Consumer<String>, Set<String>>>> recipes = new HashMap<>();
}
