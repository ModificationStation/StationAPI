package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.item.ItemBase;
import net.minecraft.level.structure.Dungeon;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Dungeon.class)
public class MixinDungeon {

    @Redirect(
            method = "getRandomChestItem(Ljava/util/Random;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase getCocoaBeans() {
        return Items.COCOA_BEANS;
    }

    @ModifyConstant(
            method = "getRandomChestItem(Ljava/util/Random;)Lnet/minecraft/item/ItemInstance;",
            constant = @Constant(
                    intValue = 3,
                    ordinal = 1
            )
    )
    private int fixMeta(int constant) {
        return 0;
    }
}
