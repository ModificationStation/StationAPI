package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.modificationstation.stationapi.api.template.item.JsonItemKey;

@EqualsAndHashCode(callSuper = true)
@Data
public class JsonSmelting extends JsonRecipe {

    private JsonItemKey ingredient;
}
