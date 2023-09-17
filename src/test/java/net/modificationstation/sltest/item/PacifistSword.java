package net.modificationstation.sltest.item;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateSword;

public class PacifistSword extends TemplateSword {
    public PacifistSword(Identifier identifier) {
        super(identifier, ToolMaterial.field_1692);
    }

    @Override
    public boolean preHit(ItemInstance itemInstance, EntityBase otherEntity, PlayerBase player) {
        if(player.level.isServerSide){
            return false;
        }
        player.sendMessage("You tried to hurt this innocent " + EntityRegistry.getStringId(otherEntity));
        return false;
    }
}
