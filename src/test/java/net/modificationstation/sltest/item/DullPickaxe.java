package net.modificationstation.sltest.item;

import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

public class DullPickaxe extends TemplatePickaxe {
    public DullPickaxe(Identifier identifier) {
        super(identifier, ToolMaterial.field_1692);
    }

    @Override
    public boolean preMine(ItemInstance itemInstance, BlockState blockState, int x, int y, int z, int l, PlayerBase player) {
        if(player.level.isServerSide){
            return false;
        }
        itemInstance.applyDamage(1,player);
        if(itemInstance.getDamage() >= itemInstance.getDurability()){
            player.inventory.setInventoryItem(player.inventory.selectedHotbarSlot, (ItemInstance)null);
        }
        return false;
    }
}
