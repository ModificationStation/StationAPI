package net.modificationstation.sltest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplateSword;

public class PacifistSword extends TemplateSword {
    public PacifistSword(Identifier identifier) {
        super(identifier, ToolMaterial.GOLD);
    }

    @Override
    public boolean preHit(ItemStack itemInstance, Entity otherEntity, PlayerEntity player) {
        if(player.world.isRemote){
            return false;
        }
        player.method_490("You tried to hurt this innocent " + EntityRegistry.getId(otherEntity));
        return false;
    }
}
