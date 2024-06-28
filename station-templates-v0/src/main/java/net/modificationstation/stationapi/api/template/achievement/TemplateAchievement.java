package net.modificationstation.stationapi.api.template.achievement;

import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.stat.StatTemplate;
import net.modificationstation.stationapi.api.util.Identifier;

public class TemplateAchievement extends Achievement implements StatTemplate {
    public TemplateAchievement(Identifier identifier, String key, int column, int row, Item displayItem, Achievement parent) {
        this(StatTemplate.getNextId(), key, column, row, displayItem, parent);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateAchievement(Identifier identifier, String key, int column, int row, Block displayBlock, Achievement parent) {
        this(StatTemplate.getNextId(), key, column, row, displayBlock, parent);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateAchievement(Identifier identifier, String key, int column, int row, ItemStack icon, Achievement parent) {
        this(StatTemplate.getNextId(), key, column, row, icon, parent);
        StatTemplate.onConstructor(this, identifier);
    }

    public TemplateAchievement(int id, String key, int column, int row, Item displayItem, Achievement parent) {
        super(id, key, column, row, displayItem, parent);
    }

    public TemplateAchievement(int id, String key, int column, int row, Block displayBlock, Achievement parent) {
        super(id, key, column, row, displayBlock, parent);
    }

    public TemplateAchievement(int id, String key, int column, int row, ItemStack icon, Achievement parent) {
        super(id, key, column, row, icon, parent);
    }
}
