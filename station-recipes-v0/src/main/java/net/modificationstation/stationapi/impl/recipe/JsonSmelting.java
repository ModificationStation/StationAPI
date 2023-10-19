package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;
import net.modificationstation.stationapi.api.recipe.RecipeSerializer;

/**
 * @deprecated Use {@link RecipeSerializer} instead.
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class JsonSmelting extends JsonRecipe {

    private JsonItemKey ingredient;
}
