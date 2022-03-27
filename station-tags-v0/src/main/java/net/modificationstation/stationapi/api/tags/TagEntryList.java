package net.modificationstation.stationapi.api.tags;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.ArrayList;

public class TagEntryList extends ArrayList<TagEntry> {

    public final Identifier fullTag;

    public TagEntryList(Identifier fullTag) {
        this.fullTag = fullTag;
    }

}
