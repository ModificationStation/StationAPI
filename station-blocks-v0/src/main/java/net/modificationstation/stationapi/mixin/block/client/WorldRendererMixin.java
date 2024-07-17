package net.modificationstation.stationapi.mixin.block.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.HasVoxelShape;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import net.modificationstation.stationapi.impl.block.Line;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.modificationstation.stationapi.impl.block.BoxToLinesConverter.convertBoxesToLines;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private World world;

    @Unique
    Block lastBlock = null;
    @Unique
    HitResult lastHitResult = null;
    @Unique
    List<Line> lines = null;

    @Inject(method = "method_1554(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    private void stationapi_drawBlockVoxelShapesOutline(PlayerEntity playerEntity, HitResult hitResult, int i, ItemStack itemStack, float f, CallbackInfo ci) {
        BlockState blockState = world.getBlockState(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
        if (i == 0 && hitResult.type == HitResultType.BLOCK && blockState.getBlock() instanceof HasVoxelShape block) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            int var7 = world.getBlockId(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            if (var7 > 0) {
                Block.BLOCKS[var7].updateBoundingBox(this.world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                // field_1637, field_1638, field_1639 is lastTickX, lastTickY, lastTickZ respectively
                // interpolates where to render based off player's old position + position change * frameDelta
                double interpolatedX = playerEntity.field_1637 + (playerEntity.x - playerEntity.field_1637) * (double)f;
                double interpolatedY = playerEntity.field_1638 + (playerEntity.y - playerEntity.field_1638) * (double)f;
                double interpolatedZ = playerEntity.field_1639 + (playerEntity.z - playerEntity.field_1639) * (double)f;

                if (!((lastBlock != null && ((lastBlock.id == blockState.getBlock().id) && lastHitResult.blockX == hitResult.blockX && lastHitResult.blockY == hitResult.blockY && lastHitResult.blockZ == hitResult.blockZ)))) {
                    Vec3d center = new Vec3d(hitResult.blockX + 0.5, hitResult.blockY + 0.5, hitResult.blockZ + 0.5);
                    lines = convertBoxesToLines(block.getVoxelShape(world, hitResult.blockX, hitResult.blockY, hitResult.blockZ), center);
                }

                Tessellator tessellator = Tessellator.INSTANCE;
                tessellator.start(1);
                for (Line line : lines) {
                    tessellator.vertex(-interpolatedX + line.points[0].x, -interpolatedY + line.points[0].y, -interpolatedZ + line.points[0].z);
                    tessellator.vertex(-interpolatedX + line.points[1].x, -interpolatedY + line.points[1].y, -interpolatedZ + line.points[1].z);
                }
                tessellator.draw();

                lastHitResult = hitResult;
                lastBlock = blockState.getBlock();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glDisable(3042);

            ci.cancel();
        }

    }
}
