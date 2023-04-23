package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.IsBlockReplaceableEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.item.StationFlatteningBlockItem;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock implements StationFlatteningBlockItem {
	@Shadow private int blockId;
	@Unique private short maxHeight;
	
	@Inject(method = "useOnTile", at = @At("HEAD"))
	private void storeLevel(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing, CallbackInfoReturnable<Boolean> info) {
		maxHeight = (short) (level.getTopY() - 1);
	}
	
	@ModifyConstant(method = "useOnTile", constant = @Constant(intValue = 127))
	private int changeMaxHeight(int value) {
		return maxHeight;
	}

	@Redirect(
			method = "useOnTile(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;IIII)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/level/Level;canPlaceTile(IIIIZI)Z"
			)
	)
	private boolean canReplace(
			Level argWorld, int blockID, int argX, int argY, int argZ, boolean checkCollision, int argSide,
			ItemInstance itemStack, PlayerBase player, Level world, int x, int y, int z, int side
	) {
		Direction direction = Direction.byId(side);
		Box box = BlockBase.BY_ID[blockID].getCollisionShape(world, x, y, z);
		return (box == null || world.canSpawnEntity(box)) && StationAPI.EVENT_BUS.post(
				IsBlockReplaceableEvent.builder()
						.context(new ItemPlacementContext(
										player,
										itemStack,
										new HitResult(
												x - direction.getOffsetX(),
												y - direction.getOffsetY(),
												z - direction.getOffsetZ(),
												side, Vec3f.from(x, y, z)
										)
						))
						.build()
		).context.canPlace() && BlockBase.BY_ID[blockID].canPlaceAt(world, x, y, z, side);
	}

	@Inject(
			method = "<init>",
			at = @At("RETURN")
	)
	private void registerCallback(int par1, CallbackInfo ci) {
		RegistryIdRemapCallback.event(BlockRegistry.INSTANCE).register(
				StationAPI.INTERNAL_PHASE,
				state -> blockId = state.getRawIdChangeMap().getOrDefault(blockId, blockId)
		);
	}

	@Override
	public BlockBase getBlock() {
		return BlockBase.BY_ID[blockId];
	}
}
