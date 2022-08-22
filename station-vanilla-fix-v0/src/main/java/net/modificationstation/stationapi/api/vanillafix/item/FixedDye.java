package net.modificationstation.stationapi.api.vanillafix.item;

import net.minecraft.entity.Living;
import net.minecraft.entity.animal.Sheep;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.vanillafix.util.DyeColor;

public class FixedDye extends TemplateItemBase {

    private final DyeColor color;

    public FixedDye(Identifier identifier, DyeColor color) {
        super(identifier);
        this.color = color;
    }

    @Override
    public void interactWithEntity(ItemInstance arg, Living arg2) {
        if (arg2 instanceof Sheep sheep) {
            int n = color.getId();
            if (!sheep.isSheared() && sheep.getColour() != n) {
                sheep.setColour(n);
                --arg.count;
            }
        }
    }
}
