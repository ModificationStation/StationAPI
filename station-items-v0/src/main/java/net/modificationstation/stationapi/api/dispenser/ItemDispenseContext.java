package net.modificationstation.stationapi.api.dispenser;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.modificationstation.stationapi.api.item.CustomDispenseBehavior;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.mixin.item.EntityAccessor;

import java.util.Random;

/**
 * Utility class for changing dispenser behavior, providing tools to customize behavior
 * @author matthewperiut
 * @see DispenseEvent
 * @see CustomDispenseBehavior
 */
public class ItemDispenseContext {
    public final DispenserBlockEntity dispenserBlockEntity;
    public final int slot;
    public final Direction dispenseDirection;
    public ItemStack itemStack;
    /**
     * Skips special vanilla behavior for arrows, snowballs and eggs.
     */
    public boolean skipSpecial;

    public ItemDispenseContext(ItemStack itemStack, DispenserBlockEntity dispenserBlockEntity, int slot) {
        this.dispenserBlockEntity = dispenserBlockEntity;
        this.itemStack = itemStack;
        this.slot = slot;
        dispenseDirection = Direction.byId(dispenserBlockEntity.world.getBlockMeta(dispenserBlockEntity.x, dispenserBlockEntity.y, dispenserBlockEntity.z));
    }

    public static void genericShootEntity(Entity entity, double velX, double velY, double velZ, float pitch, float yaw) {
        float var9 = MathHelper.sqrt(velX * velX + velY * velY + velZ * velZ);
        velX /= var9;
        velY /= var9;
        velZ /= var9;
        Random random = ((EntityAccessor) entity).stationapi_getRandom();
        velX += random.nextGaussian() * 0.007499999832361937 * yaw;
        velY += random.nextGaussian() * 0.007499999832361937 * yaw;
        velZ += random.nextGaussian() * 0.007499999832361937 * yaw;
        velX *= pitch;
        velY *= pitch;
        velZ *= pitch;
        entity.velocityX = velX;
        entity.velocityY = velY;
        entity.velocityZ = velZ;
        float var10 = MathHelper.sqrt(velX * velX + velZ * velZ);
        entity.prevYaw = entity.yaw = (float) (Math.atan2(velX, velZ) * 180.0 / 3.1415927410125732);
        entity.prevPitch = entity.pitch = (float) (Math.atan2(velY, var10) * 180.0 / 3.1415927410125732);
    }

    public interface ShootEntityFunction {
        void shoot(Entity entity, double velX, double velY, double velZ, float pitch, float yaw);
    }

    public void shootEntity(Entity entity) {
        shootEntity(entity, ItemDispenseContext::genericShootEntity);
    }

    public void shootEntity(Entity entity, ShootEntityFunction shootFunc) {
        entity.method_1340(dispenserBlockEntity.x + 0.5, dispenserBlockEntity.y + 0.5, dispenserBlockEntity.z + 0.5);
        shootFunc.shoot(entity, dispenseDirection.getOffsetX(), dispenseDirection.getOffsetY() + 0.1, dispenseDirection.getOffsetZ(), 1.1F, 6.0F);
        dispenserBlockEntity.world.method_210(entity);
        dispenserBlockEntity.world.method_230(1002, dispenserBlockEntity.x, dispenserBlockEntity.y, dispenserBlockEntity.z, 0);
        dispenserBlockEntity.world.method_230(2000, dispenserBlockEntity.x, dispenserBlockEntity.y, dispenserBlockEntity.z, dispenseDirection.getOffsetX() + 1 + (dispenseDirection.getOffsetZ() + 1) * 3);
    }

    public BlockPos getFacingBlockPos() {
        return new BlockPos(dispenserBlockEntity.x, dispenserBlockEntity.y, dispenserBlockEntity.z).add(dispenseDirection.getVector());
    }
}
