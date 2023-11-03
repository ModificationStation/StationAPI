package net.modificationstation.stationapi.mixin.flattening;

import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.IsBlockReplaceableEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapEvent;
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

@Mixin(BlockItem.class)
public class MixinBlock extends Item implements StationFlatteningBlockItem {
    @Shadow private int blockId;
    @Unique private short maxHeight;

    protected MixinBlock(int i) {
        super(i);
    }

    @Inject(method = "useOnTile", at = @At("HEAD"))
    private void storeLevel(ItemStack item, PlayerEntity player, World level, int x, int y, int z, int facing, CallbackInfoReturnable<Boolean> info) {
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
            World argWorld, int blockID, int argX, int argY, int argZ, boolean checkCollision, int argSide,
            ItemStack itemStack, PlayerEntity player, World world, int x, int y, int z, int side
    ) {
        Direction direction = Direction.byId(side);
        Box box = Block.BLOCKS[blockID].getCollisionShape(world, x, y, z);
        return (box == null || world.canSpawnEntity(box)) && StationAPI.EVENT_BUS.post(
                IsBlockReplaceableEvent.builder()
                        .context(new ItemPlacementContext(
                                player,
                                itemStack,
                                new HitResult(
                                        x - direction.getOffsetX(),
                                        y - direction.getOffsetY(),
                                        z - direction.getOffsetZ(),
                                        side, Vec3d.createCached(x, y, z)
                                )
                        ))
                        .build()
        ).context.canPlace() && Block.BLOCKS[blockID].canPlaceAt(world, x, y, z, side);
    }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void registerCallback(int par1, CallbackInfo ci) {
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<Block>>simple()
                .listener(event -> blockId = event.state.getRawIdChangeMap().getOrDefault(blockId, blockId))
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
    }

    @Override
    public Block getBlock() {
        return Block.BLOCKS[blockId];
    }

    @Override
    public void setBlock(Block block) {
        blockId = block.id;
        method_458(block.getTexture(2));
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;BY_ID:[Lnet/minecraft/block/BlockBase;",
                    args = "array=get"
            )
    )
    private Block failsafeBlock(Block[] array, int index) {
        return index < 0 ? null : array[index];
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(I)I"
            )
    )
    private int failsafeTexture(Block instance, int i) {
        return instance == null ? 0 : instance.getTexture(i);
    }
}
