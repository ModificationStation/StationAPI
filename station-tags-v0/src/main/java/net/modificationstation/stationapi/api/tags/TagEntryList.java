package net.modificationstation.stationapi.api.tags;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.*;

public class TagEntryList extends ArrayList<TagEntry> {

    public final Identifier fullTag;

    public TagEntryList(Identifier fullTag) {
        this.fullTag = fullTag;
    }

    public boolean tagMatches(Identifier otherTag) {
        return otherTag.id.endsWith("/")? fullTag.toString().startsWith(otherTag.toString()) || fullTag.toString().equals(otherTag.toString().substring(0, otherTag.toString().length()-1)) : fullTag.equals(otherTag);
    }
}
