package net.modificationstation.stationapi.impl.recipe;

import lombok.Data;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;

@Data
public class JsonRecipe {

    private JsonItemKey result;
}
