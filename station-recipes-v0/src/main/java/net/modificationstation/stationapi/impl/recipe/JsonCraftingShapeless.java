package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;
import net.modificationstation.stationapi.api.recipe.StationShapelessRecipe.Serializer;

/**
 * @deprecated Use {@link Serializer} instead.
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
public class JsonCraftingShapeless extends JsonRecipe {

    private JsonItemKey[] ingredients;
}
