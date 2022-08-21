package net.modificationstation.stationapi.mixin.vanillafix.block.leaves;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Leaves;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Shears;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(Shears.class)
public class MixinShears {

    @Redirect(
            method = "postMine(Lnet/minecraft/item/ItemInstance;IIIILnet/minecraft/entity/Living;)Z",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Leaves;id:I"
            )
    )
    private int redirectField(Leaves instance, ItemInstance arg, int i, int j, int k, int l, Living arg2) {
        return switch (BlockRegistry.INSTANCE.getIdByLegacyId(i).orElseThrow().toString()) {
            case "minecraft:spruce_leaves",
                    "minecraft:birch_leaves"
                    -> i;
            default -> Blocks.OAK_LEAVES.id;
        };
    }

    @Redirect(
            method = "getStrengthOnBlock(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/block/BlockBase;)F",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/Leaves;id:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int redirectField(Leaves instance, ItemInstance arg, BlockBase arg2) {
        return (switch (Objects.requireNonNull(BlockRegistry.INSTANCE.getId(arg2)).toString()) {
            case "minecraft:spruce_leaves",
                    "minecraft:birch_leaves"
                    -> arg2;
            default -> Blocks.OAK_LEAVES;
        }).id;
    }
}
