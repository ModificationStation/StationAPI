package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicOverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HeldItemRenderer.class)
@Environment(EnvType.CLIENT)
class class_556Mixin {
    @Unique
    private final ArsenicOverlayRenderer arsenic_plugin = new ArsenicOverlayRenderer((HeldItemRenderer) (Object) this);

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void method_1862(LivingEntity entity, ItemStack item) {
        arsenic_plugin.renderItem3D(entity, item);
    }

    /**
     * @reason there's no saving notch code
     * @author mine_diver
     */
    @Overwrite
    public void method_1860(float f) {
        arsenic_plugin.renderItem(f);
    }
}
