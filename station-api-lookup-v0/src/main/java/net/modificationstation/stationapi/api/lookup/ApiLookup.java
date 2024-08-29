package net.modificationstation.stationapi.api.lookup;

import net.modificationstation.stationapi.api.util.API;

@API
public class ApiLookup<SUBJECT> {

    @API
    public static final ItemAPILookup ITEM_API_LOOKUP = new ItemAPILookup();
    @API
    public static final BlockAPILookup BLOCK_API_LOOKUP = new BlockAPILookup();

}
