package net.modificationstation.sltest.item;

import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateShearsItem;
import net.modificationstation.stationapi.api.util.math.Direction;

public class TestShearsItem extends TemplateShearsItem {
    public TestShearsItem(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean useOnBlock(ItemStack item, PlayerEntity player, World level, int x, int y, int z, int facing) {
        if (player.isSneaking()) {
            if (!level.isRemote) {
                level.setBlock(x, y, z, 0);
            }
            item.bobbingAnimationTime = 20;
            return true;
        } else if (facing == Direction.UP.ordinal()) {
            if (!level.isRemote) {
                LivingEntity entity = (LivingEntity) EntityRegistry.create("Sheep", level);
                entity.setPosition(x + 0.5, y + 1, z + 0.5);
                level.spawnEntity(entity);
                entity.animateSpawn();
            }
            item.bobbingAnimationTime = 20;
            return true;
        } else
            return false;
    }
}
