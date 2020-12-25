package net.modificationstation.stationapi.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.sortme.GameRenderer;
import net.modificationstation.stationapi.api.common.item.CustomReach;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow
    private Minecraft minecraft;

    @ModifyConstant(method = "method_1838(F)V", constant = @Constant(doubleValue = 3))
    private double getEntityReach(double originalReach) {
        Double defaultEntityReach = CustomReach.getDefaultEntityReach();
        Double handEntityReach = CustomReach.getHandEntityReach();
        if (defaultEntityReach != null)
            originalReach = defaultEntityReach;
        ItemInstance itemInstance = minecraft.player.getHeldItem();
        if (itemInstance == null) {
            if (handEntityReach != null)
                originalReach = handEntityReach;
        } else {
            ItemBase itemBase = itemInstance.getType();
            if (itemBase instanceof CustomReach) {
                originalReach = ((CustomReach) itemBase).getCustomEntityReach(itemInstance, originalReach);
            }
        }
        return originalReach;
    }
}
