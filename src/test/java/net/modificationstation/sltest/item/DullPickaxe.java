package net.modificationstation.sltest.item;

import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

public class DullPickaxe extends TemplatePickaxe {
    public DullPickaxe(Identifier identifier) {
        super(identifier, ToolMaterial.field_1692);
    }

    @Override
    public boolean preMine(ItemInstance itemInstance, int x, int y, int z, int l, Living entity) {
        if(entity.level.isServerSide){
            return false;
        }
        itemInstance.applyDamage(1,entity);
        if(itemInstance.getDamage() >= itemInstance.getDurability()){
            PlayerBase player = (PlayerBase) entity;
            player.inventory.setInventoryItem(player.inventory.selectedHotbarSlot, (ItemInstance)null);
        }
        return false;
    }
}
