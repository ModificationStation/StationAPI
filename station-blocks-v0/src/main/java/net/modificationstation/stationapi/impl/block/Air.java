package net.modificationstation.stationapi.impl.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.block.ReplaceableBlock;

import java.util.*;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

final class Air extends BlockBase implements ReplaceableBlock {

    Air(int id) {
        super(id, Material.AIR);
        setHardness(0);
        setBlastResistance(0);
        setSounds(BlockBase.GLASS_SOUNDS);
        setTranslationKey(of("air").toString());
        disableNotifyOnMetaDataChange();
        disableStat();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isSideRendered(BlockView tileView, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public Box getOutlineShape(Level level, int x, int y, int z) {
        return null;
    }

    @Override
    public Box getCollisionShape(Level level, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isFullOpaque() {
        return false;
    }

    @Override
    public boolean isCollidable(int meta, boolean flag) {
        return false;
    }

    @Override
    public int getDropCount(Random rand) {
        return 0;
    }

    @Override
    public HitResult method_1564(Level arg, int x, int y, int z, Vec3f arg1, Vec3f arg2) {
        return null;
    }

    @Override
    public boolean canBeReplaced(Level level, int x, int y, int z, BlockBase replacedBy, int replacedByMeta) {
        return true;
    }
}
