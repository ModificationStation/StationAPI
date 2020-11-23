package net.modificationstation.stationloader.api.common.recipe;

import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.io.IOException;
import java.net.URL;
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
        public void addJsonRecipe(URL recipe) throws IOException {
            checkAccess(handler);
            handler.addJsonRecipe(recipe);
        }

        @Override
        public Set<URL> addOrGetRecipeType(String type, Consumer<URL> register) {
            checkAccess(handler);
            return handler.addOrGetRecipeType(type, register);
        }
    };

    void addJsonRecipe(URL recipe) throws IOException;

    Set<URL> addOrGetRecipeType(String type, Consumer<URL> register);

    Map<String, Map<String, Map.Entry<Consumer<URL>, Set<URL>>>> recipes = new HashMap<>();
}
