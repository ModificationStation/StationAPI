package net.modificationstation.stationapi.api.template.stat;

import net.minecraft.stat.ItemOrBlockStat;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateItemOrBlockStat extends ItemOrBlockStat implements StatTemplate {
    public TemplateItemOrBlockStat(Identifier identifier, String translationKey, int itemIdOrBlockId) {
        this(StatTemplate.getNextId(), translationKey, itemIdOrBlockId);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateItemOrBlockStat(int statId, String translationKey, int itemIdOrBlockId) {
        super(statId, translationKey, itemIdOrBlockId);
    }
}