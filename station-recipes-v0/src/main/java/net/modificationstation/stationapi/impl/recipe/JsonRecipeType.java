package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import net.modificationstation.stationapi.api.recipe.RecipeSerializer;

/**
 * @deprecated Use {@link RecipeSerializer} instead.
 */
@Deprecated
@Data
public class JsonRecipeType {

    private String type;
}
