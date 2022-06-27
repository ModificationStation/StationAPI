package net.modificationstation.stationapi.mixin.block;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public class MixinBlock {

    @ModifyConstant(
            method = "<init>(I)V",
            constant = @Constant(intValue = 256)
    )
    private int getBlocksSize(int constant) {
        return BlockRegistry.INSTANCE.getSize();
    }

    @Redirect(
            method = "useOnTile(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;placeBlockWithMetaData(IIIII)Z"
            )
    )
    private boolean handlePlace(
            Level level, int x, int y, int z, int id, int meta,
            ItemInstance blockItem, PlayerBase player, Level argLevel, int argX, int argY, int argZ, int side
    ) {
        return StationAPI.EVENT_BUS.post(
                BlockEvent.BeforePlacedByItem.builder()
                        .level(level)
                        .player(player)
                        .x(x).y(y).z(z)
                        .block(BlockRegistry.INSTANCE.get(id).orElseThrow())
                        .meta(meta)
                        .blockItem(blockItem)
                        .placeFunction(() -> level.placeBlockWithMetaData(x, y, z, id, meta))
                        .build()
        ).placeFunction.getAsBoolean();
    }
}
