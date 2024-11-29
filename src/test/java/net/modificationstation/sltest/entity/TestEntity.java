package net.modificationstation.sltest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;

import static net.modificationstation.sltest.SLTest.NAMESPACE;

@HasTrackingParameters(trackingDistance = 20, updatePeriod = 20)
public class TestEntity extends Entity implements EntitySpawnDataProvider {
    public static final Identifier ID = NAMESPACE.id("test");

    public TestEntity(World world) {
        super(world);
        setBoundingBoxSpacing(0.5F, 2);
    }

    public TestEntity(World world, double x, double y, double z) {
        this(world);
        method_1340(x, y + eyeHeight, z);
        prevX = x;
        prevY = y;
        prevZ = z;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(16, 0);
    }

    @Override
    protected void readNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

    }

    @Override
    public boolean method_1356() {
        return true;
    }

    @Override
    public void onPlayerInteraction(PlayerEntity player) {
        super.onPlayerInteraction(player);
        System.out.println(dataTracker.getInt(16));
    }

    @Override
    public boolean method_1323(PlayerEntity playerEntity) {
        super.method_1323(playerEntity);
        dataTracker.set(16, dataTracker.getInt(16) + 1);
        return true;
    }

    @Override
    public Identifier getHandlerIdentifier() {
        return ID;
    }

    @Override
    public boolean syncTrackerAtSpawn() {
        return true;
    }
}
