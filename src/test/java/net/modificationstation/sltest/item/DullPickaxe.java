package net.modificationstation.sltest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.tool.TemplatePickaxe;

public class DullPickaxe extends TemplatePickaxe {
    public DullPickaxe(Identifier identifier) {
        super(identifier, ToolMaterial.field_1692);
    }

    @Override
    public boolean preMine(ItemStack itemInstance, BlockState blockState, int x, int y, int z, int l, PlayerEntity player) {
        if(player.world.isRemote){
            return false;
        }
        itemInstance.damage(1,player);
        if(itemInstance.getDamage() >= itemInstance.getMaxDamage()){
            player.inventory.setStack(player.inventory.selectedSlot, null);
        }
        return false;
    }
}
