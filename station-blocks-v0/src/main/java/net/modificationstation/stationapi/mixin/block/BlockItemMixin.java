package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
class BlockItemMixin {
    @WrapOperation(
            method = "useOnBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;method_201(IIIII)Z"
            )
    )
    private boolean stationapi_handlePlace(
            World world, int x, int y, int z, int id, int meta, Operation<Boolean> placeFunction,
            ItemStack blockItem, PlayerEntity player, World argWorld, int argX, int argY, int argZ, int side
    ) {
        return StationAPI.EVENT_BUS.post(
                BlockEvent.BeforePlacedByItem.builder()
                        .world(world)
                        .player(player)
                        .x(x).y(y).z(z)
                        .side(Direction.byId(side))
                        .block(BlockRegistry.INSTANCE.get(id))
                        .meta(meta)
                        .blockItem(blockItem)
                        .placeFunction(() -> placeFunction.call(world, x, y, z, id, meta))
                        .build()
        ).placeFunction.getAsBoolean();
    }
}
