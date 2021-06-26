package net.modificationstation.stationapi.api.template.item.tool;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateShears extends net.minecraft.item.tool.Shears implements ItemTemplate<TemplateShears> {
    
    public TemplateShears(Identifier identifier) {
        this(ItemRegistry.INSTANCE.getNextSerialIDShifted());
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateShears(int i) {
        super(i);
    }

    @Override
    public TemplateShears setTexturePosition(int texturePosition) {
        return (TemplateShears) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateShears setMaxStackSize(int newMaxStackSize) {
        return (TemplateShears) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateShears setTexturePosition(int x, int y) {
        return (TemplateShears) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateShears setHasSubItems(boolean hasSubItems) {
        return (TemplateShears) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateShears setDurability(int durability) {
        return (TemplateShears) super.setDurability(durability);
    }

    @Override
    public TemplateShears setRendered3d() {
        return (TemplateShears) super.setRendered3d();
    }

    @Override
    public TemplateShears setTranslationKey(String newName) {
        return (TemplateShears) super.setTranslationKey(newName);
    }

    @Override
    public TemplateShears setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateShears) super.setContainerItem(itemType);
    }
}
