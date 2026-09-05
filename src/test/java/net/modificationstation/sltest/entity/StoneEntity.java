package net.modificationstation.sltest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class StoneEntity extends Entity {
    public StoneEntity(World world) {
        super(world);
        setBoundingBoxSpacing(1, 1);
    }

    @Override
    public boolean interact(PlayerEntity player) {
        this.setCobblestone(!this.isCobblestone());
        return true;
    }

    @Override
    public boolean damage(Entity damageSource, int amount) {
        this.markDead();
        return true;
    }

    @Override
    public Box getCollisionAgainstShape(Entity other) {
        return other.boundingBox;
    }

    @Override
    public Box getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, 0);
    }

    @Override
    protected void readNbt(NbtCompound nbt) {
        this.setCobblestone(nbt.getBoolean("cobblestone"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putBoolean("cobblestone", this.isCobblestone());
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public boolean isCobblestone() {
        return dataTracker.getInt(16) != 0;
    }

    public void setCobblestone(boolean cobblestone) {
        dataTracker.set(16, cobblestone ? 1 : 0);
    }
}
