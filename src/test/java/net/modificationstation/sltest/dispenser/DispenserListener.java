package net.modificationstation.sltest.dispenser;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.item.DispenseEvent;
import net.modificationstation.stationapi.api.item.ItemDispenseContext;

public class DispenserListener {
    @EventListener
    public static void changeDispenseBehavior(DispenseEvent event) {
        ItemDispenseContext context = event.itemDispenseContext;
        if (context.itemStack != null && context.inventory != null) {
            // Make arrows drop as an item instead of shoot as an entity
            if (context.itemStack.itemId == Item.ARROW.id) {
                context.shootStack(new ItemStack(Item.ARROW));
                event.cancel();
            }

            int targetX = context.x + context.xDir;
            int targetY = context.y;
            int targetZ = context.z + context.zDir;

            // Make buckets pickup liquids
            if (context.itemStack.itemId == Item.BUCKET.id) {
                if (context.world.method_1779(targetX, targetY, targetZ) == Material.WATER && context.world.getBlockMeta(targetX, targetY, targetZ) == 0) {
                    context.world.setBlock(targetX, targetY, targetZ, 0);
                    context.setItem(new ItemStack(Item.WATER_BUCKET));
                    event.cancel();
                }

                if (context.world.method_1779(targetX, targetY, targetZ) == Material.LAVA && context.world.getBlockMeta(targetX, targetY, targetZ) == 0) {
                    context.world.setBlock(targetX, targetY, targetZ, 0);
                    context.setItem(new ItemStack(Item.LAVA_BUCKET));
                    event.cancel();
                }
            }

            // Make water buckets place water
            if (context.itemStack.itemId == Item.WATER_BUCKET.id) {
                if (context.world.method_1779(targetX, targetY, targetZ) == Material.AIR) {
                    context.world.setBlockStateWithNotify(targetX, targetY, targetZ, Block.WATER.getDefaultState());
                    context.setItem(new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }

            // Make lava buckets place lava
            if (context.itemStack.itemId == Item.LAVA_BUCKET.id) {
                if (context.world.method_1779(targetX, targetY, targetZ) == Material.AIR) {
                    context.world.setBlockStateWithNotify(targetX, targetY, targetZ, Block.LAVA.getDefaultState());
                    context.setItem(new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }
        }
    }
}
