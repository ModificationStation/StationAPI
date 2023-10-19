package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.api.recipe.StationShapedRecipe.Serializer;

/**
 * @deprecated Use {@link Serializer} instead.
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class JsonCraftingShaped extends JsonRecipe {

    private String[] pattern;
}
