package net.modificationstation.stationapi.api.template.block;

import net.minecraft.block.ButtonBlock;
import net.modificationstation.stationapi.api.registry.Identifier;

public class TemplateButton extends ButtonBlock implements BlockTemplate {

    public TemplateButton(Identifier identifier, int texture) {
        this(BlockTemplate.getNextId(), texture);
        BlockTemplate.onConstructor(this, identifier);
    }

    public TemplateButton(int id, int texture) {
        super(id, texture);
    }
}
