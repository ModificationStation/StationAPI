package net.modificationstation.sltest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class DullPickaxeItem extends TemplatePickaxeItem {
    public DullPickaxeItem(Identifier identifier) {
        super(identifier, ToolMaterial.GOLD);
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

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        if (side != Direction.UP.ordinal()) return false;
        if (!world.isRemote) {
            Entity entity = EntityRegistry.create("sltest:test", world);
            entity.setPosition(x + 0.5, y + 1, z + 0.5);
            world.spawnEntity(entity);
        }
        stack.bobbingAnimationTime = 20;
        return true;
    }
}
