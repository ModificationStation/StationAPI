package net.modificationstation.stationapi.api.template.stat;

import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateStat extends Stat implements StatTemplate {
    public TemplateStat(Identifier identifier, String stringId, StatFormatter statTypeProvider) {
        this(StatTemplate.getNextId(), stringId, statTypeProvider);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateStat(Identifier identifier, String stringId) {
        this(StatTemplate.getNextId(), stringId);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateStat(int id, String stringId, StatFormatter statTypeProvider) {
        super(id, stringId, statTypeProvider);
    }

    public TemplateStat(int id, String stringId) {
        super(id, stringId);
    }
}