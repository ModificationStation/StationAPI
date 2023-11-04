package net.modificationstation.stationapi.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;

import java.util.Random;

import static net.modificationstation.stationapi.api.util.Identifier.of;

final class Air extends Block {

    Air(int id) {
        super(id, Material.AIR);
        setHardness(0);
        setResistance(0);
        setSoundGroup(Block.GLASS_SOUND_GROUP);
        setTranslationKey(of("air").toString());
        ignoreMetaUpdates();
        disableTrackingStatistics();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isSideVisible(BlockView tileView, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public Box getBoundingBox(World level, int x, int y, int z) {
        return null;
    }

    @Override
    public Box getCollisionShape(World level, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean hasCollision(int meta, boolean flag) {
        return false;
    }

    @Override
    public int getDroppedItemCount(Random rand) {
        return 0;
    }

    @Override
    public HitResult raycast(World arg, int x, int y, int z, Vec3d arg1, Vec3d arg2) {
        return null;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return true;
    }
}
