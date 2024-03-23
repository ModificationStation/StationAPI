package net.modificationstation.stationapi.api.item;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class DispenseUtil {
    public World world;
    public DispenserBlockEntity dispenserBlockEntity;
    public ItemStack itemStack;
    private ItemStack shotItemStack;
    public final ItemStack[] inventory;
    public final int slot;
    public Random random = new Random();
    public int x;
    public int y;
    public int z;
    public byte xDir = 0;
    public byte zDir = 0;
    public double xVel;
    public double yVel;
    public double zVel;

    public DispenseUtil(World world, ItemStack itemStack, DispenserBlockEntity dispenserBlockEntity, ItemStack[] inventory, int slot) {
        this.world = world;
        this.dispenserBlockEntity = dispenserBlockEntity;
        this.inventory = inventory;
        this.itemStack = itemStack;
        this.slot = slot;
        populateData();
    }

    public void decrementItem(int amount) {
        if (inventory[slot] != null) {
            if (inventory[slot].count - amount <= 0) {
                inventory[slot] = null;
            } else {
                shotItemStack = inventory[slot].split(amount);
            }
            dispenserBlockEntity.markDirty();
        }
    }

    public void setItem(ItemStack itemStack) {
        inventory[slot] = itemStack;
    }

    private void populateData() {
        x = dispenserBlockEntity.x;
        y = dispenserBlockEntity.y;
        z = dispenserBlockEntity.z;
        int meta = world.getBlockMeta(x, y, z);
        if (meta == 3) {
            zDir = 1;
        } else if (meta == 2) {
            zDir = -1;
        } else if (meta == 5) {
            xDir = 1;
        } else {
            xDir = -1;
        }
        xVel = x + xDir * 0.6 + 0.5;
        yVel = y + 0.5;
        zVel = z + zDir * 0.6 + 0.5;
    }

    public void shootItemStack() {
        populateData();
        ItemEntity itemEntity = new ItemEntity(world, xVel, yVel - 0.3, zVel, shotItemStack);
        double var20 = random.nextDouble() * 0.1 + 0.2;
        itemEntity.velocityX = xDir * var20;
        itemEntity.velocityY = 0.20000000298023224;
        itemEntity.velocityZ = zDir * var20;
        itemEntity.velocityX += random.nextGaussian() * 0.007499999832361937 * 6.0;
        itemEntity.velocityY += random.nextGaussian() * 0.007499999832361937 * 6.0;
        itemEntity.velocityZ += random.nextGaussian() * 0.007499999832361937 * 6.0;
        world.method_210(itemEntity);
        world.method_230(1000, x, y, z, 0);
    }

    public void shootItemStack(ItemStack itemStack) {
        shotItemStack = itemStack;
        shootItemStack();
    }

    public void genericThrowEntity(Entity entity, double xDir, double yVel, double zDir, float pitch, float yaw) {
        float var9 = MathHelper.sqrt(xDir * xDir + yVel * yVel + zDir * zDir);
        xDir /= var9;
        yVel /= var9;
        zDir /= var9;
        xDir += this.random.nextGaussian() * 0.007499999832361937 * yaw;
        yVel += this.random.nextGaussian() * 0.007499999832361937 * yaw;
        zDir += this.random.nextGaussian() * 0.007499999832361937 * yaw;
        xDir *= pitch;
        yVel *= pitch;
        zDir *= pitch;
        entity.velocityX = xDir;
        entity.velocityY = yVel;
        entity.velocityZ = zDir;
        float var10 = MathHelper.sqrt(xDir * xDir + zDir * zDir);
        entity.prevYaw = entity.yaw = (float) (Math.atan2(xDir, zDir) * 180.0 / 3.1415927410125732);
        entity.prevPitch = entity.pitch = (float) (Math.atan2(yVel, var10) * 180.0 / 3.1415927410125732);
    }

    public void shootEntity(Entity entity) {
        entity.method_1340(x + 0.5, y + 0.5, z + 0.5);
        genericThrowEntity(entity, xDir, 0.1, zDir, 1.1F, 6.0F);
        world.method_210(entity);
        world.method_230(1002, x, y, z, 0);
    }

    public BlockPos getFacingBlockPos() {
        return new BlockPos(x + xDir, y, z + zDir);
    }
}