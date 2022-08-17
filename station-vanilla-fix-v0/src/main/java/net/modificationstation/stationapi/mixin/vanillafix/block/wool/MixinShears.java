package net.modificationstation.stationapi.mixin.vanillafix.block.wool;

import net.minecraft.block.BlockBase;
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
            method = "getStrengthOnBlock(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/block/BlockBase;)F",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;id:I",
                    ordinal = 3
            )
    )
    private int normalizeWoolBlockID(BlockBase instance) {
        return switch (Objects.requireNonNull(BlockRegistry.INSTANCE.getId(instance)).toString()) {
            case
                    "minecraft:orange_wool",
                    "minecraft:magenta_wool",
                    "minecraft:light_blue_wool",
                    "minecraft:yellow_wool",
                    "minecraft:lime_wool",
                    "minecraft:pink_wool",
                    "minecraft:gray_wool",
                    "minecraft:light_gray_wool",
                    "minecraft:cyan_wool",
                    "minecraft:purple_wool",
                    "minecraft:blue_wool",
                    "minecraft:brown_wool",
                    "minecraft:green_wool",
                    "minecraft:red_wool",
                    "minecraft:black_wool"
                -> Blocks.WHITE_WOOL.id;
            default -> instance.id;
        };
    }

    @Redirect(
            method = "getStrengthOnBlock(Lnet/minecraft/item/ItemInstance;Lnet/minecraft/block/BlockBase;)F",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private BlockBase redirectWoolField() {
        return Blocks.WHITE_WOOL;
    }
}
