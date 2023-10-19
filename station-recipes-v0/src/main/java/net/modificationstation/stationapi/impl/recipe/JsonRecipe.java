package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;
import net.modificationstation.stationapi.api.recipe.RecipeSerializer;

/**
 * @deprecated Use {@link RecipeSerializer} instead.
 */
@Deprecated
@Data
public class JsonRecipe {

    private JsonItemKey result;
}
