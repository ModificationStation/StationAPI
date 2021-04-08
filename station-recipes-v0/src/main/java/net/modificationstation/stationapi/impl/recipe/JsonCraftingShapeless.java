package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.api.item.JsonItemKey;

@EqualsAndHashCode(callSuper = true)
@Data
public class JsonCraftingShapeless extends JsonRecipe {

    private JsonItemKey[] ingredients;
}
