package net.modificationstation.sltest.dispenser;


import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.dispenser.DispenseEvent;
import net.modificationstation.stationapi.api.dispenser.ItemDispenseContext;

public class DispenserListener {
    @EventListener
    public static void changeDispenseBehavior(DispenseEvent event) {
        ItemDispenseContext context = event.context;
        World world = context.dispenser.world;

        if (context.itemStack != null) {
            // Make arrows drop as an item instead of shoot as an entity
            if (context.itemStack.itemId == Item.ARROW.id) {
                context.skipSpecial = true;
            }

            BlockPos facing = context.getFacingBlockPos();

            // Make buckets pickup liquids
            if (context.itemStack.itemId == Item.BUCKET.id) {
                if (world.method_1779(facing.x, facing.y, facing.z) == Material.WATER && world.getBlockMeta(facing.x, facing.y, facing.z) == 0) {
                    world.setBlock(facing.x, facing.y, facing.z, 0);
                    context.dispenser.setStack(context.slot, new ItemStack(Item.WATER_BUCKET));
                    event.cancel();
                }

                if (world.method_1779(facing.x, facing.y, facing.z) == Material.LAVA && world.getBlockMeta(facing.x, facing.y, facing.z) == 0) {
                    world.setBlock(facing.x, facing.y, facing.z, 0);
                    context.dispenser.setStack(context.slot, new ItemStack(Item.LAVA_BUCKET));
                    event.cancel();
                }
            }

            // Make water buckets place water
            if (context.itemStack.itemId == Item.WATER_BUCKET.id) {
                if (world.method_1779(facing.x, facing.y, facing.z) == Material.AIR) {
                    world.setBlockStateWithNotify(facing.x, facing.y, facing.z, Block.WATER.getDefaultState());
                    context.dispenser.setStack(context.slot, new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }

            // Make lava buckets place lava
            if (context.itemStack.itemId == Item.LAVA_BUCKET.id) {
                if (world.method_1779(facing.x, facing.y, facing.z) == Material.AIR) {
                    world.setBlockStateWithNotify(facing.x, facing.y, facing.z, Block.LAVA.getDefaultState());
                    context.dispenser.setStack(context.slot, new ItemStack(Item.BUCKET));
                    event.cancel();
                }
            }
        }
    }
}
