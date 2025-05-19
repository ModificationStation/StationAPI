package net.modificationstation.stationapi.api.template.item;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.DoorItem;
import net.modificationstation.stationapi.api.template.block.TemplateDoorBlock;
import net.modificationstation.stationapi.api.util.Identifier;

@Getter
public class TemplateDoorItem extends DoorItem implements ItemTemplate {
    /**
     * Used in a mixin to override the door block placed.
     */
    protected final Block doorBlock;

    public TemplateDoorItem(Identifier identifier, Material arg, Block doorBlock) {
        this(ItemTemplate.getNextId(), arg, doorBlock);
        ItemTemplate.onConstructor(this, identifier);
    }
    
    public TemplateDoorItem(int id, Material arg, Block doorBlock) {
        super(id, arg);
        this.doorBlock = doorBlock;
        if (doorBlock instanceof TemplateDoorBlock templateDoorBlock) {
            templateDoorBlock.doorItem = this;
        }
    }

}
