package net.modificationstation.stationapi.mixin.template.item;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.class_259;
import net.minecraft.item.DoorItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.template.item.TemplateDoorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DoorItem.class)
class DoorItemMixin extends Item {
    @Unique
    private static final Material UNUSED_MATERIAL = new Material(class_259.field_2738);

    public DoorItemMixin(int id) {
        super(id);
    }

    // Why do you only hit ordinal 1, this shit's so fucking annoying
    @ModifyVariable(method = "useOnBlock", at = @At(value = "STORE"))
    private Block hijackBlock(Block value) {
        if (TemplateDoorItem.class.isAssignableFrom(getClass())) {
            //noinspection DataFlowIssue
            return ((TemplateDoorItem) (Object) this).getDoorBlock();
        }
        return value;
    }

    // Workaround for said angry comment.
    @WrapOperation(method = "useOnBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/item/DoorItem;material:Lnet/minecraft/block/Material;"))
    private Material hijackPlacement(DoorItem instance, Operation<Material> original) {
        if (TemplateDoorItem.class.isAssignableFrom(getClass())) {
            return UNUSED_MATERIAL; // To ensure mods doing something similar to vanilla aren't broken by this.
        }

        return original.call(instance);
    }
}
