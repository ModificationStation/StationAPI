package net.modificationstation.stationapi.api.template.item.tool;

import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.template.item.ItemTemplate;

public class TemplateHatchet extends net.minecraft.item.tool.Hatchet implements ItemTemplate<TemplateHatchet> {
    
    public TemplateHatchet(Identifier identifier, ToolMaterial material) {
        this(ItemRegistry.INSTANCE.getNextSerialID(), material);
        ItemRegistry.INSTANCE.register(identifier, this);
    }
    
    public TemplateHatchet(int id, ToolMaterial material) {
        super(id, material);
    }

    @Override
    public TemplateHatchet setTexturePosition(int texturePosition) {
        return (TemplateHatchet) super.setTexturePosition(texturePosition);
    }

    @Override
    public TemplateHatchet setMaxStackSize(int newMaxStackSize) {
        return (TemplateHatchet) super.setMaxStackSize(newMaxStackSize);
    }

    @Override
    public TemplateHatchet setTexturePosition(int x, int y) {
        return (TemplateHatchet) super.setTexturePosition(x, y);
    }

    @Override
    public TemplateHatchet setHasSubItems(boolean hasSubItems) {
        return (TemplateHatchet) super.setHasSubItems(hasSubItems);
    }

    @Override
    public TemplateHatchet setDurability(int durability) {
        return (TemplateHatchet) super.setDurability(durability);
    }

    @Override
    public TemplateHatchet setRendered3d() {
        return (TemplateHatchet) super.setRendered3d();
    }

    @Override
    public TemplateHatchet setTranslationKey(String newName) {
        return (TemplateHatchet) super.setTranslationKey(newName);
    }

    @Override
    public TemplateHatchet setContainerItem(net.minecraft.item.ItemBase itemType) {
        return (TemplateHatchet) super.setContainerItem(itemType);
    }
}
