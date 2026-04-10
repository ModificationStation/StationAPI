package net.modificationstation.stationapi.api.template.stat;

import net.minecraft.stat.SimpleStat;
import net.minecraft.stat.StatFormatter;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateSimpleStat extends SimpleStat implements StatTemplate {
    public TemplateSimpleStat(Identifier identifier, String stringId, StatFormatter statTypeProvider) {
        this(StatTemplate.getNextId(), stringId, statTypeProvider);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateSimpleStat(Identifier identifier, String stringId) {
        this(StatTemplate.getNextId(), stringId);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateSimpleStat(int id, String stringId, StatFormatter statTypeProvider) {
        super(id, stringId, statTypeProvider);
    }

    public TemplateSimpleStat(int id, String stringId) {
        super(id, stringId);
    }
}