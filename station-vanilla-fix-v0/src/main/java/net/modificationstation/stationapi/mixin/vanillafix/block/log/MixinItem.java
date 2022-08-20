package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.Item;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.block.BlockStateHolder;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.vanillafix.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(Item.class)
public class MixinItem {

    @Shadow public ItemInstance item;

    @Redirect(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerBase;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;id:I",
                    opcode = GETFIELD
            )
    )
    private int getId(BlockBase instance) {
        return BlockRegistry.INSTANCE.getByLegacyId(item.itemId).stream().mapToInt(block -> ((BlockStateHolder) block).getDefaultState().isIn(BlockTags.LOGS) ? item.itemId : -1).findFirst().orElse(-1);
    }
}
