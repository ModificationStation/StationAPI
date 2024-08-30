package net.modificationstation.stationapi.api.lookup;

import net.modificationstation.stationapi.api.lookup.block.BlockAPILookup;
import net.modificationstation.stationapi.api.lookup.item.ItemAPILookup;
import net.modificationstation.stationapi.api.util.API;

public class ApiLookup {

    @API
    public static final ItemAPILookup ITEM_API_LOOKUP = new ItemAPILookup();
    @API
    public static final BlockAPILookup BLOCK_API_LOOKUP = new BlockAPILookup();

}
