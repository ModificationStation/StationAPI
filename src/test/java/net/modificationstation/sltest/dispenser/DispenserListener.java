package net.modificationstation.sltest.dispenser;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.item.DispenseEvent;
import net.modificationstation.stationapi.api.item.DispenseUtil;

public class DispenserListener {
    @EventListener
    public static void changeDispenseBehavior(DispenseEvent event) {
        DispenseUtil util = event.dispenseUtil;
        if (util.itemStack != null && util.inventory != null) {
            // Make arrows drop as an item instead of shoot as an entity
            if (util.itemStack.itemId == Item.ARROW.id) {
                util.shootItemStack(new ItemStack(Item.ARROW));
                event.cancel();
            }

            int targetX = util.x + util.xDir;
            int targetY = util.y;
            int targetZ = util.z + util.zDir;

            // Make buckets pickup liquids
            if (util.itemStack.itemId == Item.BUCKET.id) {
                if (util.world.method_1779(targetX, targetY, targetZ) == Material.WATER && util.world.getBlockMeta(targetX, targetY, targetZ) == 0) {
                    util.world.setBlock(targetX, targetY, targetZ, 0);
                    util.setItem(new ItemStack(Item.WATER_BUCKET));
                    event.cancel();
                }

                if (util.world.method_1779(targetX, targetY, targetZ) == Material.LAVA && util.world.getBlockMeta(targetX, targetY, targetZ) == 0) {
                    util.world.setBlock(targetX, targetY, targetZ, 0);
                    util.setItem(new ItemStack(Item.LAVA_BUCKET));
                    event.cancel();
                }
            }

            // Make water buckets place water
            if (util.itemStack.itemId == Item.WATER_BUCKET.id) {
                if (util.world.method_1779(targetX, targetY, targetZ) == Material.AIR) {
                    util.world.setBlockStateWithNotify(targetX, targetY, targetZ, Block.WATER.getDefaultState());
                    util.setItem(new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }

            // Make lava buckets place lava
            if (util.itemStack.itemId == Item.LAVA_BUCKET.id) {
                if (util.world.method_1779(targetX, targetY, targetZ) == Material.AIR) {
                    util.world.setBlockStateWithNotify(targetX, targetY, targetZ, Block.LAVA.getDefaultState());
                    util.setItem(new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }
        }
    }
}
