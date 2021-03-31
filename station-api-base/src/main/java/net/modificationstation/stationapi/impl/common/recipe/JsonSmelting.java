package net.modificationstation.stationapi.impl.common.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.impl.common.item.JsonItemKey;

@EqualsAndHashCode(callSuper = true)
@Data
public class JsonSmelting extends JsonRecipe {

    private JsonItemKey ingredient;
}
