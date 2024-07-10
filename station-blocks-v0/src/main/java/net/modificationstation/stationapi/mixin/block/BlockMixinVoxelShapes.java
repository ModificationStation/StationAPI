package net.modificationstation.stationapi.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.HasCollisionVoxelShape;
import net.modificationstation.stationapi.api.block.HasVoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(Block.class)
public class BlockMixinVoxelShapes {
    @Inject(method = "addIntersectingBoundingBox(Lnet/minecraft/world/World;IIILnet/minecraft/util/math/Box;Ljava/util/ArrayList;)V", at = @At("HEAD"), cancellable = true)
    private void  stationapi_addBlockVoxelShapesToCollision(World world, int x, int y, int z, Box box, ArrayList<Box> boxes, CallbackInfo ci) {
        if (this instanceof HasCollisionVoxelShape hasCollisionVoxelShape) {
            boxes.addAll(Arrays.asList(hasCollisionVoxelShape.getCollisionVoxelShape(world, x, y, z)));
            ci.cancel();
        } else if (this instanceof HasVoxelShape hasVoxelShape) {
            boxes.addAll(Arrays.asList(hasVoxelShape.getVoxelShape(world, x, y, z)));
            ci.cancel();
        }
    }

    @Inject(method = "raycast(Lnet/minecraft/world/World;IIILnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/hit/HitResult;", at = @At("HEAD"), cancellable = true)
    private void stationapi_allowMineThroughBlockWithVoxelShapes(World world, int x, int y, int z, Vec3d startPos, Vec3d endPos, CallbackInfoReturnable<HitResult> cir) {
        if (this instanceof HasVoxelShape hasVoxelShape) {
            startPos = startPos.add(-x, -y, -z);
            endPos = endPos.add(-x, -y, -z);
            for (Box translatedBox : hasVoxelShape.getVoxelShape(world, x, y, z)) {
                Box box = translatedBox.translate(-x, -y, -z);
                Vec3d northVec = startPos.interpolateByX(endPos, box.minX);
                Vec3d southVec = startPos.interpolateByX(endPos, box.maxX);
                Vec3d downVec = startPos.interpolateByY(endPos, box.minY);
                Vec3d upVec = startPos.interpolateByY(endPos, box.maxY);
                Vec3d eastVec = startPos.interpolateByZ(endPos, box.minZ);
                Vec3d westVec = startPos.interpolateByZ(endPos, box.maxZ);

                if (!containsInYZPlane(box, northVec)) northVec = null;
                if (!containsInYZPlane(box, southVec)) southVec = null;
                if (!containsInXZPlane(box, downVec)) downVec = null;
                if (!containsInXZPlane(box, upVec)) upVec = null;
                if (!containsInXYPlane(box, eastVec)) eastVec = null;
                if (!containsInXYPlane(box, westVec)) westVec = null;


                Vec3d hitPos = null;
                if (northVec != null && (hitPos == null || startPos.distanceTo(northVec) < startPos.distanceTo(hitPos))) {
                    hitPos = northVec;
                }

                if (southVec != null && (hitPos == null || startPos.distanceTo(southVec) < startPos.distanceTo(hitPos))) {
                    hitPos = southVec;
                }

                if (downVec != null && (hitPos == null || startPos.distanceTo(downVec) < startPos.distanceTo(hitPos))) {
                    hitPos = downVec;
                }

                if (upVec != null && (hitPos == null || startPos.distanceTo(upVec) < startPos.distanceTo(hitPos))) {
                    hitPos = upVec;
                }

                if (eastVec != null && (hitPos == null || startPos.distanceTo(eastVec) < startPos.distanceTo(hitPos))) {
                    hitPos = eastVec;
                }

                if (westVec != null && (hitPos == null || startPos.distanceTo(westVec) < startPos.distanceTo(hitPos))) {
                    hitPos = westVec;
                }

                if (hitPos != null) {
                    byte side = -1;
                    if (hitPos == northVec) side = 4;
                    if (hitPos == southVec) side = 5;
                    if (hitPos == downVec) side = 0;
                    if (hitPos == upVec) side = 1;
                    if (hitPos == eastVec) side = 2;
                    if (hitPos == westVec) side = 3;

                    cir.setReturnValue(new HitResult(x, y, z, side, hitPos.add(x, y, z)));
                    return;
                }
            }
            cir.setReturnValue(null);
        }
    }

    @Unique
    private boolean containsInYZPlane(Box box, Vec3d vec3d) {
        if (vec3d == null) {
            return false;
        } else {
            return vec3d.y >= box.minY && vec3d.y <= box.maxY && vec3d.z >= box.minZ && vec3d.z <= box.maxZ;
        }
    }

    @Unique
    private boolean containsInXZPlane(Box box, Vec3d vec3d) {
        if (vec3d == null) {
            return false;
        } else {
            return vec3d.x >= box.minX && vec3d.x <= box.maxX && vec3d.z >= box.minZ && vec3d.z <= box.maxZ;
        }
    }

    @Unique
    private boolean containsInXYPlane(Box box, Vec3d vec3d) {
        if (vec3d == null) {
            return false;
        } else {
            return vec3d.x >= box.minX && vec3d.x <= box.maxX && vec3d.y >= box.minY && vec3d.y <= box.maxY;
        }
    }
}
