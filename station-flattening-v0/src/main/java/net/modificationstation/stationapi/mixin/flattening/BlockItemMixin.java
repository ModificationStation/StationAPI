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
class BlockItemMixin extends Item implements StationFlatteningBlockItem {
    @Shadow private int itemId;
    @Unique private short maxHeight;

    protected BlockItemMixin(int i) {
        super(i);
    }

    @Inject(
            method = "useOnBlock",
            at = @At("HEAD")
    )
    private void stationapi_storeWorld(ItemStack item, PlayerEntity player, World world, int x, int y, int z, int facing, CallbackInfoReturnable<Boolean> info) {
        maxHeight = (short) (world.getTopY() - 1);
    }

    @ModifyConstant(
            method = "useOnBlock",
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeMaxHeight(int value) {
        return maxHeight;
    }

    @Redirect(
            method = "useOnBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_156(IIIIZI)Z"
            )
    )
    private boolean stationapi_canReplace(
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
    private void stationapi_registerCallback(int par1, CallbackInfo ci) {
        BlockRegistry.INSTANCE.getEventBus().register(Listener.<RegistryIdRemapEvent<Block>>simple()
                .listener(event -> itemId = event.state.getRawIdChangeMap().getOrDefault(itemId, itemId))
                .phase(StationAPI.INTERNAL_PHASE)
                .build());
    }

    @Override
    @Unique
    public Block getBlock() {
        return Block.BLOCKS[itemId];
    }

    @Override
    @Unique
    public void setBlock(Block block) {
        itemId = block.id;
        method_458(block.getTexture(2));
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Block;BLOCKS:[Lnet/minecraft/block/Block;",
                    args = "array=get"
            )
    )
    private Block stationapi_failsafeBlock(Block[] array, int index) {
        return index < 0 ? null : array[index];
    }

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getTexture(I)I"
            )
    )
    private int stationapi_failsafeTexture(Block instance, int i) {
        return instance == null ? 0 : instance.getTexture(i);
    }
}
