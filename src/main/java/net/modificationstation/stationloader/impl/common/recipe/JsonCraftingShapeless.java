package net.modificationstation.stationloader.impl.common.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationloader.impl.common.item.JsonItemKey;

@EqualsAndHashCode(callSuper = true)
@Data
public class JsonCraftingShapeless extends JsonRecipe {

    private JsonItemKey[] ingredients;
}
